package com.example.mealqr.rest.request;

import lombok.Value;

import javax.validation.constraints.*;

@Value(staticConstructor = "of")
public class DishRatingReq {

    @NotBlank
    @Email(message = "Email should be valid", regexp = ".*@.*\\..*")
    String userEmail;

    @NotBlank
    String dishName;

    @NotBlank
    String restaurantName;

    @NotNull
    @Min(0)
    @Max(5)
    int rating;
}
