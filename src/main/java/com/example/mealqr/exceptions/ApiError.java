package com.example.mealqr.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
public class ApiError {

    private final HttpStatus status;
    private final String message;
    private final List<String> errors;

    public ApiError(String message, List<String> errors) {
        super();
        this.status = HttpStatus.BAD_REQUEST;
        this.message = message;
        this.errors = errors;
    }

    public ApiError(String message) {
        super();
        this.status = HttpStatus.BAD_REQUEST;
        this.message = message;
        this.errors = List.of();
    }
}