package com.example.mealqr.web.rest.reponse;

import lombok.Value;

import java.math.BigDecimal;

@Value(staticConstructor = "of")
public class DishRes {
    Integer dishId;
    String dishName;
    RestaurantRes restaurant;
    ImageRes dishImage;
    BigDecimal dishPrice;
    String dishDescription;
}
