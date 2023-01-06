package com.example.mealqr.web.rest.request;

import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Value(staticConstructor = "of")
public class DishCommentReq {

    @NotBlank
    String dishId;

    @NotBlank
    @Size(max = 1000, message = "Please shorted your comment to 1000 symbols")
    String comment;
}
