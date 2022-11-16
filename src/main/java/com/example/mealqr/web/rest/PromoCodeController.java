package com.example.mealqr.web.rest;

import com.example.mealqr.exceptions.ApiException;
import com.example.mealqr.services.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PromoCodeController {

    private final CartItemService cartItemService;

    @PatchMapping("/promo/apply")
    public ResponseEntity<Boolean> applyPromoCode(Principal principal, @RequestParam @Valid String promoCode) {
        return cartItemService.registerPromoInSession(principal.getName(), promoCode)//
                .map(ResponseEntity::ok)//
                .getOrElseThrow(ApiException::new);
    }
}
