package com.example.mealqr.web.rest;

import com.example.mealqr.services.RestaurantService;
import com.example.mealqr.web.rest.reponse.RestaurantRes;
import com.example.mealqr.web.rest.request.RestaurantSaveReq;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    @PostMapping("/restaurant")
    public ResponseEntity<RestaurantRes> createRestaurant(@RequestBody RestaurantSaveReq restaurantSaveReq) {
        return ResponseEntity.ok(restaurantService.createRestaurant(restaurantSaveReq));
    }
}
