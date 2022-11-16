package com.example.mealqr.services;

import com.example.mealqr.domain.CartItem;
import com.example.mealqr.rest.reponse.QRDataRes;
import com.example.mealqr.services.mappers.QRDataResMapper;
import io.vavr.collection.Seq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;

@Service
@RequiredArgsConstructor
public class QRDataService {

    private final CartItemService cartItemService;

    public QRDataRes generateQRDataFromCustomerCart(@NotBlank String userEmail) {
        Seq<CartItem> customerCartItems = cartItemService.getCustomerCart(userEmail);
        double customerCartCost = cartItemService.getCustomerCartCost(userEmail);
        return QRDataResMapper.mapToQRDataRes(userEmail, customerCartItems, customerCartCost);
    }
}
