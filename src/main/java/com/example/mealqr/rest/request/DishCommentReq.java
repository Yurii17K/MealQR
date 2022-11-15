package com.example.mealqr.rest.request;

import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Value(staticConstructor = "of")
public class DishCommentReq {

    @NotBlank
    String dishName;

    @NotBlank
    String restaurantId;

    @NotBlank
    @Size(max = 1000)
    String comment;
}
