package com.bank.util;

/**
 * Thrown when a withdrawal or transfer is attempted for an amount
 * greater than the available balance on the account.
 */
public class InsufficientBalanceException extends CustomException {

    public InsufficientBalanceException(String message) {
        super(message);
    }
}
