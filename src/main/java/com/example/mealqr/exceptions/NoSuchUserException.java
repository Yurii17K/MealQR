package com.example.mealqr.exceptions;

import org.springframework.security.core.AuthenticationException;

public class NoSuchUserException extends AuthenticationException {
    public NoSuchUserException(String msg) {
        super(msg);
    }

    public NoSuchUserException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
