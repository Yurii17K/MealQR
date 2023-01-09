package com.example.mealqr.web.rest;

import com.example.mealqr.exceptions.ApiException;
import com.example.mealqr.security.CustomPrincipal;
import com.example.mealqr.services.CartItemService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Validated
public class PromoCodeController {

    private final CartItemService cartItemService;

    @PostMapping("/promo/apply/{promoCode}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "JWT token with a promo code attached to it"),
            @ApiResponse(responseCode = "404", description = "Promo code doesn't exist"),
            @ApiResponse(responseCode = "400", description = "Promo code exceeded the amount of uses")})
    public ResponseEntity<String> registerPromoInSession(Authentication authentication, @PathVariable @Valid String promoCode) {
        return cartItemService.registerPromoInSession((CustomPrincipal) authentication.getPrincipal(), promoCode)//
                .map(ResponseEntity::ok)//
                .getOrElseThrow(ApiException::new);
    }
}
