package com.example.mealqr.web.rest.request;

import lombok.Value;

import javax.validation.constraints.*;

@Value(staticConstructor = "of")
public class ChangeUserPasswordReq {

    @NotBlank(message = "Old password can not be empty")
    String oldPassword;
    @NotBlank
    @Size(min = 12, message = "New password is too short")
    String newPassword;
}
