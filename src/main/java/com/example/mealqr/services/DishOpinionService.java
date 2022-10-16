package com.example.mealqr.services;

import com.example.mealqr.domain.Dish;
import com.example.mealqr.domain.DishComment;
import com.example.mealqr.domain.DishRating;
import com.example.mealqr.repositories.DishCommentRepository;
import com.example.mealqr.repositories.DishRatingRepository;
import com.example.mealqr.repositories.DishRepository;
import com.example.mealqr.repositories.UserRepository;
import com.example.mealqr.rest.request.DishCommentReq;
import com.example.mealqr.rest.request.DishRatingReq;
import io.vavr.collection.Seq;
import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
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

    public Either<String, DishRating> addOrUpdateRating(DishRatingReq dishRatingReq) {
        return dishRepository.findByDishNameAndRestaurantName(dishRatingReq.getDishName(), dishRatingReq.getRestaurantName())//
                .map(dish -> dishRatingRepository.findByDishDishIdAndUserEmail(dish.getDishId(), dishRatingReq.getUserEmail())//
                        .peek(dishRating -> dishRatingRepository.save(dishRating.withRating(dishRatingReq.getRating())))//
                        .getOrElse(() -> dishRatingRepository.save(DishRating.builder()//
                                .rating(dishRatingReq.getRating())//
                                .userEmail(dishRatingReq.getUserEmail())//
                                .dish(Dish.builder().dishId(dish.getDishId()).build())//
                                .build())))//
                .toEither(SUCH_DISH_DOES_NOT_EXIST);
    }

    public Either<String, DishComment> addOrUpdateComment(DishCommentReq dishCommentReq) {
        return dishRepository.findByDishNameAndRestaurantName(dishCommentReq.getDishName(), dishCommentReq.getRestaurantName())//
                .map(dish -> dishCommentRepository.findByDishDishIdAndUserEmail(dish.getDishId(), dishCommentReq.getUserEmail())//
                        .peek(dishComment -> dishCommentRepository.save(dishComment.withComment(dishCommentReq.getComment())))//
                        .getOrElse(() -> dishCommentRepository.save(DishComment.builder()//
                                .comment(CurseLanguage.filterBadLanguage(dishCommentReq.getComment()))//
                                .userEmail(dishCommentReq.getUserEmail())//
                                .dish(Dish.builder().dishId(dish.getDishId()).build())//
                                .build())))//
                .toEither(SUCH_DISH_DOES_NOT_EXIST);
    }

    Map<String, Map<Integer, Double>> getDataForPreferenceAnalysis(@NotBlank String restaurantName) {
        Seq<Integer> dishIdsByRestaurantName = dishRepository.findAllByRestaurantName(restaurantName)//
                .map(Dish::getDishId)//
                .sorted();
        // take only the customers that rated sth in the restaurant
        Seq<String> customerEmails = userRepository.findAllCustomersWhoRatedSomethingInRestaurant(restaurantName)
                .distinct();
        return customerEmails.toMap(Function.identity(),
                        email -> {
                            // create a map of dishId:rating
                            Map<Integer, Double> dishIdsWithRatingsByEmailAndRestaurantName = dishRatingRepository
                                    .findAllByUserEmailAndRestaurantName(email, restaurantName)//
                                    .toMap(dishRating -> dishRating.getDish().getDishId(),d -> (double) d.getRating())
                                    .toJavaMap();
                            // generate random ratings for all unrated dishes
                            if (dishIdsByRestaurantName.size() != dishIdsWithRatingsByEmailAndRestaurantName.size()) {
                                for (int i = dishIdsWithRatingsByEmailAndRestaurantName.size(); i < dishIdsByRestaurantName.size(); i++) {
                                    dishIdsWithRatingsByEmailAndRestaurantName.put(dishIdsByRestaurantName.get(i), Math.random() * 5);
                                }
                            }
                            return dishIdsWithRatingsByEmailAndRestaurantName;
                        }).toJavaMap();
    }

    Seq<DishComment> getDishComments(@NotNull Integer dishID) {
        return dishCommentRepository.findAllByDishDishId(dishID);
    }

    double getDishAverageRating(@NotNull Integer dishID) {
        return Math.round(dishRatingRepository
                .findAllByDishDishId(dishID)//
                .map(DishRating::getRating)//
                .average().getOrElse(0.d) * 2) / 2.0;
    }
}
