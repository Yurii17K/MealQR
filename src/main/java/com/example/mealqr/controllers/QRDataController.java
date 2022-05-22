package com.example.mealqr.controllers;

import com.example.mealqr.pojos.QRData;
import com.example.mealqr.services.QRDataService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/qr")
@AllArgsConstructor
public class QRDataController {

    private final QRDataService qrDataService;

    @PreAuthorize("hasAuthority(#userEmail)")
    @GetMapping
    public ResponseEntity<QRData> generateQRDataFromCustomerCart(
            @RequestParam String userEmail
    ) {
        return ResponseEntity.ok()
                .body(qrDataService.generateQRDataFromCustomerCart(userEmail));
    }
}
