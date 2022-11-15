package com.example.mealqr.rest.reponse;

import com.example.mealqr.domain.Restaurant;
import com.example.mealqr.rest.dto.ImageDto;
import lombok.Value;

import java.math.BigDecimal;

@Value(staticConstructor = "of")
public class DishRes {

    Integer dishId;
    String dishName;
    Restaurant restaurant;
    ImageDto dishImage;
    BigDecimal dishPrice;
    String dishDescription;
}
