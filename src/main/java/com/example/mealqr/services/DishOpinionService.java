package com.example.mealqr.services;

import com.example.mealqr.pojos.*;
import com.example.mealqr.repositories.DishCommentRepository;
import com.example.mealqr.repositories.DishRatingRepository;
import com.example.mealqr.repositories.DishRepository;
import com.example.mealqr.repositories.UserRepository;
import com.example.mealqr.security.Roles;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DishOpinionService {

    private final DishCommentRepository dishCommentRepository;
    private final DishRatingRepository dishRatingRepository;
    private final DishRepository dishRepository;
    private UserRepository userRepository;

    public Map<Dish, Tuple2<Double, List<String>>> getAllDishesInRestaurantWithAverageRatingsAndComments(
            @NotBlank String restaurantName) {
        List<Dish> dishList = dishRepository.findAllByRestaurantName(restaurantName);

        return dishList.stream()
                .collect(Collectors.toMap(dish -> dish, dish -> Tuple.of(
                        getDishAverageRating(dish.getID()),
                        getDishComments(dish.getID())
                )));
    }

    public Tuple2<Boolean, String> addOrUpdateComment(@NotBlank String userEmail, @NotBlank String dishName,
                                                      @NotBlank String restaurantName, @NotBlank String comment) {
        Optional<Dish> optionalDish = dishRepository.findByDishNameAndRestaurantName(dishName, restaurantName);

        // might happen if restaurant employee removed a dish while this request was processing
        if (optionalDish.isEmpty()) {
            return Tuple.of(false, "Such dish does not exist");
        }

        Optional<DishComment> optionalDishComment =
                dishCommentRepository.findByDishIdAndUserEmail(optionalDish.get().getID(), userEmail);

        DishComment dishComment = DishComment.builder()
                .dishId(optionalDish.get().getID())
                .userEmail(userEmail)
                .comment(comment)
                .build();

        // if the comment is not present -> add it
        if (optionalDishComment.isEmpty()) {
            dishCommentRepository.save(dishComment);
        } else { // if the comment is present -> update it
            dishComment.setID(optionalDishComment.get().getID());
            dishCommentRepository.save(dishComment);
        }

        return Tuple.of(true, "Added comment to dish " + dishName + " from " + restaurantName);
    }

    public Tuple2<Boolean, String> addOrUpdateRating(@NotBlank String userEmail, @NotBlank String dishName,
                                                     @NotBlank String restaurantName, @NotNull Integer rating) {
        Optional<Dish> optionalDish = dishRepository.findByDishNameAndRestaurantName(dishName, restaurantName);

        // might happen if restaurant employee removed a dish while this request was processing
        if (optionalDish.isEmpty()) {
            return Tuple.of(false, "Such dish does not exist");
        }

        Optional<DishRating> optionalDishRating =
                dishRatingRepository.findByDishIdAndUserEmail(optionalDish.get().getID(), userEmail);

        DishRating dishRating = DishRating.builder()
                .dishId(optionalDish.get().getID())
                .userEmail(userEmail)
                .rating(rating)
                .build();

        // if the rating is not present -> add it
        if (optionalDishRating.isEmpty()) {
            dishRatingRepository.save(dishRating);
        } else { // if the rating is present -> update it
            dishRating.setID(optionalDishRating.get().getID());
            dishRatingRepository.save(dishRating);
        }

        return Tuple.of(true, "Added rating to dish " + dishName + " from " + restaurantName);
    }

    private List<String> getDishComments(@NotNull Integer dishID) {
        return dishCommentRepository
                .findAllByDishId(dishID)
                .stream()
                .map(DishComment::getComment)
                .collect(Collectors.toList());
    }

    private double getDishAverageRating(@NotNull Integer dishID) {
        return dishRatingRepository
                .findAllByDishId(dishID)
                .stream()
                .mapToInt(DishRating::getRating)
                .average().getAsDouble();
    }
}
