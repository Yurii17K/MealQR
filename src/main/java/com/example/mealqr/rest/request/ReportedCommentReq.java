package com.example.mealqr.rest.request;

import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Value
public class ReportedCommentReq {

    @NotNull
    int commentId;

    @NotBlank
    @Email(message = "Email should be valid", regexp = ".*@.*\\..*")
    String userEmail;

    @Size(max = 1000)
    String reasoning;
}
