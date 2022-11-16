package com.example.mealqr.web.rest;

import com.example.mealqr.exceptions.ApiException;
import com.example.mealqr.services.DishService;
import com.example.mealqr.web.rest.reponse.DishRes;
import com.example.mealqr.web.rest.reponse.DishWithOpinionsRes;
import com.example.mealqr.web.rest.request.DishSaveReq;
import com.example.mealqr.web.rest.request.DishUpdateReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.vavr.collection.Seq;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Validated
public class DishController {

    private final DishService dishService;

    @GetMapping("/dishes/{restaurantId}")
    public ResponseEntity<List<DishRes>> getAllDishesInRestaurant(
            @Valid @NotBlank @PathVariable("restaurantId") String restaurantId) {
        return dishService.getAllDishesInRestaurant(restaurantId)//
                .map(Seq::asJava)//
                .map(ResponseEntity::ok)//
                .getOrElseThrow(ApiException::new);
    }

    @GetMapping("/dishes/random")
    public ResponseEntity<DishRes> getRandomDish() {
        return dishService.getRandomDish()
                .map(ResponseEntity::ok)//
                .getOrElseThrow(ApiException::new);
    }

    @GetMapping("/dishes/random/{restaurantId}")
    public ResponseEntity<DishRes> getRandomDish(
            @Valid @NotBlank @PathVariable("restaurantId") String restaurantId) {
        return dishService.getRandomDishFromRestaurantOffer(restaurantId)//
                .map(ResponseEntity::ok)//
                .getOrElseThrow(ApiException::new);
    }

    @GetMapping("/dishes/user")
    @Operation(summary = "getAllDishesInRestaurantSortedByUserPreference", security = @SecurityRequirement(name = "JWT AUTH"))
    public ResponseEntity<List<DishWithOpinionsRes>> getAllDishesInRestaurantSortedByUserPreference(Principal principal,
            @RequestParam @NotBlank @Valid String restaurantId) {
        return ResponseEntity.ok(dishService.getAllDishesInRestaurantConfiguredForUser(principal.getName(), restaurantId));
    }

    @PreAuthorize("hasAuthority({#dishSaveReq.restaurantId})")
    @PostMapping("/dishes")
    @Operation(summary = "addDishToRestaurantMenu", security = @SecurityRequirement(name = "JWT AUTH"))
    public ResponseEntity<DishRes> addDishToRestaurantMenu(@RequestBody @Valid DishSaveReq dishSaveReq) {
        return dishService.addDishToRestaurantMenu(dishSaveReq)//
                .map(ResponseEntity::ok)//
                .getOrElseThrow(ApiException::new);
    }

    @PreAuthorize("hasAuthority({#dishUpdateReq.restaurantId})")
    @PutMapping("/dishes")
    @Operation(summary = "updateDishInRestaurantOffer", security = @SecurityRequirement(name = "JWT AUTH"))
    public ResponseEntity<DishRes> updateDishInRestaurantOffer(@RequestBody @Valid DishUpdateReq dishUpdateReq) {
        return dishService.updateDishInRestaurantOffer(dishUpdateReq)//
                .map(ResponseEntity::ok)//
                .getOrElseThrow(ApiException::new);
    }

    @PreAuthorize("hasAuthority({#restaurantId})")
    @DeleteMapping("/dishes")
    @Operation(summary = "removeDishFromRestaurantOffer", security = @SecurityRequirement(name = "JWT AUTH"))
    public ResponseEntity<DishRes> removeDishFromRestaurantOffer(@RequestParam String dishName,
            @RequestParam String restaurantId) {
        return dishService.removeDishFromRestaurantOffer(dishName, restaurantId)
                .map(ResponseEntity::ok)//
                .getOrElseThrow(ApiException::new);
    }
}