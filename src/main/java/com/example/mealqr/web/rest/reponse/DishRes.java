package com.example.mealqr.web.rest.reponse;

import lombok.Value;

import java.math.BigDecimal;

@Value(staticConstructor = "of")
public class DishRes {
    String dishId;
    String dishName;
    RestaurantRes restaurant;
    ImageDto dishImage;
    BigDecimal dishPrice;
    String dishDescription;
}
