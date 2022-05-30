package com.example.mealqr.controllers;

import com.example.mealqr.pojos.Dish;
import com.example.mealqr.services.DishOpinionService;
import com.example.mealqr.services.DishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.vavr.Tuple2;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/dishes")
@AllArgsConstructor
public class DishController {

    private final DishService dishService;
    private final DishOpinionService dishOpinionService;

    @GetMapping
    public ResponseEntity<List<Dish>> getAllDishesInRestaurant(
            @RequestParam String restaurantName
    ) {
        return ResponseEntity.ok()
                .body(dishService.getAllDishesInRestaurant(restaurantName));
    }

    @PreAuthorize("hasAuthority(#userEmail)")
    @GetMapping("/preferences")
    @Operation(summary = "getAllDishesInRestaurantSortedByUserPreference", security = @SecurityRequirement(name = "JWT AUTH"))
    public ResponseEntity<List<Tuple2<Dish, Tuple2<Double, List<String>>>>> getAllDishesInRestaurantSortedByUserPreference(
            @RequestParam String userEmail,
            @RequestParam String restaurantName
    ) {
        return ResponseEntity.ok()
                .body(dishOpinionService.getAllDishesInRestaurantConfiguredForUser(userEmail, restaurantName));
    }

    @GetMapping("/random")
    public ResponseEntity<Dish> getRandomDish() {
        return ResponseEntity.ok()
                .body(dishService.getRandomDish());
    }

    @GetMapping("/random/{restaurantName}")
    public ResponseEntity<Dish> getRandomDish(
            @PathVariable("restaurantName") String restaurantName
    ) {
        return ResponseEntity.ok()
                .body(dishService.getRandomDishFromRestaurantOffer(restaurantName));
    }

    @PreAuthorize("hasAuthority({#restaurantName})")
    @PostMapping("/restaurant")
    @Operation(summary = "addDishToRestaurantOffer", security = @SecurityRequirement(name = "JWT AUTH"))
    public ResponseEntity<Tuple2<Boolean, String>> addDishToRestaurantOffer(
            @RequestParam String dishName,
            @RequestParam String restaurantName,
            @RequestBody String dishImgFile,
            @RequestParam BigDecimal dishPrice,
            @RequestParam String dishDescription
    ) {
        Dish dishToAdd = Dish.builder()
                .dishName(dishName)
                .restaurantName(restaurantName)
                .dishImg(dishImgFile.getBytes())
                .dishPrice(dishPrice)
                .dishDescription(dishDescription)
                .build();

        return ResponseEntity.ok()
                .body(dishService.addDishToRestaurantOffer(dishToAdd));
    }

    @PreAuthorize("hasAuthority({#restaurantName})")
    @PutMapping("/restaurant")
    @Operation(summary = "updateDishInRestaurantOffer", security = @SecurityRequirement(name = "JWT AUTH"))
    public ResponseEntity<Tuple2<Boolean, String>> updateDishInRestaurantOffer(
            @RequestParam String dishName,
            @RequestParam String restaurantName,
            @RequestBody String dishImgFile,
            @RequestParam BigDecimal dishPrice,
            @RequestParam String dishDescription
    ) {
        Dish dishWithNewData = Dish.builder()
                .dishName(dishName)
                .restaurantName(restaurantName)
                .dishImg(dishImgFile.getBytes())
                .dishPrice(dishPrice)
                .dishDescription(dishDescription)
                .build();

        return ResponseEntity.ok()
                .body(dishService.updateDishInRestaurantOffer(dishWithNewData));
    }

    @PreAuthorize("hasAuthority({#restaurantName})")
    @DeleteMapping("/restaurant")
    @Operation(summary = "removeDishFromRestaurantOffer", security = @SecurityRequirement(name = "JWT AUTH"))
    public ResponseEntity<Tuple2<Boolean, String>> removeDishFromRestaurantOffer(
            @RequestParam String dishName,
            @RequestParam String restaurantName
    ) {
        return ResponseEntity.ok()
                .body(dishService.removeDishFromRestaurantOffer(dishName, restaurantName));
    }




}
