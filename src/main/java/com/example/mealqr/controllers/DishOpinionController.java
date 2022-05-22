package com.example.mealqr.controllers;

import com.example.mealqr.services.DishOpinionService;
import io.vavr.Tuple2;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/opinions")
@AllArgsConstructor
public class DishOpinionController {

    private final DishOpinionService dishOpinionService;

    @PreAuthorize("hasAuthority(#userEmail)")
    @PostMapping("/comments")
    public ResponseEntity<Tuple2<Boolean, String>> addComment(
            @RequestParam String userEmail,
            @RequestParam String dishName,
            @RequestParam String restaurantName,
            @RequestParam String comment
    ) {
        return ResponseEntity.ok()
                .body(dishOpinionService.addOrUpdateComment(userEmail, dishName, restaurantName, comment));
    }


    @PreAuthorize("hasAuthority(#userEmail)")
    @PostMapping("/ratings")
    public ResponseEntity<Tuple2<Boolean, String>> addRating(
            @RequestParam String userEmail,
            @RequestParam String dishName,
            @RequestParam String restaurantName,
            @RequestParam Integer rating
    ) {
        return ResponseEntity.ok()
                .body(dishOpinionService.addOrUpdateRating(userEmail, dishName, restaurantName, rating));
    }

    @PreAuthorize("hasAuthority(#userEmail)")
    @PatchMapping("/comments")
    public ResponseEntity<Tuple2<Boolean, String>> updateComment(
            @RequestParam String userEmail,
            @RequestParam String dishName,
            @RequestParam String restaurantName,
            @RequestParam String comment
    ) {
        return ResponseEntity.ok()
                .body(dishOpinionService.addOrUpdateComment(userEmail, dishName, restaurantName, comment));
    }

    @PreAuthorize("hasAuthority(#userEmail)")
    @PatchMapping("/ratings")
    public ResponseEntity<Tuple2<Boolean, String>> updateRating(
            @RequestParam String userEmail,
            @RequestParam String dishName,
            @RequestParam String restaurantName,
            @RequestParam Integer rating
    ) {
        return ResponseEntity.ok()
                .body(dishOpinionService.addOrUpdateRating(userEmail, dishName, restaurantName, rating));
    }
}
