package com.example.mealqr.web.rest;

import com.example.mealqr.domain.DishComment;
import com.example.mealqr.domain.DishRating;
import com.example.mealqr.exceptions.ApiException;
import com.example.mealqr.security.SecurityUtils;
import com.example.mealqr.services.DishOpinionService;
import com.example.mealqr.web.rest.request.DishCommentReq;
import com.example.mealqr.web.rest.request.DishRatingReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DishOpinionController {

    private final DishOpinionService dishOpinionService;

    @PostMapping("/opinion/add-comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404", description = "Dish doesn't exist")})
    @Operation(summary = "addOrUpdateComment", security = @SecurityRequirement(name = "JWT AUTH"))
    public ResponseEntity<DishComment> addOrUpdateComment(Principal principal,
            @RequestBody @Valid DishCommentReq dishCommentReq) {
        return dishOpinionService.addOrUpdateComment(principal.getName(), dishCommentReq)//
                .map(ResponseEntity::ok)//
                .getOrElseThrow(ApiException::new);
    }

    @PostMapping("/opinion/add-rating")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404", description = "Dish doesn't exist")})
    @Operation(summary = "addOrUpdateRating", security = @SecurityRequirement(name = "JWT AUTH"))
    public ResponseEntity<DishRating> addOrUpdateRating(
            @RequestBody @Valid DishRatingReq dishRatingReq) {
        String username = SecurityUtils.getCurrentUserLogin()//
                .getOrElse("unauthenticated@user.rating");
        return dishOpinionService.addOrUpdateRating(username, dishRatingReq)//
                .map(ResponseEntity::ok)//
                .getOrElseThrow(ApiException::new);
    }
}
