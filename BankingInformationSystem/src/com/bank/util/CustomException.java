package com.bank.util;

/**
 * Base checked exception for all banking business-rule errors.
 * All specific banking exceptions extend this class so calling code
 * can either catch a precise subtype or handle every case generically.
 */
public class CustomException extends Exception {

    public CustomException(String message) {
        super(message);
    }
}
