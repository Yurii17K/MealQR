package com.example.mealqr.exceptions;

public class CustomerAllergyUpdateException extends Exception {
    public CustomerAllergyUpdateException(String s) {
        super(s);
    }

    public CustomerAllergyUpdateException(String s, Throwable cause) {
        super(s, cause);
    }
}
