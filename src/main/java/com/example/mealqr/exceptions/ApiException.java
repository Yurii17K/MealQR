package com.example.mealqr.exceptions;

import io.vavr.collection.Seq;

import java.util.stream.Collectors;

public class ApiException extends RuntimeException {

    public ApiException(String errorMessage) {
        super(errorMessage, null, true, false);
    }

    public ApiException(Seq<String> errorMessages) {
        super(errorMessages.collect(Collectors.joining("; ")), null, true, false);
    }
}
