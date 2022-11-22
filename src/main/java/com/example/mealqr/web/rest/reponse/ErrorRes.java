package com.example.mealqr.web.rest.reponse;

import lombok.Value;

@Value(staticConstructor = "of")
public class ErrorRes {
    String errorMessage;
}
