package com.example.mealqr.web.rest.reponse;

import io.vavr.collection.Seq;
import lombok.Value;


@Value(staticConstructor = "of")
public class QRDataRes {
    String userEmail;
    Seq<CartItemRes> cartItemRes;
    double cartCost;
}
