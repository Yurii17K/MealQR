package com.example.mealqr.web.rest.request;

import lombok.Value;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Value(staticConstructor = "of")
public class DishRatingReq {

    @NotBlank
    String dishId;

    @Min(0)
    @Max(5)
    int rating;
}
