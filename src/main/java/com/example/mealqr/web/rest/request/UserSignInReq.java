package com.example.mealqr.web.rest.request;

import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Value
public class UserSignInReq {

    @Email(message = "Email should be valid", regexp = ".*@.*\\..*")
    @NotBlank
    String userEmail;

    @NotBlank
    String userPassword;
}
