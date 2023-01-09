package com.example.mealqr.web.rest.request;

import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Value
public class UserSignInReq {

    @Email(message = "User email should be valid", regexp = ".*@.*\\..*")
    String userEmail;

    @NotBlank(message = "User password can not be empty")
    String userPassword;
}
