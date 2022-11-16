package com.example.mealqr.services.mappers;

import com.example.mealqr.web.rest.reponse.CartItemRes;
import com.example.mealqr.web.rest.reponse.QRDataRes;
import io.vavr.collection.Seq;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QRDataResMapper {
    public static QRDataRes mapToQRDataRes(String userEmail, Seq<CartItemRes> customerCartItems, double cartCost) {
        return QRDataRes.of(userEmail, customerCartItems, cartCost);
    }
}
