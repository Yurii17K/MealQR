package com.example.mealqr.services;

import com.example.mealqr.services.mappers.QRDataResMapper;
import com.example.mealqr.web.rest.reponse.CartItemRes;
import com.example.mealqr.web.rest.reponse.QRDataRes;
import io.vavr.collection.Seq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;

@Service
@RequiredArgsConstructor
public class QRDataService {

    private final CartItemService cartItemService;

    public QRDataRes generateQRDataFromCustomerCart(@NotBlank String userEmail) {
        Seq<CartItemRes> customerCartItems = cartItemService.getCustomerCart(userEmail);
        double customerCartCost = cartItemService.getCustomerCartCost(userEmail);
        return QRDataResMapper.mapToQRDataRes(userEmail, customerCartItems, customerCartCost);
    }
}
