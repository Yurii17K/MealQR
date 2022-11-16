package com.example.mealqr.services.mappers;

import com.example.mealqr.domain.CartItem;
import com.example.mealqr.rest.reponse.QRDataRes;
import io.vavr.collection.Seq;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QRDataResMapper {
    public static QRDataRes mapToQRDataRes(String userEmail, Seq<CartItem> customerCartItems, double cartCost) {
        return QRDataRes.of(
                userEmail,
                customerCartItems.map(cartItem -> QRDataRes.CartItemRes.of(
                        cartItem.getDish().getDishName(),
                        cartItem.getCartItemCost(),
                        cartItem.getDishQuantity())),
                cartCost);
    }
}
