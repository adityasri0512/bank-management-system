package com.bank.util;

/**
 * Thrown when user-supplied input fails validation
 * (malformed name, email, phone, amount, menu choice, etc.).
 */
public class InvalidInputException extends CustomException {

    public InvalidInputException(String message) {
        super(message);
    }
}
