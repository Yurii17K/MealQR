package com.example.mealqr.web.rest;

import com.example.mealqr.security.CustomPrincipal;
import com.example.mealqr.services.RestaurantService;
import com.example.mealqr.web.rest.reponse.RestaurantRes;
import com.example.mealqr.web.rest.request.RestaurantSaveReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.security.Principal;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    @PostMapping("/restaurant")
    @Operation(summary = "Create a new restaurant", security = @SecurityRequirement(name = "JWT AUTH"))
    public ResponseEntity<RestaurantRes> createRestaurant(Principal principal,
            @RequestBody RestaurantSaveReq restaurantSaveReq) {
        return ResponseEntity.ok(restaurantService.createRestaurant(principal.getName(), restaurantSaveReq));
    }

    @GetMapping("/restaurants")
    @Operation(summary = "Get all restaurants that belong to the current user", security = @SecurityRequirement(name = "JWT AUTH"))
    public ResponseEntity<List<RestaurantRes>> getRestaurants(Authentication authentication) {
        CustomPrincipal customPrincipal = (CustomPrincipal) authentication.getPrincipal();
        return ResponseEntity.ok(restaurantService.getRestaurants(customPrincipal.getRestaurantIds()));
    }

    @GetMapping("/restaurantsByCity")
    @Operation(summary = "Get all restaurants given a city")
    public ResponseEntity<List<RestaurantRes>> getRestaurantsByCity( @Valid @NotBlank @RequestParam("restaurantCity") String city) {
        return ResponseEntity.ok(restaurantService.getRestaurantsByCity(city));
    }

    @GetMapping("/restaurant-cities")
    @Operation(summary = "Get all cities with registered restaurants")
    public ResponseEntity<Set<String>> getRestaurantCities() {
        return ResponseEntity.ok(restaurantService.getRestaurantCities());
    }
}
