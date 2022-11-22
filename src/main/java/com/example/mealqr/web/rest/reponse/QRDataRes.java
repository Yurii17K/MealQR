package com.example.mealqr.web.rest.reponse;

import lombok.Value;

import java.util.List;


@Value(staticConstructor = "of")
public class QRDataRes {
    String userEmail;
    List<CartItemRes> cartItemRes;
    double cartCost;
}
