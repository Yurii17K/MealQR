package com.example.mealqr.exceptions;


import org.springframework.security.core.AuthenticationException;

public class UserSavingException extends Exception {
    public UserSavingException(String s) {
        super(s);
    }

    public UserSavingException(String s, Throwable cause) {
        super(s, cause);
    }
}
