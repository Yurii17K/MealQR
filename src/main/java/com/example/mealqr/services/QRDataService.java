package com.example.mealqr.services;

import com.example.mealqr.pojos.CartItem;
import com.example.mealqr.pojos.QRData;
import com.example.mealqr.repositories.CartItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QRDataService {

    private final CartItemRepository cartItemRepository;

    public QRDataService(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    public QRData generateQRDataFromCart(String customerMail) {
        List<CartItem> customerCartItemList = cartItemRepository.findAllByCustomerMail(customerMail);
//        cartItemRepository.deleteAllByCustomerMail(customerMail);

        return QRData.builder()
                .customerMail(customerMail)
                .dishes(customerCartItemList.stream().map(CartItem::getDishName).toArray(String[]::new))
                .dishQuantities(customerCartItemList.stream().mapToInt(CartItem::getDishQuantity).toArray())
                .itemPrices(customerCartItemList.stream().mapToDouble(CartItem::getCartItemCost).toArray())
                .build();
    }
}
