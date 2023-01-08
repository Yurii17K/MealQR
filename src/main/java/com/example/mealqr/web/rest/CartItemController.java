package com.example.mealqr.web.rest;

import com.example.mealqr.domain.Dish;
import com.example.mealqr.exceptions.ApiException;
import com.example.mealqr.services.CartItemService;
import com.example.mealqr.web.rest.reponse.CartItemRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CartItemController {

    private final CartItemService cartItemService;

    @GetMapping("/cart")
    @ApiResponse(responseCode = "200", description = "")
    @Operation(summary = "Gets customer's cart. Considers a promo code if applied", security = @SecurityRequirement(name = "JWT AUTH"))
    public ResponseEntity<List<CartItemRes>> getCustomerCart(Principal principal) {
        return ResponseEntity.ok(cartItemService.getCustomerCart(principal.getName()).asJava());
    }

    @GetMapping("/cart/cost")
    @ApiResponse(responseCode = "200", description = "")
    @Operation(summary = "Gets customer's cart cost. Considers a promo code if applied", security = @SecurityRequirement(name = "JWT AUTH"))
    public ResponseEntity<Double> getCustomerCartCost(Principal principal) {
        return ResponseEntity.ok(cartItemService.getCustomerCartCost(principal.getName()));
    }

    @PostMapping("/cart/add-dish")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = ""),
            @ApiResponse(responseCode = "404", description = "Dish doesn't exist in the restaurant")})
    @Operation(summary = "Add dish to the customer's cart", security = @SecurityRequirement(name = "JWT AUTH"))
    public ResponseEntity<Dish> addDishToCustomerCart(Principal principal, @RequestParam String dishId) {
        return cartItemService.addDishToCustomerCart(principal.getName(), dishId)//
                .map(ResponseEntity::ok)//
                .getOrElseThrow(ApiException::new);
    }

    @PutMapping("/cart/update-dish")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = ""),
            @ApiResponse(responseCode = "404", description = "Dish doesn't exist in the restaurant")})
    @Operation(summary = "Change quantity of the dish in the cart", security = @SecurityRequirement(name = "JWT AUTH"))
    public ResponseEntity<Boolean> alterDishQuantityInCustomerCart(Principal principal,
            @RequestParam String dishId, @RequestParam Integer quantity) {
        return cartItemService.alterDishQuantityInCustomerCart(principal.getName(), dishId, quantity)//
                .map(ResponseEntity::ok)//
                .getOrElseThrow(ApiException::new);
    }

    @DeleteMapping("/cart/clear-cart")
    @ApiResponse(responseCode = "200", description = "")
    @Operation(summary = "Clear the whole cart", security = @SecurityRequirement(name = "JWT AUTH"))
    public ResponseEntity<Boolean> clearCustomerCart(Principal principal) {
        return ResponseEntity.ok(cartItemService.clearCustomerCart(principal.getName()));
    }

    @DeleteMapping("/cart/clear-cart/{restaurantId}")
    @ApiResponse(responseCode = "200", description = "")
    @Operation(summary = "Clear dishes of the restaurant from the cart", security = @SecurityRequirement(name = "JWT AUTH"))
    public ResponseEntity<Boolean> clearCustomerCartByRestaurantId(Principal principal, @PathVariable String restaurantId) {
        return ResponseEntity.ok(cartItemService.clearCustomerCart(principal.getName(), restaurantId));
    }

    @DeleteMapping("/cart/delete-dish")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = ""),
            @ApiResponse(responseCode = "404", description = "Dish doesn't exist in the restaurant")})
    @Operation(summary = "deleteDishFromCustomerCart", security = @SecurityRequirement(name = "JWT AUTH"))
    public ResponseEntity<Boolean> deleteDishFromCustomerCart(Principal principal, @RequestParam String dishName,
            @RequestParam String restaurantId) {
        return cartItemService.deleteDishFromCustomerCart(principal.getName(), dishName, restaurantId)//
                .map(ResponseEntity::ok)//
                .getOrElseThrow(ApiException::new);
    }
}
