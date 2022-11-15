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
import com.example.mealqr.services.mappers.DishWithOpinionsResMapper;
import io.vavr.API;
import io.vavr.collection.Seq;
import io.vavr.collection.Vector;
import io.vavr.control.Either;
import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.security.SecureRandom;
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

    private static final SecureRandom RANDOM = new SecureRandom();

    public Either<String, Seq<DishRes>> getAllDishesInRestaurant(@NotBlank String restaurantId) {
        return API.Some(dishRepository.findAllByRestaurantRestaurantId(restaurantId))//
                .filter(Seq::nonEmpty)//
                .map(dishes -> dishes.map(DishResMapper::mapToDishRes))//
                .toEither("This restaurant's menu is empty");
    }

    public List<DishWithOpinionsRes> getAllDishesInRestaurantConfiguredForUser(String userEmail, String restaurantId) {
        if (!restaurantRepository.existsByRestaurantId(restaurantId)) {
            return new LinkedList<>();
        }
        return getAllDishesInRestaurantSortedByUserPreference(userEmail, restaurantId) // analyse user preferences
                .stream()//
                .filter(dish -> !isUserAllergicToDish(userEmail, dish)) // remove allergies
                .map(dish -> DishWithOpinionsResMapper.mapToDishWithOpinionsRes(
                        dish,
                        dishOpinionService.getDishAverageRating(dish.getDishId()),
                        dishOpinionService.getDishComments(dish.getDishId())))//
                .collect(Collectors.toCollection(LinkedList::new));
    }

    public Either<String, DishRes> getRandomDish() {
        Seq<Dish> dishes = Vector.ofAll(dishRepository.findAll());
        return dishes.headOption()//
                .map(dosh -> dishes.get(RANDOM.nextInt(dishes.size())))//
                .map(DishResMapper::mapToDishRes)//
                .toEither("App is in the development stage, no dishes");
    }

    public Either<String, DishRes> getRandomDishFromRestaurantOffer(@NotBlank String restaurantId) {
        Seq<Dish> dishes = dishRepository.findAllByRestaurantRestaurantId(restaurantId);
        return dishes.headOption()//
                .map(dosh -> dishes.get(RANDOM.nextInt(dishes.size())))//
                .map(DishResMapper::mapToDishRes)//
                .toEither("This restaurant's menu is empty");
    }

    private LinkedList<Dish> getAllDishesInRestaurantSortedByUserPreference(String userEmail, String restaurantId) {
        io.vavr.collection.Map<Integer, Dish> dishesWithIdsInRestaurant = dishRepository
                .findAllByRestaurantRestaurantId(restaurantId)//
                .toMap(Dish::getDishId, Function.identity());

        // results of user preference analysis
        return slopeOne.slopeOne(userEmail, dishOpinionService.getDataForPreferenceAnalysis(restaurantId))//
                .entrySet().stream()//
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())) // get most preferable at the top
                .map(integerDoubleEntry -> dishesWithIdsInRestaurant.get(integerDoubleEntry.getKey()).get()) // map dishIds to Dish objects
                .collect(Collectors.toCollection(LinkedList::new)); // collect to a linked list to preserve sorted order
    }

    public Either<String, DishRes> addDishToRestaurantMenu(DishSaveReq dishSaveReq) {
        DishImage dishImage = DishImage.of(dishSaveReq);
        Dish dishToBeAdded = Dish.of(dishSaveReq, dishImage);
        dishImageRepository.save(dishImage);
        return API.Some(dishRepository.findByDishNameAndRestaurantRestaurantId(dishSaveReq.getDishName(), dishSaveReq.getRestaurantId()))//
                .filter(Option::isEmpty)//
                .map(noDishInDb -> DishResMapper.mapToDishRes(dishRepository.save(dishToBeAdded)))//
                .toEither("Dish with this name already exists in your restaurant");
    }

    public Either<String, DishRes> removeDishFromRestaurantOffer(@NotBlank String dishName, @NotBlank String restaurantId) {
        return dishRepository.findByDishNameAndRestaurantRestaurantId(dishName, restaurantId)//
                .peek(dish -> dishRepository.deleteByDishNameAndRestaurantRestaurantId(dishName, restaurantId))//
                .map(DishResMapper::mapToDishRes)//
                .toEither("Dish with this name does not exist in this restaurant offer");
    }

    public Either<String, DishRes> updateDishInRestaurantOffer(DishUpdateReq dishUpdateReq) {
        DishImage updatedDishImage = DishImage.of(dishUpdateReq, dishImageRepository::save);
        return dishRepository.findByDishId(dishUpdateReq.getDishId())//
                .map(dishInDb -> DishResMapper.mapToDishRes(
                        dishRepository.save(Dish.of(dishUpdateReq, dishInDb, updatedDishImage))))//
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
