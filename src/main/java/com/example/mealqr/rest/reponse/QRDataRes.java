package com.example.mealqr.rest.reponse;

import io.vavr.collection.Seq;
import lombok.Value;

import java.math.BigDecimal;


@Value(staticConstructor = "of")
public class QRDataRes {

    String userEmail;
    Seq<CartItemRes> cartItemRes;
    double sum;

    public static QRDataRes of(String userEmail, Seq<CartItemRes> cartItemRes) {
        return QRDataRes.of(
                userEmail,
                cartItemRes,
                cartItemRes.map(CartItemRes::getCartItemCost).sum().doubleValue());
    }

    @Value(staticConstructor = "of")
    public static class CartItemRes {
        String dishName;
        BigDecimal cartItemCost;
        int dishQuantity;
    }
}
