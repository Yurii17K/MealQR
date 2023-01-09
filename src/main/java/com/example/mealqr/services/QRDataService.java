package com.example.mealqr.services;

import com.example.mealqr.domain.PromoCode;
import com.example.mealqr.repositories.PromoCodeRepository;
import com.example.mealqr.security.CustomPrincipal;
import com.example.mealqr.services.mappers.QRDataResMapper;
import com.example.mealqr.web.rest.reponse.CartItemRes;
import com.example.mealqr.web.rest.reponse.QRDataRes;
import io.vavr.collection.Seq;
import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import static com.example.mealqr.services.CartItemService.PROMOCODE;

@Service
@RequiredArgsConstructor
public class QRDataService {

    private final CartItemService cartItemService;
    private final PromoCodeRepository promoCodeRepository;

    public QRDataRes generateQRDataFromCustomerCart(CustomPrincipal customPrincipal) {
        Seq<CartItemRes> customerCartItems = cartItemService.getCustomerCart(customPrincipal);
        double customerCartCost = customerCartItems.map(CartItemRes::getCartItemCost).sum().doubleValue();
        updatePromoCode();
        return QRDataResMapper.mapToQRDataRes(customPrincipal.getUsername(), customerCartItems, customerCartCost);
    }

    private void updatePromoCode() {
        Option.of(RequestContextHolder.currentRequestAttributes().getAttribute(PROMOCODE, RequestAttributes.SCOPE_SESSION))
                .map(PromoCode.class::cast)//
                .peek(promo -> promoCodeRepository.save(promo.withUsesLeft(promo.getUsesLeft() - 1)));
    }
}
