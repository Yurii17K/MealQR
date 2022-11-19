package com.example.mealqr.web.rest.reponse;

import lombok.Value;

import java.util.List;

@Value(staticConstructor = "of")
public class DishWithOpinionsRes {

    String dishId;
    String dishName;
    RestaurantRes restaurant;
    ImageDto dishImage;
    double dishPrice;
    String dishDescription;

    double dishAverageRating;
    List<DishCommentRes> dishComments;

    @Value(staticConstructor = "of")
    public static class DishCommentRes {
        String userEmail;
        String comment;
    }
}
