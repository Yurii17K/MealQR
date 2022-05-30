package com.example.mealqr.services;

import com.example.mealqr.pojos.CartItem;
import com.example.mealqr.pojos.Dish;
import com.example.mealqr.pojos.QRData;
import com.example.mealqr.repositories.CartItemRepository;
import com.example.mealqr.repositories.DishRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Service
@AllArgsConstructor
public class QRDataService {

    private final CartItemRepository cartItemRepository;
    private final DishRepository dishRepository;

    public QRData generateQRDataFromCustomerCart(@NotBlank String userEmail) {
        List<CartItem> customerCartItemList = cartItemRepository.getCustomerCart(userEmail);

        return QRData.builder()
                .userEmail(userEmail)
                .dishes(customerCartItemList.stream().map(cartItem -> dishRepository.findByID(cartItem.getDishId()).orElse(new Dish()).getDishName()).toArray(String[]::new))
                .dishQuantities(customerCartItemList.stream().mapToInt(CartItem::getDishQuantity).toArray())
                .itemCosts(customerCartItemList.stream().mapToDouble(cartItem -> cartItem.getCartItemCost().doubleValue()).toArray())
                .build();
    }
}
