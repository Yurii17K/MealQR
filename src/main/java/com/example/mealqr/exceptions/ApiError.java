package com.example.mealqr.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiError {

    private final HttpStatus status;
    private final String message;

    private ApiError(String message, HttpStatus httpStatus) {
        this.status = httpStatus;
        this.message = message;
    }

    public static ApiError buildError(String message, HttpStatus httpStatus) {
        return new ApiError(message, httpStatus);
    }

    public static ApiError buildError(String message) {
        return new ApiError(message, HttpStatus.BAD_REQUEST);
    }
}