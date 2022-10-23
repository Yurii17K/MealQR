package com.example.mealqr.services;

import com.example.mealqr.domain.CartItem;
import com.example.mealqr.domain.PromoCode;
import com.example.mealqr.repositories.CartItemRepository;
import com.example.mealqr.repositories.PromoCodeRepository;
import com.example.mealqr.rest.reponse.QRDataRes;

import io.vavr.collection.Seq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QRDataService {

    private final CartItemRepository cartItemRepository;
    private final PromoCodeRepository promoCodeRepository;

    public QRDataRes generateQRDataFromCustomerCart(@NotBlank String userEmail) {
        Seq<CartItem> customerCartItems = cartItemRepository.getCustomerCart(userEmail);
        return QRDataRes.of(
                userEmail,
                customerCartItems.map(cartItem -> QRDataRes.CartItemRes.of(
                                cartItem.getDish().getDishName(),//
                                cartItem.getCartItemCost(),//
                                cartItem.getDishQuantity())));
    }
    //TODO: Add tests
    public QRDataRes generateQRDataFromCustomerCart(@NotBlank String userEmail, @NotBlank String promocode) {
        Seq<CartItem> customerCartItems = cartItemRepository.getCustomerCart(userEmail);
        List<PromoCode> promoCodes = customerCartItems.toStream().map(CartItem::getDish).flatMap(promoCodeRepository::findByDish).filter(x->(!x.getUsed())).collect(Collectors.toList());
        Optional<PromoCode> existingCode = promoCodes.stream().filter(x->x.getPromoCodeString().compareTo(promocode)==0).findFirst();
        if(existingCode.isEmpty()){
            return generateQRDataFromCustomerCart(userEmail);
        }
        return QRDataRes.of(
                userEmail,
                customerCartItems.map(cartItem -> QRDataRes.CartItemRes.of(
                        cartItem.getDish().getDishName(),//
                        cartItem.getCartItemCost().subtract(existingCode.get().getPriceReduction()).max(BigDecimal.ZERO),
                        cartItem.getDishQuantity())),
                promocode);
    }
}
