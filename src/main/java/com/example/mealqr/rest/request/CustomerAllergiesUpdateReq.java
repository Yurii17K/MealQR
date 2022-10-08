package com.example.mealqr.rest.request;

import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Value
public class CustomerAllergiesUpdateReq {

    @NotBlank
    @Email(message = "Email should be valid", regexp = ".*@.*\\..*")
    String userEmail;

    @NotBlank
    String allergies;
}
