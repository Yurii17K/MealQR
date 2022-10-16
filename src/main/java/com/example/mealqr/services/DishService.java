package com.example.mealqr.services;

import com.example.mealqr.domain.CustomerAllergy;
import com.example.mealqr.domain.Dish;
import com.example.mealqr.domain.DishImage;
import com.example.mealqr.preferenceAnalysis.SlopeOne;
import com.example.mealqr.repositories.CustomerAllergyRepository;
import com.example.mealqr.repositories.DishImageRepository;
import com.example.mealqr.repositories.DishRepository;
import com.example.mealqr.repositories.RestaurantRepository;
import com.example.mealqr.rest.reponse.DishRes;
import com.example.mealqr.rest.reponse.DishWithOpinionsRes;
import com.example.mealqr.rest.request.DishSaveReq;
import com.example.mealqr.rest.request.DishUpdateReq;
import com.example.mealqr.services.mappers.DishResMapper;
import com.example.mealqr.services.mappers.ImageDtoMapper;
import io.vavr.API;
import io.vavr.collection.Seq;
import io.vavr.collection.Vector;
import io.vavr.control.Either;
import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DishService {

    private final DishOpinionService dishOpinionService;
    private final DishRepository dishRepository;
    private final RestaurantRepository restaurantRepository;
    private final CustomerAllergyRepository customerAllergyRepository;
    private final DishImageRepository dishImageRepository;
    private final SlopeOne slopeOne;

    private static final Random RANDOM = new Random();

    public Either<String, Seq<DishRes>> getAllDishesInRestaurant(@NotBlank String restaurantName) {
        return API.Some(dishRepository.findAllByRestaurantName(restaurantName))//
                .filter(Seq::nonEmpty)//
                .map(dishes -> dishes.map(DishResMapper::mapToDishRes))//
                .toEither("This restaurant's menu is empty");
    }

    public List<DishWithOpinionsRes> getAllDishesInRestaurantConfiguredForUser(String userEmail, String restaurantName) {
        if (!restaurantRepository.existsByRestaurantName(restaurantName)) {
            return new LinkedList<>();
        }
        return getAllDishesInRestaurantSortedByUserPreference(userEmail, restaurantName) // analyse user preferences
                .stream()//
                .filter(dish -> !isUserAllergicToDish(userEmail, dish)) // remove allergies
                .map(dish -> DishWithOpinionsRes.of(
                        dish.getDishId().toString(),//
                        dish.getDishName(),//
                        dish.getRestaurantName(),//
                        ImageDtoMapper.mapToImageDto(dish.getDishImage()),//
                        dish.getDishPrice().doubleValue(),//
                        dish.getDishDescription(),//
                        dishOpinionService.getDishAverageRating(dish.getDishId()),//
                        dishOpinionService.getDishComments(dish.getDishId())//
                                .map(dishComment -> DishWithOpinionsRes.DishCommentDto.of(
                                        dishComment.getUserEmail(),//
                                        dishComment.getComment()))))//
                .collect(Collectors.toCollection(LinkedList::new));
    }

    public Either<String, DishRes> getRandomDish() {
        Seq<Dish> dishes = Vector.ofAll(dishRepository.findAll());
        return dishes.headOption()//
                .map(dosh -> dishes.get(RANDOM.nextInt(dishes.size())))//
                .map(DishResMapper::mapToDishRes)//
                .toEither("App is in the development stage, no dishes");
    }

    public Either<String, DishRes> getRandomDishFromRestaurantOffer(@NotBlank String restaurantName) {
        Seq<Dish> dishes = dishRepository.findAllByRestaurantName(restaurantName);
        return dishes
                .headOption()//
                .map(dosh -> dishes.get(RANDOM.nextInt(dishes.size())))//
                .map(DishResMapper::mapToDishRes)//
                .toEither("This restaurant's menu is empty");
    }

    private LinkedList<Dish> getAllDishesInRestaurantSortedByUserPreference(String userEmail, String restaurantName) {
        io.vavr.collection.Map<Integer, Dish> dishesWithIdsInRestaurant = dishRepository
                .findAllByRestaurantName(restaurantName)//
                .toMap(Dish::getDishId, Function.identity());

        // results of user preference analysis
        return slopeOne.slopeOne(userEmail, dishOpinionService.getDataForPreferenceAnalysis(restaurantName))//
                .entrySet().stream()//
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())) // get most preferable at the top
                .map(integerDoubleEntry -> dishesWithIdsInRestaurant.get(integerDoubleEntry.getKey()).get()) // map dishIds to Dish objects
                .collect(Collectors.toCollection(LinkedList::new)); // collect to a linked list to preserve sorted order
    }

    public Either<String, DishRes> addDishToRestaurantMenu(DishSaveReq dishSaveReq) {
        DishImage dishImage = DishImage.builder()//
                .data(dishSaveReq.getDishImage().getBase64Data().getBytes(StandardCharsets.UTF_8))//
                .contentType(dishSaveReq.getDishImage().getContentType())//
                .build();
        Dish dishToBeAdded = Dish.builder()//
                .dishName(dishSaveReq.getDishName())//
                .restaurantName(dishSaveReq.getRestaurantName())//
                .dishDescription(dishSaveReq.getDishDescription())//
                .dishPrice(dishSaveReq.getDishPrice())//
                .dishImage(dishImageRepository.save(dishImage))
                .build();
        return API.Some(dishRepository.findByDishNameAndRestaurantName(dishSaveReq.getDishName(),
                        dishSaveReq.getRestaurantName()))//
                .filter(Option::isEmpty)//
                .map(noDishInDb -> DishResMapper.mapToDishRes(dishRepository.save(dishToBeAdded)))//
                .toEither("Dish with this name already exists in " + dishSaveReq.getRestaurantName() + "s offer");
    }

    public Either<String, DishRes> removeDishFromRestaurantOffer(@NotBlank String dishName, @NotBlank String restaurantName) {
        return dishRepository.findByDishNameAndRestaurantName(dishName, restaurantName)//
                .peek(dish -> dishRepository.deleteByDishNameAndRestaurantName(dishName, restaurantName))//
                .map(DishResMapper::mapToDishRes)//
                .toEither("Dish with this name does not exist in this restaurant offer");
    }

    public Either<String, DishRes> updateDishInRestaurantOffer(DishUpdateReq dishUpdateReq) {
        DishImage dishImageToUpdate = dishUpdateReq.getDishImage()//
                .map(newImage -> {
                    DishImage updatedDishImage = DishImage.builder()//
                            .data(newImage.getBase64Data().getBytes(StandardCharsets.UTF_8))//
                            .contentType(newImage.getContentType())//
                            .build();
                    return dishImageRepository.save(updatedDishImage);
                })//
                .getOrElse(DishImage.builder().build());
        return dishRepository.findByDishId(dishUpdateReq.getDishId())//
                .map(dishInDb -> {
                    Dish updatedDish = Dish.builder()//
                            .dishId(dishInDb.getDishId())//
                            .dishPrice(dishUpdateReq.getDishPrice().getOrElse(dishInDb.getDishPrice()))//
                            .dishDescription(dishUpdateReq.getDishDescription().getOrElse(dishInDb.getDishDescription()))//
                            .dishImage(dishUpdateReq.getDishImage()//
                                    .map(d -> dishImageToUpdate)//
                                    .getOrElse(dishInDb.getDishImage()))//
                            .build();
                    return DishResMapper.mapToDishRes(dishRepository.save(updatedDish));
                })//
                .toEither("No such dish in restaurant's offer");
    }

    private boolean isUserAllergicToDish(@NotBlank String userEmail, @NotNull Dish dish){
        return customerAllergyRepository.findByUserEmail(userEmail)//
                .map(CustomerAllergy::getAllergies)//
                .map(allergies -> allergies.toLowerCase(Locale.ROOT).split("(, )|,"))//
                .map(allergies -> Arrays.stream(allergies)//
                        .anyMatch(allergy -> dish.getDishDescription()//
                                .toLowerCase(Locale.ROOT)//
                                .contains(allergy)))//
                .getOrElse(false);
    }
}
