package com.example.mealqr.web.rest.reponse;

import lombok.Value;

@Value(staticConstructor = "of")
public class RestaurantRes {
    String restaurantId;
    String restaurantName;
    String restaurantCity;
    UserRes restaurantManager;
}
