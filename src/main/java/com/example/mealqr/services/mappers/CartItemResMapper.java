package com.example.mealqr.services.mappers;

import com.example.mealqr.domain.CartItem;
import com.example.mealqr.web.rest.reponse.CartItemRes;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CartItemResMapper {

    public static CartItemRes mapToCartItemRes(CartItem cartItem) {
        return CartItemRes.of(
                cartItem.getCartItemId().toString(),
                cartItem.getUserEmail(),
                DishResMapper.mapToDishRes(cartItem.getDish()),
                cartItem.getDishQuantity(),
                cartItem.getCartItemCost()
        );
    }
}
