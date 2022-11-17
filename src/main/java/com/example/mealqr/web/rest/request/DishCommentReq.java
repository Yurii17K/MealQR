package com.example.mealqr.web.rest.request;

import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Value(staticConstructor = "of")
public class DishCommentReq {

    @NotNull
    Integer dishId;

    @NotBlank
    @Size(max = 1000)
    String comment;
}
