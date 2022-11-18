package com.example.mealqr.web.rest.request;

import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Value
public class ReportedCommentReq {

    @NotBlank
    String commentId;

    @Size(max = 1000)
    String reasoning;
}
