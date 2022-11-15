package com.example.mealqr.rest;

import com.example.mealqr.domain.CartItem;
import com.example.mealqr.domain.Dish;
import com.example.mealqr.services.CartItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.vavr.collection.Seq;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CartItemController {

    private final CartItemService cartItemService;

    @GetMapping("/cart")
    @Operation(summary = "getCustomerCart", security = @SecurityRequirement(name = "JWT AUTH"))
    public ResponseEntity<Seq<CartItem>> getCustomerCart(Principal principal) {
        return ResponseEntity.ok(cartItemService.getCustomerCart(principal.getName()));
    }

    @GetMapping("/cart/cost")
    @Operation(summary = "getCustomerCartCost", security = @SecurityRequirement(name = "JWT AUTH"))
    public ResponseEntity<Double> getCustomerCartCost(Principal principal) {
        return ResponseEntity.ok(cartItemService.getCustomerCartCost(principal.getName()));
    }

    @PostMapping("/cart/add-dish")
    @Operation(summary = "addDishToCustomerCart", security = @SecurityRequirement(name = "JWT AUTH"))
    public ResponseEntity<Dish> addDishToCustomerCart(Principal principal, @RequestParam String dishName,
            @RequestParam String restaurantId) {
        return cartItemService.addDishToCustomerCart(principal.getName(), dishName, restaurantId)
                .map(ResponseEntity::ok)//
                .getOrElseThrow(s -> new RuntimeException(s));
    }

    @PatchMapping("/cart/update-dish")
    @Operation(summary = "changeDishQuantityInCustomerCart", security = @SecurityRequirement(name = "JWT AUTH"))
    public ResponseEntity<Boolean> changeDishQuantityInCustomerCart(Principal principal,
            @RequestParam String dishName, @RequestParam String restaurantId, @RequestParam Integer quantity) {
        return cartItemService.changeDishQuantityInCustomerCart(principal.getName(), dishName, restaurantId, quantity)//
                .map(ResponseEntity::ok)//
                .getOrElseThrow(s -> new RuntimeException(s));
    }

    @DeleteMapping("/cart/clear-cart")
    @Operation(summary = "clearCustomerCart", security = @SecurityRequirement(name = "JWT AUTH"))
    public ResponseEntity<Boolean> clearCustomerCart(Principal principal) {
        return ResponseEntity.ok(cartItemService.clearCustomerCart(principal.getName()));
    }

    @DeleteMapping("/cart/delete-dish")
    @Operation(summary = "deleteDishFromCustomerCart", security = @SecurityRequirement(name = "JWT AUTH"))
    public ResponseEntity<Boolean> deleteDishFromCustomerCart(Principal principal, @RequestParam String dishName,
            @RequestParam String restaurantId) {
        return cartItemService.deleteDishFromCustomerCart(principal.getName(), dishName, restaurantId)//
                .map(ResponseEntity::ok)//
                .getOrElseThrow(s -> new RuntimeException(s));
    }
}
