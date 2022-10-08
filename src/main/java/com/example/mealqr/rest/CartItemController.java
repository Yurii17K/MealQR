package com.example.mealqr.rest;

import com.example.mealqr.domain.CartItem;
import com.example.mealqr.domain.Dish;
import com.example.mealqr.services.CartItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.vavr.collection.Seq;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CartItemController {

    private final CartItemService cartItemService;

    @PreAuthorize("hasAuthority({#userEmail})")
    @GetMapping("/cart")
    @Operation(summary = "getCustomerCart", security = @SecurityRequirement(name = "JWT AUTH"))
    public ResponseEntity<Seq<CartItem>> getCustomerCart(@RequestParam String userEmail) {
        return ResponseEntity.ok(cartItemService.getCustomerCart(userEmail));
    }

    @PreAuthorize("hasAuthority({#userEmail})")
    @GetMapping("/cart/cost")
    @Operation(summary = "getCustomerCartCost", security = @SecurityRequirement(name = "JWT AUTH"))
    public ResponseEntity<Double> getCustomerCartCost(@RequestParam String userEmail) {
        return ResponseEntity.ok(cartItemService.getCustomerCartCost(userEmail));
    }

    @PreAuthorize("hasAuthority({#userEmail})")
    @PostMapping("/cart/add-dish")
    @Operation(summary = "addDishToCustomerCart", security = @SecurityRequirement(name = "JWT AUTH"))
    public ResponseEntity<Dish> addDishToCustomerCart(@RequestParam String userEmail, @RequestParam String dishName,
            @RequestParam String restaurantName) {
        return cartItemService.addDishToCustomerCart(userEmail, dishName, restaurantName)
                .map(ResponseEntity::ok)//
                .getOrElseThrow(s -> new RuntimeException(s));
    }

    @PreAuthorize("hasAuthority({#userEmail})")
    @PatchMapping("/cart/update-dish")
    @Operation(summary = "changeDishQuantityInCustomerCart", security = @SecurityRequirement(name = "JWT AUTH"))
    public ResponseEntity<Boolean> changeDishQuantityInCustomerCart(@RequestParam String userEmail,
            @RequestParam String dishName, @RequestParam String restaurantName, @RequestParam Integer quantity) {
        return cartItemService.changeDishQuantityInCustomerCart(userEmail, dishName, restaurantName, quantity)//
                .map(ResponseEntity::ok)//
                .getOrElseThrow(s -> new RuntimeException(s));
    }

    @PreAuthorize("hasAuthority({#userEmail})")
    @DeleteMapping("/cart/clear-cart")
    @Operation(summary = "clearCustomerCart", security = @SecurityRequirement(name = "JWT AUTH"))
    public ResponseEntity<Boolean> clearCustomerCart(@RequestParam String userEmail) {
        return ResponseEntity.ok(cartItemService.clearCustomerCart(userEmail));
    }

    @PreAuthorize("hasAuthority({#userEmail})")
    @DeleteMapping("/cart/delete-dish")
    @Operation(summary = "deleteDishFromCustomerCart", security = @SecurityRequirement(name = "JWT AUTH"))
    public ResponseEntity<Boolean> deleteDishFromCustomerCart(@RequestParam String userEmail, @RequestParam String dishName,
            @RequestParam String restaurantName) {
        return cartItemService.deleteDishFromCustomerCart(userEmail, dishName, restaurantName)//
                .map(ResponseEntity::ok)//
                .getOrElseThrow(s -> new RuntimeException(s));
    }
}
