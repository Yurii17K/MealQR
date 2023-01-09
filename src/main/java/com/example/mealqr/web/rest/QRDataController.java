package com.example.mealqr.web.rest;

import com.example.mealqr.security.CustomPrincipal;
import com.example.mealqr.services.QRDataService;
import com.example.mealqr.web.rest.reponse.QRDataRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class QRDataController {

    private final QRDataService qrDataService;

    @GetMapping("/generate-qr")
    @ApiResponse(responseCode = "200")
    @Operation(summary = "generateQRDataFromCustomerCart", security = @SecurityRequirement(name = "JWT AUTH"))
    public ResponseEntity<QRDataRes> generateQRDataFromCustomerCart(Authentication authentication) {
        return ResponseEntity.ok(qrDataService.generateQRDataFromCustomerCart((CustomPrincipal) authentication.getPrincipal()));
    }
}
