package com.example.mealqr.web.rest.reponse;

import lombok.Value;

import java.math.BigDecimal;

@Value(staticConstructor = "of")
public class CartItemRes {
    String cartItemId;
    String userEmail;
    DishRes dish;
    int dishQuantity;
    BigDecimal cartItemCost;
}
