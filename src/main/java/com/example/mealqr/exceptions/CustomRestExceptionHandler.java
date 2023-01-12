package com.example.mealqr.exceptions;

import com.example.mealqr.web.rest.reponse.ErrorRes;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex,
            final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        logger.info(ex.getClass().getName());
        //
        final List<String> errorMessages = new ArrayList<>();
        for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
            errorMessages.add(error.getDefaultMessage());
        }
        for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errorMessages.add(error.getDefaultMessage());
        }

        final ApiError apiError = ApiError.buildError(String.join("; ", errorMessages));
        return handleExceptionInternal(ex, apiError, headers, apiError.getStatus(), request);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Object> handleCustomException(final ApiException apiException) {
        return new ResponseEntity<>(ErrorRes.of(apiException.getMessage()), apiException.getHttpStatus());
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<Object> handleCustomException(final SQLException sqlException) {
        return new ResponseEntity<>(ErrorRes.of("Our database is experiencing a high load, please try again in 10m"),
                HttpStatus.BANDWIDTH_LIMIT_EXCEEDED);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Object> handleCustomException(final DataAccessException dataAccessException) {
        return new ResponseEntity<>(ErrorRes.of("Our databases are under maintenance by a provider, please try again in 15m"),
                HttpStatus.REQUEST_TIMEOUT);
    }


}