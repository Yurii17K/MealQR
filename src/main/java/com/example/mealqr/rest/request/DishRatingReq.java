package com.example.mealqr.rest.request;

import lombok.Value;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Value(staticConstructor = "of")
public class DishRatingReq {

    @NotBlank
    String dishName;

    @NotBlank
    String restaurantId;

    @NotNull
    @Min(0)
    @Max(5)
    int rating;
}
