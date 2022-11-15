package com.example.mealqr.rest;

import com.example.mealqr.domain.DishComment;
import com.example.mealqr.domain.DishRating;
import com.example.mealqr.rest.request.DishCommentReq;
import com.example.mealqr.rest.request.DishRatingReq;
import com.example.mealqr.services.DishOpinionService;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "addOrUpdateComment", security = @SecurityRequirement(name = "JWT AUTH"))
    public ResponseEntity<DishComment> addOrUpdateComment(Principal principal,
            @RequestBody @Valid DishCommentReq dishCommentReq) {
        return dishOpinionService.addOrUpdateComment(principal.getName(), dishCommentReq)//
                .map(ResponseEntity::ok)//
                .getOrElseThrow(s -> new RuntimeException(s));
    }

    @PostMapping("/opinion/add-rating")
    @Operation(summary = "addOrUpdateRating", security = @SecurityRequirement(name = "JWT AUTH"))
    public ResponseEntity<DishRating> addOrUpdateRating(Principal principal,
            @RequestBody @Valid DishRatingReq dishRatingReq) {
        return dishOpinionService.addOrUpdateRating(principal.getName(), dishRatingReq)//
                        .map(ResponseEntity::ok)//
                        .getOrElseThrow(s -> new RuntimeException(s));
    }
}
