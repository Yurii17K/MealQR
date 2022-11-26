package com.example.mealqr.services;

import com.example.mealqr.domain.Dish;
import com.example.mealqr.domain.DishComment;
import com.example.mealqr.domain.DishRating;
import com.example.mealqr.exceptions.ApiError;
import com.example.mealqr.repositories.DishCommentRepository;
import com.example.mealqr.repositories.DishRatingRepository;
import com.example.mealqr.repositories.DishRepository;
import com.example.mealqr.repositories.UserRepository;
import com.example.mealqr.web.rest.request.DishCommentReq;
import com.example.mealqr.web.rest.request.DishRatingReq;
import io.vavr.collection.Seq;
import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class DishOpinionService {

    private static final String SUCH_DISH_DOES_NOT_EXIST = "Such dish does not exist";

    private final DishCommentRepository dishCommentRepository;
    private final DishRatingRepository dishRatingRepository;
    private final DishRepository dishRepository;
    private final UserRepository userRepository;

    public Either<ApiError, DishRating> addOrUpdateRating(String userEmail, DishRatingReq dishRatingReq) {
        return dishRepository.findByDishId(dishRatingReq.getDishId())//
                .map(dish -> dishRatingRepository.findByDishDishIdAndUserEmail(dish.getDishId(), userEmail)//
                        .peek(dishRating -> dishRatingRepository.save(dishRating.withRating(dishRatingReq.getRating())))//
                        .getOrElse(() -> dishRatingRepository.save(DishRating.of(userEmail, dishRatingReq, dish))))//
                .toEither(ApiError.buildError(SUCH_DISH_DOES_NOT_EXIST, HttpStatus.NOT_FOUND));
    }

    public Either<ApiError, DishComment> addOrUpdateComment(String userEmail, DishCommentReq dishCommentReq) {
        return dishRepository.findByDishId(dishCommentReq.getDishId())//
                .map(dish -> dishCommentRepository.findByDishDishIdAndUserEmail(dish.getDishId(), userEmail)//
                        .peek(dishComment -> dishCommentRepository.save(dishComment.withComment(dishCommentReq.getComment())))//
                        .getOrElse(() -> dishCommentRepository.save(DishComment.of(userEmail, dishCommentReq, dish))))//
                .toEither(ApiError.buildError(SUCH_DISH_DOES_NOT_EXIST, HttpStatus.NOT_FOUND));
    }


    //Returns <User email, <Dish_id, Dish_rating>> for all users that rated something in the given restaurant
    //Eg. a map from users to their respective dish ratings
    Map<String, Map<String, Double>> getDataForPreferenceAnalysis(@NotBlank String restaurantId) {
        Seq<String> dishIdsByRestaurant = dishRepository.findAllByRestaurantRestaurantId(restaurantId)//
                .map(Dish::getDishId)//
                .sorted();

        // take only the customers that rated sth in the restaurant
        Seq<String> clientEmails = userRepository.findAllClientsWhoRatedSomethingInRestaurant(restaurantId)
                .distinct();
        return clientEmails.toMap(Function.identity(),
                        email -> {
                            // create a map of dishId:rating
                            Map<String, Double> dishIdsWithRatingsByEmailAndRestaurant = dishRatingRepository
                                    .findAllByUserEmailAndRestaurant(email, restaurantId)//
                                    .toMap(dishRating -> dishRating.getDish().getDishId(),d -> (double) d.getRating())
                                    .toJavaMap();
                            // generate random ratings for all unrated dishes
                            if (dishIdsByRestaurant.size() != dishIdsWithRatingsByEmailAndRestaurant.size()) {
                                for (int i = dishIdsWithRatingsByEmailAndRestaurant.size(); i < dishIdsByRestaurant.size(); i++) {
                                    dishIdsWithRatingsByEmailAndRestaurant.put(dishIdsByRestaurant.get(i), Math.random() * 5);
                                }
                            }
                            return dishIdsWithRatingsByEmailAndRestaurant;
                        }).toJavaMap();
    }

    Seq<DishComment> getDishComments(@NotNull String dishID) {
        return dishCommentRepository.findAllByDishDishId(dishID);
    }

    double getDishAverageRating(@NotNull String dishID) {
        return Math.round(dishRatingRepository
                .findAllByDishDishId(dishID)//
                .map(DishRating::getRating)//
                .average().getOrElse(0.d) * 2) / 2.0;
    }
}
