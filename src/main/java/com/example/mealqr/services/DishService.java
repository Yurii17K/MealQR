package com.example.mealqr.services;

import com.example.mealqr.domain.CartItem;
import com.example.mealqr.domain.CustomerAllergy;
import com.example.mealqr.domain.Dish;
import com.example.mealqr.domain.Restaurant;
import com.example.mealqr.exceptions.ApiError;
import com.example.mealqr.preferenceAnalysis.SlopeOne;
import com.example.mealqr.repositories.*;
import com.example.mealqr.security.CustomPrincipal;
import com.example.mealqr.services.mappers.DishResMapper;
import com.example.mealqr.services.mappers.DishWithOpinionsResMapper;
import com.example.mealqr.web.rest.reponse.DishRes;
import com.example.mealqr.web.rest.reponse.DishWithOpinionsRes;
import com.example.mealqr.web.rest.request.DishSaveReq;
import com.example.mealqr.web.rest.request.DishUpdateReq;
import io.vavr.API;
import io.vavr.Function3;
import io.vavr.collection.Seq;
import io.vavr.collection.Stream;
import io.vavr.collection.Vector;
import io.vavr.control.Either;
import io.vavr.control.Option;
import io.vavr.control.Validation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.security.Principal;
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
    private final CartItemRepository cartItemRepository;
    private final MasterRecommenderService masterRecommenderService;
    private final DishRatingRepository dishRatingRepository;
    private final DishCommentRepository dishCommentRepository;


    private static final SecureRandom RANDOM = new SecureRandom();

    public Either<ApiError, Seq<DishRes>> getAllDishesInRestaurant(@NotBlank String restaurantId) {


        try{
            var dishes = dishRepository.findAllByRestaurantRestaurantId(restaurantId).stream().map(x->DishResMapper.mapToDishRes(x));
            var vavrList = Vector.ofAll(dishes);
            return Either.right(vavrList);
        }catch (Exception e){
            return Either.left(ApiError.buildError("The restaurant is empty or doesn't exist"));
        }


        //return Either.right(Vector.ofAll(dishes)).orElse(ApiError.buildError("The restaurant is empty or doesn't exist"));

        /*
        return API.Some(vavrStream//
                .filter(Seq::nonEmpty)//
                .map(dishes -> dishes.map(DishResMapper::mapToDishRes))
                .toEither(ApiError.buildError("The restaurant is empty or doesn't exist")));

         */
    }

    public Either<ApiError, DishRes> getSpecificDish(@NotBlank String dishId) {

        var optional = dishRepository.findByDishId(dishId);
        if(optional.isEmpty()){
            return Either.left(ApiError.buildError("The dish does not exist"));
        }else{
            var dishRes = DishResMapper.mapToDishRes(optional.get());
            return Either.right(dishRes);
        }
    }

    public List<DishWithOpinionsRes> getAllDishesInRestaurantConfiguredForUser(String userEmail, String restaurantId) {
        if (restaurantRepository.findByRestaurantId(restaurantId).isEmpty()) {
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

    public Either<ApiError, DishRes> getRandomDish() {
        Seq<Dish> dishes = Vector.ofAll(dishRepository.findAll());
        return dishes.headOption()//
                .map(dosh -> dishes.get(RANDOM.nextInt(dishes.size())))//
                .map(DishResMapper::mapToDishRes)//
                .toEither(ApiError.buildError("App is in the development stage, no dishes", HttpStatus.NOT_FOUND));
    }

    public Either<ApiError, DishRes> getRandomDishFromRestaurantOffer(@NotBlank String restaurantId) {
        List<Dish> dishes = dishRepository.findAllByRestaurantRestaurantId(restaurantId);

        var vavrStream = Stream.ofAll(dishes.stream());
        return vavrStream.headOption()
                .map(dosh -> dishes.get(RANDOM.nextInt(dishes.size())))
                .map(DishResMapper::mapToDishRes)//
                .toEither(ApiError.buildError("The restaurant is empty or doesn't exist"));
    }

    private LinkedList<Dish> getAllDishesInRestaurantSortedByUserPreference(String userEmail, String restaurantId) {
        Map<String, Dish> dishesWithIdsInRestaurant = dishRepository
                .findAllByRestaurantRestaurantId(restaurantId)
                .stream()
                .collect(Collectors.toMap(Dish::getDishId, Function.identity()));


        // results of user preference analysis

        var vavrStream = Stream.ofAll(masterRecommenderService.getDishRatingPredictionsForRestaurantAndUser(userEmail,restaurantId)//
                .entrySet().stream());
        return vavrStream
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())) // get most preferable at the top
                .map(ratingByDishId -> dishesWithIdsInRestaurant.get(ratingByDishId.getKey())) // map dishIds to Dish objects
                .collect(Collectors.toCollection(LinkedList::new)); // collect to a linked list to preserve sorted order
    }

    public Either<ApiError, DishRes> addDishToRestaurantMenu(DishSaveReq dishSaveReq) {
        return API.Some(dishRepository.findByDishNameAndRestaurantRestaurantId(dishSaveReq.getDishName(),
                        dishSaveReq.getRestaurantId()))//
                .filter(Option::isEmpty)//
                .map(notPresent -> Dish.create(dishSaveReq))//
                .map(dishRepository::save)//
                .map(DishResMapper::mapToDishRes)//
                .toEither(ApiError.buildError("Dish with this name already exists in the restaurant"));
    }

    @Transactional
    public Either<Seq<ApiError>, Boolean> removeDishFromRestaurantOffer(@NotBlank String dishId, CustomPrincipal customPrincipal) {
        return validateDishDelete(dishId, customPrincipal.getRestaurantIds())//
                .peek(dish -> dishRepository.deleteById(dishId))//
                .peek(dish->dishRatingRepository.deleteByDishId(dishId))
                .peek(dish->dishCommentRepository.deleteByDishId(dishId))
                .toEither();
    }

    public Either<ApiError, DishRes> updateDishInRestaurantOffer(DishUpdateReq dishUpdateReq) {
        return validateDishUpdate(dishUpdateReq)//
                .map(originalDish -> DishResMapper.mapToDishRes(updateDish(dishUpdateReq, originalDish)))//
                .toEither();
    }

    private Dish updateDish(DishUpdateReq dishUpdateReq, Dish originalDish) {
        return dishRepository.save(Dish.update(dishUpdateReq, originalDish));
    }

    private Validation<Seq<ApiError>, Boolean> validateDishDelete(String dishId, Set<String> restaurantIds) {
        Option<Dish> dishOption = dishRepository.findByDishId(dishId);
        return Validation.combine(
                        dishOption.toValidation(ApiError.buildError(
                                "Dish with this id does not exist in the restaurant", HttpStatus.NOT_FOUND)),
                        validateDishIsNotInCart(dishOption),
                        validateAccessToDeleteDish(restaurantIds, dishOption))//
                .ap(Function3.constant(true))//
                .mapError(Function.identity());
    }

    private Validation<ApiError, String> validateAccessToDeleteDish(Set<String> restaurantIds, Option<Dish> dishOption) {
        return dishOption.map(Dish::getRestaurant)//
                .map(Restaurant::getRestaurantId)//
                .filter(restaurantIds::contains)//
                .toValidation(ApiError.buildError("You do not have permission to delete this dish", HttpStatus.FORBIDDEN));
    }

    private Validation<ApiError, Seq<CartItem>> validateDishIsNotInCart(Option<Dish> dishOption) {
        return dishOption//
                .map(cartItemRepository::findAllByDish)//
                .filter(Seq::isEmpty)//
                .toValidation(ApiError.buildError("Invalid state! Dish is in someone's cart"));
    }

    private Validation<ApiError, Dish> validateDishUpdate(DishUpdateReq dishUpdateReq) {
        Option<Dish> dishById = dishRepository.findByDishId(dishUpdateReq.getDishId());
        if (dishById.isEmpty()){
            return Validation.invalid(ApiError.buildError("Dish does not exist", HttpStatus.NOT_FOUND));
        }

        if (dishUpdateReq.getDishName().isEmpty() || dishUpdateReq.getDishName().get().equals(dishById.get().getDishName())) {
            return Validation.valid(dishById.get());
        } else {
            return API.Some(dishRepository.findByDishNameAndRestaurantRestaurantId(dishUpdateReq.getDishName().get(),
                                                            dishById.get().getRestaurant().getRestaurantId()))
                                                    .filter(Option::isEmpty)//
                                                    .flatMap(dishNotPresent -> dishById)//
                                                    .toValidation(ApiError.buildError("Dish with this name already exists in the restaurant"));
        }
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
