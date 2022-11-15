package com.example.mealqr.domain;

import com.example.mealqr.rest.request.DishRatingReq;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;

@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "DishRatings")
@With
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DishRating extends DishOpinion{
    int rating;

    public static DishRating of(String userEmail, DishRatingReq dishRatingReq, Dish dish) {
        return DishRating.builder()//
                .rating(dishRatingReq.getRating())//
                .userEmail(userEmail)//
                .dish(Dish.builder().dishId(dish.getDishId()).build())//
                .build();
    }
}
