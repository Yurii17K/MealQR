package com.example.mealqr.web.rest.request;

import lombok.Value;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Value(staticConstructor = "of")
public class DishRatingReq {

    @NotNull
    Integer dishId;

    @NotNull
    @Min(0)
    @Max(5)
    int rating;
}
