package com.example.mealqr.rest;

import com.example.mealqr.rest.reponse.DishRes;
import com.example.mealqr.rest.reponse.DishWithOpinionsRes;
import com.example.mealqr.rest.request.DishSaveReq;
import com.example.mealqr.rest.request.DishUpdateReq;
import com.example.mealqr.services.DishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.vavr.collection.Seq;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DishController {

    private final DishService dishService;

    @GetMapping("/dishes/{restaurantName}")
    public ResponseEntity<Seq<DishRes>> getAllDishesInRestaurant(
            @Valid @NotBlank @PathVariable("restaurantName") String restaurantName) {
        return dishService.getAllDishesInRestaurant(restaurantName)//
                .map(ResponseEntity::ok)//
                .getOrElseThrow(s -> new RuntimeException(s));
    }

    @GetMapping("/dishes/random")
    public ResponseEntity<DishRes> getRandomDish() {
        return dishService.getRandomDish()
                .map(ResponseEntity::ok)//
                .getOrElseThrow(s -> new RuntimeException(s));
    }

    @GetMapping("/dishes/random/{restaurantName}")
    public ResponseEntity<DishRes> getRandomDish(
            @Valid @NotBlank @PathVariable("restaurantName") String restaurantName) {
        return dishService.getRandomDishFromRestaurantOffer(restaurantName)//
                .map(ResponseEntity::ok)//
                .getOrElseThrow(s -> new RuntimeException(s));
    }

    @PreAuthorize("hasAuthority(#userEmail)")
    @GetMapping("/dishes/user")
    @Operation(summary = "getAllDishesInRestaurantSortedByUserPreference", security = @SecurityRequirement(name = "JWT AUTH"))
    public ResponseEntity<List<DishWithOpinionsRes>> getAllDishesInRestaurantSortedByUserPreference(
            @RequestParam @NotBlank @Valid String userEmail,
            @RequestParam @NotBlank @Valid String restaurantName) {
        return ResponseEntity.ok(dishService.getAllDishesInRestaurantConfiguredForUser(userEmail, restaurantName));
    }

    @PreAuthorize("hasAuthority({#dishSaveReq.restaurantName})")
    @PostMapping("/dishes")
    @Operation(summary = "addDishToRestaurantMenu", security = @SecurityRequirement(name = "JWT AUTH"))
    public ResponseEntity<DishRes> addDishToRestaurantMenu(@RequestBody @Valid DishSaveReq dishSaveReq) {
        return dishService.addDishToRestaurantMenu(dishSaveReq)//
                .map(ResponseEntity::ok)//
                .getOrElseThrow(s -> new RuntimeException(s));
    }

    @PreAuthorize("hasAuthority({#dishUpdateReq.restaurantName})")
    @PutMapping("/dishes")
    @Operation(summary = "updateDishInRestaurantOffer", security = @SecurityRequirement(name = "JWT AUTH"))
    public ResponseEntity<DishRes> updateDishInRestaurantOffer(@RequestBody @Valid DishUpdateReq dishUpdateReq) {
        return dishService.updateDishInRestaurantOffer(dishUpdateReq)//
                .map(ResponseEntity::ok)//
                .getOrElseThrow(s -> new RuntimeException(s));
    }

    @PreAuthorize("hasAuthority({#restaurantName})")
    @DeleteMapping("/dishes")
    @Operation(summary = "removeDishFromRestaurantOffer", security = @SecurityRequirement(name = "JWT AUTH"))
    public ResponseEntity<DishRes> removeDishFromRestaurantOffer(@RequestParam String dishName,
            @RequestParam String restaurantName) {
        return dishService.removeDishFromRestaurantOffer(dishName, restaurantName)
                .map(ResponseEntity::ok)//
                .getOrElseThrow(s -> new RuntimeException(s));
    }
}