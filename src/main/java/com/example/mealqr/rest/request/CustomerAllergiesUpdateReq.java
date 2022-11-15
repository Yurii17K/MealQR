package com.example.mealqr.rest.request;

import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
public class CustomerAllergiesUpdateReq {

    @NotBlank
    String allergies;
}
