package com.example.mealqr.exceptions;

import io.vavr.collection.Seq;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.stream.Collectors;

@Getter
public class ApiException extends RuntimeException {

    private final HttpStatus httpStatus;

    public ApiException(ApiError apiError) {
        super(apiError.getMessage(), null, true, false);
        this.httpStatus = apiError.getStatus();
    }

    public ApiException(Seq<ApiError> apiError) {
        super(apiError.map(ApiError::getMessage).collect(Collectors.joining("; ")), null, true, false);
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }
}
