package com.bank.util;

/**
 * Thrown when a lookup is performed for a customer, account, or other
 * record that does not exist in the system.
 */
public class RecordNotFoundException extends CustomException {

    public RecordNotFoundException(String message) {
        super(message);
    }
}
