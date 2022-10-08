package com.example.mealqr.services;

import com.example.mealqr.domain.CartItem;
import com.example.mealqr.repositories.CartItemRepository;
import com.example.mealqr.rest.reponse.QRDataRes;
import io.vavr.collection.Seq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;

@Service
@RequiredArgsConstructor
public class QRDataService {

    private final CartItemRepository cartItemRepository;

    public QRDataRes generateQRDataFromCustomerCart(@NotBlank String userEmail) {
        Seq<CartItem> customerCartItems = cartItemRepository.getCustomerCart(userEmail);
        return QRDataRes.of(
                userEmail,//
                customerCartItems.map(cartItem -> QRDataRes.CartItemRes.of(
                                cartItem.getDish().getDishName(),//
                                cartItem.getCartItemCost(),//
                                cartItem.getDishQuantity())));
    }
}
