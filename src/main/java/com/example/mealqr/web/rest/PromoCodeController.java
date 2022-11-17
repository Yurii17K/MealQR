package com.example.mealqr.web.rest;

import com.example.mealqr.exceptions.ApiException;
import com.example.mealqr.services.CartItemService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Validated
public class PromoCodeController {

    private final CartItemService cartItemService;

    @PatchMapping("/promo/apply/{promoCode}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = ""),
            @ApiResponse(responseCode = "404", description = "Promo code doesn't exist"),
            @ApiResponse(responseCode = "400", description = "Promo code exceeded the amount of uses")})
    public ResponseEntity<Boolean> registerPromoInSession(Principal principal, @PathVariable @Valid String promoCode) {
        return cartItemService.registerPromoInSession(principal.getName(), promoCode)//
                .map(ResponseEntity::ok)//
                .getOrElseThrow(ApiException::new);
    }
}
