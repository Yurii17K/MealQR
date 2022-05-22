package com.example.mealqr.controllers;

import com.example.mealqr.pojos.Dish;
import com.example.mealqr.security.Roles;
import com.example.mealqr.services.DishService;
import io.vavr.Tuple2;
import io.vavr.Tuple3;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/dishes")
@AllArgsConstructor
public class DishController {

    private final DishService dishService;

    @GetMapping
    public ResponseEntity<List<Dish>> getAllDishesInRestaurant(
            @RequestParam String restaurantName
    ) {
        return ResponseEntity.ok()
                .body(dishService.getAllDishesInRestaurant(restaurantName));
    }

    @GetMapping("/opinions")
    public ResponseEntity<Tuple3<List<Dish>, List<Double>, List<List<String>>>> getAllDishesInRestaurantWithAverageRatingsAndComments(
            @RequestParam String restaurantName
    ) {
        return ResponseEntity.ok()
                .body(dishService.getAllDishesInRestaurantWithAverageRatingsAndComments(restaurantName));
    }

    @GetMapping("/random")
    public ResponseEntity<Dish> getRandomDish() {
        return ResponseEntity.ok()
                .body(dishService.getRandomDish());
    }

    @GetMapping("/restaurant/random")
    public ResponseEntity<Dish> getRandomDish(
            @RequestParam String restaurantName
    ) {
        return ResponseEntity.ok()
                .body(dishService.getRandomDishFromRestaurantOffer(restaurantName));
    }

    @PreAuthorize("hasAuthority({#restaurantName})")
    @PostMapping("/restaurant")
    public ResponseEntity<Tuple2<Boolean, String>> addDishToRestaurantOffer(
            @RequestParam String dishName,
            @RequestParam String restaurantName,
            @RequestParam MultipartFile dishImg,
            @RequestParam Double dishPrice,
            @RequestParam String dishDescription
    ) throws IOException {
        Dish dishToAdd = Dish.builder()
                .dishName(dishName)
                .restaurantName(restaurantName)
                .dishImg(dishImg.getBytes())
                .dishPrice(dishPrice)
                .dishDescription(dishDescription)
                .build();

        return ResponseEntity.ok()
                .body(dishService.addDishToRestaurantOffer(dishToAdd));
    }

    @PreAuthorize("hasAuthority({#restaurantName})")
    @PatchMapping("/restaurant")
    public ResponseEntity<Tuple2<Boolean, String>> updateDishInRestaurantOffer(
            @RequestParam String dishName,
            @RequestParam String restaurantName,
            @RequestParam MultipartFile dishImg,
            @RequestParam Double dishPrice,
            @RequestParam String dishDescription
    ) throws IOException {
        Dish dishWithNewData = Dish.builder()
                .dishName(dishName)
                .restaurantName(restaurantName)
                .dishImg(dishImg.getBytes())
                .dishPrice(dishPrice)
                .dishDescription(dishDescription)
                .build();

        return ResponseEntity.ok()
                .body(dishService.updateDishInRestaurantOffer(dishWithNewData));
    }

    @PreAuthorize("hasAuthority({#restaurantName})")
    @DeleteMapping("/restaurant")
    public ResponseEntity<Tuple2<Boolean, String>> removeDishFromRestaurantOffer(
            @RequestParam String dishName,
            @RequestParam String restaurantName
    ) {
        return ResponseEntity.ok()
                .body(dishService.removeDishFromRestaurantOffer(dishName, restaurantName));
    }




}
