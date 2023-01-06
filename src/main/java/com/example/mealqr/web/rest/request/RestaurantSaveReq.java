package com.example.mealqr.web.rest.request;

import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
public class RestaurantSaveReq {

    @NotBlank(message = "Restaurant name should not be empty")
    String restaurantName;

    @NotBlank(message = "Restaurant city should not be empty")
    String restaurantCity;
}
