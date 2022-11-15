package com.example.mealqr.rest;

import com.example.mealqr.rest.reponse.QRDataRes;
import com.example.mealqr.services.QRDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    @Operation(summary = "generateQRDataFromCustomerCart", security = @SecurityRequirement(name = "JWT AUTH"))
    public ResponseEntity<QRDataRes> generateQRDataFromCustomerCart(Principal principal) {
        return ResponseEntity.ok(qrDataService.generateQRDataFromCustomerCart(principal.getName()));
    }
}
