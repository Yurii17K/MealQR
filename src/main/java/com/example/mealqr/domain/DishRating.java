package com.example.mealqr.domain;

import com.example.mealqr.web.rest.request.DishRatingReq;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import java.util.UUID;

@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "DishRatings")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DishRating extends DishOpinion{

    @Setter
    int rating;

    public static DishRating of(String userEmail, DishRatingReq dishRatingReq, Dish dish) {
        return DishRating.builder()//
                .dishOpinionId(UUID.randomUUID().toString())//
                .rating(dishRatingReq.getRating())//
                .userEmail(userEmail)//
                .dish(Dish.builder().dishId(dish.getDishId()).build())//
                .build();
    }

    public DishRating withRating(int rating) {
        this.setRating(rating);
        return this;
    }
}
