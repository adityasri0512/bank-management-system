package com.bank.util;

/**
 * Thrown when an attempt is made to create a record (e.g. customer)
 * that would duplicate an existing unique field such as email.
 */
public class DuplicateRecordException extends CustomException {

    public DuplicateRecordException(String message) {
        super(message);
    }
}
