package com.example.mealqr.web.rest;

import com.example.mealqr.exceptions.ApiException;
import com.example.mealqr.security.CustomPrincipal;
import com.example.mealqr.services.DishService;
import com.example.mealqr.web.rest.reponse.DishRes;
import com.example.mealqr.web.rest.reponse.DishWithOpinionsRes;
import com.example.mealqr.web.rest.request.DishSaveReq;
import com.example.mealqr.web.rest.request.DishUpdateReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.vavr.collection.Seq;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = ""),
            @ApiResponse(responseCode = "400", description = "The restaurant is empty or doesn't exist")})
    @Operation(summary = "Get all dishes in the restaurant")
    public ResponseEntity<List<DishRes>> getAllDishesInRestaurant(
            @Valid @NotBlank @PathVariable("restaurantId") String restaurantId) {
        return dishService.getAllDishesInRestaurant(restaurantId)//
                .map(Seq::asJava)//
                .map(ResponseEntity::ok)//
                .getOrElseThrow(ApiException::new);
    }

    @GetMapping("/dish/{dishId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = ""),
            @ApiResponse(responseCode = "400", description = "The given dish does not exist")})
    @Operation(summary = "Get a specific dish by dishId")
    public ResponseEntity<DishRes> getDishById(
            @Valid @NotBlank @PathVariable("dishId") String dishId) {
        return dishService.getSpecificDish(dishId).map(ResponseEntity::ok)//
                .getOrElseThrow(ApiException::new);
    }



    @GetMapping("/dishes/random")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = ""),
            @ApiResponse(responseCode = "400", description = "App is in the development and no dishes are present in the database")})
    @Operation(summary = "Get a random dish from a random restaurant")
    public ResponseEntity<DishRes> getRandomDish() {
        return dishService.getRandomDish()
                .map(ResponseEntity::ok)//
                .getOrElseThrow(ApiException::new);
    }

    @GetMapping("/dishes/random/{restaurantId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = ""),
            @ApiResponse(responseCode = "400", description = "The restaurant is empty or doesn't exist")})
    @Operation(summary = "Get a random dish from the restaurant")
    public ResponseEntity<DishRes> getRandomDish(
            @Valid @NotBlank @PathVariable("restaurantId") String restaurantId) {
        return dishService.getRandomDishFromRestaurantOffer(restaurantId)//
                .map(ResponseEntity::ok)//
                .getOrElseThrow(ApiException::new);
    }

    @GetMapping("/dishes/user")
    @ApiResponse(responseCode = "200", description = "")
    @Operation(summary = "getAllDishesInRestaurantSortedByUserPreference", security = @SecurityRequirement(name = "JWT AUTH"))
    public ResponseEntity<List<DishWithOpinionsRes>> getAllDishesInRestaurantSortedByUserPreference(Principal principal,
            @RequestParam @NotBlank @Valid String restaurantId) {
        return ResponseEntity.ok(dishService.getAllDishesInRestaurantConfiguredForUser(principal.getName(), restaurantId));
    }

    @PreAuthorize("hasAuthority({#dishSaveReq.restaurantId})")
    @PostMapping("/dishes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = ""),
            @ApiResponse(responseCode = "400", description = "Dish with this name already exists in the restaurant")})
    @Operation(summary = "Add dish to the restaurant's menu", security = @SecurityRequirement(name = "JWT AUTH"))
    public ResponseEntity<DishRes> addDishToRestaurantMenu(@RequestBody @Valid DishSaveReq dishSaveReq) {
        return dishService.addDishToRestaurantMenu(dishSaveReq)//
                .map(ResponseEntity::ok)//
                .getOrElseThrow(ApiException::new);
    }

    @PreAuthorize("hasAuthority({#dishUpdateReq.restaurantId})")
    @PutMapping("/dishes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = ""),
            @ApiResponse(responseCode = "400", description = "Dish with this name already exists in the restaurant"),
            @ApiResponse(responseCode = "404", description = "Dish does not exist")})
    @Operation(summary = "updateDishInRestaurantOffer", security = @SecurityRequirement(name = "JWT AUTH"))
    public ResponseEntity<DishRes> updateDishInRestaurantOffer(@RequestBody @Valid DishUpdateReq dishUpdateReq) {
        return dishService.updateDishInRestaurantOffer(dishUpdateReq)//
                .map(ResponseEntity::ok)//
                .getOrElseThrow(ApiException::new);
    }

//    @PreAuthorize("hasAuthority({#restaurantId})") // verification happens in the service
    @DeleteMapping("/dishes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = ""),
            @ApiResponse(responseCode = "404", description = "Dish with this id does not exists in the restaurant")})
    @Operation(summary = "removeDishFromRestaurantOffer", security = @SecurityRequirement(name = "JWT AUTH"))
    public ResponseEntity<Boolean> removeDishFromRestaurantOffer(@RequestParam String dishId, Authentication authentication) {
        CustomPrincipal principal = (CustomPrincipal) authentication.getPrincipal();
        return dishService.removeDishFromRestaurantOffer(dishId, principal)
                .map(ResponseEntity::ok)//
                .getOrElseThrow(ApiException::new);
    }
}