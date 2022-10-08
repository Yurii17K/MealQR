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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DishOpinionController {

    private final DishOpinionService dishOpinionService;

    @PreAuthorize("hasAuthority({#dishCommentReq.userEmail})")
    @PostMapping("/opinion/add-comment")
    @Operation(summary = "addOrUpdateComment", security = @SecurityRequirement(name = "JWT AUTH"))
    public ResponseEntity<DishComment> addOrUpdateComment(@RequestBody @Valid DishCommentReq dishCommentReq) {
        return dishOpinionService.addOrUpdateComment(dishCommentReq)//
                .map(ResponseEntity::ok)//
                .getOrElseThrow(s -> new RuntimeException(s));
    }

    @PreAuthorize("hasAuthority({#dishRatingReq.userEmail})")
    @PostMapping("/opinion/add-rating")
    @Operation(summary = "addOrUpdateRating", security = @SecurityRequirement(name = "JWT AUTH"))
    public ResponseEntity<DishRating> addOrUpdateRating(@RequestBody @Valid DishRatingReq dishRatingReq) {
        return dishOpinionService.addOrUpdateRating(dishRatingReq)//
                        .map(ResponseEntity::ok)//
                        .getOrElseThrow(s -> new RuntimeException(s));
    }
}
