package com.example.mealqr.web.rest.request;

import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Value(staticConstructor = "of")
public class DishCommentReq {

    @NotBlank
    String dishId;

    @NotBlank(message = "Comment can not be empty")
    @Size(max = 1000, message = "Comment can not exceed 1000 symbols")
    String comment;
}
