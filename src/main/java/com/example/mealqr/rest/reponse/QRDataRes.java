package com.example.mealqr.rest.reponse;

import io.vavr.collection.Seq;
import lombok.Value;

import java.math.BigDecimal;


@Value(staticConstructor = "of")
public class QRDataRes {

    String userEmail;
    Seq<CartItemRes> cartItemRes;
    double cartCost;

    @Value(staticConstructor = "of")
    public static class CartItemRes {
        String dishName;
        BigDecimal cartItemCost;
        int dishQuantity;
    }
}
