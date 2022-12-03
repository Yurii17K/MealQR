package com.example.mealqr.web.rest;

import com.example.mealqr.domain.enums.Roles;
import com.example.mealqr.services.RestaurantService;
import com.example.mealqr.web.rest.reponse.RestaurantRes;
import com.example.mealqr.web.rest.request.RestaurantSaveReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    public ResponseEntity<List<String>> createRestaurant(Authentication authentication) {
        Set<SimpleGrantedAuthority> authoritiesToRemove = Arrays.stream(Roles.values())//
                .map(Enum::name)//
                .map(SimpleGrantedAuthority::new)//
                .collect(Collectors.toSet());
        authoritiesToRemove.add(new SimpleGrantedAuthority(authentication.getName()));
        return ResponseEntity.ok(authentication.getAuthorities()//
                .stream()//
                .filter(grantedAuthority -> !authoritiesToRemove.contains(grantedAuthority))//
                .map(GrantedAuthority::getAuthority)//
                .collect(Collectors.toList()));
    }
}
