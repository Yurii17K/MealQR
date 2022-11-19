package com.example.mealqr.web.rest.request;

import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
public class RestaurantSaveReq {

    @NotBlank
    String restaurantName;

    @NotBlank
    String restaurantCity;
}
