package com.example.mealqr.web.rest.request;

import lombok.Value;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Value
public class ReportedCommentReq {

    @NotNull
    int commentId;

    @Size(max = 1000)
    String reasoning;
}
