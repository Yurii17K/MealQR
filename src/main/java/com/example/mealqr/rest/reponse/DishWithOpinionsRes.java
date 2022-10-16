package com.example.mealqr.rest.reponse;

import com.example.mealqr.rest.dto.ImageDto;
import io.vavr.collection.Seq;
import lombok.Value;

@Value(staticConstructor = "of")
public class DishWithOpinionsRes {

    String dishId;
    String dishName;
    String restaurantName;
    ImageDto dishImage;
    double dishPrice;
    String dishDescription;

    double dishAverageRating;
    Seq<DishCommentDto> dishCommentDto;

    @Value(staticConstructor = "of")
    public static class DishCommentDto {
        String userEmail;
        String comment;
    }
}
