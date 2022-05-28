package com.example.mealqr.controllers;

import com.example.mealqr.pojos.CartItem;
import com.example.mealqr.services.CartItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.vavr.Tuple2;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart-items")
@AllArgsConstructor
public class CartItemController {

    private final CartItemService cartItemService;

    @PreAuthorize("hasAuthority(#userEmail)")
    @GetMapping
    @Operation(summary = "getCustomerCart", security = @SecurityRequirement(name = "JWT AUTH"))
    public ResponseEntity<List<CartItem>> getCustomerCart(
            @RequestParam String userEmail
    ) {
        return ResponseEntity.ok()
                .body(cartItemService.getCustomerCart(userEmail));
    }

    @PreAuthorize("hasAuthority(#userEmail)")
    @GetMapping(value = "/cost")
    @Operation(summary = "getCustomerCartCost", security = @SecurityRequirement(name = "JWT AUTH"))
    public ResponseEntity<Double> getCustomerCartCost(
            @RequestParam String userEmail
    ) {
        return ResponseEntity.ok()
                .body(cartItemService.getCustomerCartCost(userEmail));
    }

    @PreAuthorize("hasAuthority(#userEmail)")
    @PostMapping
    @Operation(summary = "addDishToCustomerCart", security = @SecurityRequirement(name = "JWT AUTH"))
    public ResponseEntity<Tuple2<Boolean, String>> addDishToCustomerCart(
            @RequestParam String userEmail,
            @RequestParam String dishName,
            @RequestParam String restaurantName
    ) {
        return ResponseEntity.ok()
                .body(cartItemService.addDishToCustomerCart(userEmail, dishName, restaurantName));
    }

    @PreAuthorize("hasAuthority(#userEmail)")
    @PatchMapping
    @Operation(summary = "changeDishQuantityInCustomerCart", security = @SecurityRequirement(name = "JWT AUTH"))
    public ResponseEntity<Tuple2<Boolean, String>> changeDishQuantityInCustomerCart(
            @RequestParam String userEmail,
            @RequestParam String dishName,
            @RequestParam String restaurantName,
            @RequestParam Integer quantity
    ) {
        return ResponseEntity.ok()
                .body(cartItemService.changeDishQuantityInCustomerCart(userEmail, dishName, restaurantName, quantity));
    }

    @PreAuthorize("hasAuthority(#userEmail)")
    @DeleteMapping("/clear")
    @Operation(summary = "clearCustomerCart", security = @SecurityRequirement(name = "JWT AUTH"))
    public ResponseEntity<Tuple2<Boolean, String>> clearCustomerCart(
            @RequestParam String userEmail
    ) {
        return ResponseEntity.ok()
                .body(cartItemService.clearCustomerCart(userEmail));
    }

    @PreAuthorize("hasAuthority(#userEmail)")
    @DeleteMapping
    @Operation(summary = "deleteDishFromCustomerCart", security = @SecurityRequirement(name = "JWT AUTH"))
    public ResponseEntity<Tuple2<Boolean, String>> deleteDishFromCustomerCart(
            @RequestParam String userEmail,
            @RequestParam String dishName,
            @RequestParam String restaurantName
    ) {
        return ResponseEntity.ok()
                .body(cartItemService.deleteDishFromCustomerCart(userEmail, dishName, restaurantName));
    }
}
