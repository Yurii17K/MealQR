package com.example.mealqr.rest.request;

import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Value(staticConstructor = "of")
public class DishCommentReq {

    @NotBlank
    @Email(message = "Email should be valid", regexp = ".*@.*\\..*")
    String userEmail;

    @NotBlank
    String dishName;

    @NotBlank
    String restaurantName;

    @NotBlank
    @Size(max = 1000)
    String comment;
}
