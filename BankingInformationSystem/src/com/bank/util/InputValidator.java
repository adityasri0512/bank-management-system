package com.bank.util;

import java.util.regex.Pattern;

/**
 * Central place for all input validation rules used throughout the system.
 * Every method throws InvalidInputException with a clear, user-friendly
 * message when a rule is violated, so the UI layer can catch and display it.
 */
public final class InputValidator {

    private static final Pattern NAME_PATTERN = Pattern.compile("^[A-Za-z ]{2,50}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[6-9][0-9]{9}$");

    private InputValidator() {
        // Utility class: prevent instantiation.
    }

    public static void validateName(String name) throws InvalidInputException {
        if (name == null || !NAME_PATTERN.matcher(name.trim()).matches()) {
            throw new InvalidInputException("Invalid name. Use only alphabets and spaces (2-50 characters).");
        }
    }

    public static void validateEmail(String email) throws InvalidInputException {
        if (email == null || !EMAIL_PATTERN.matcher(email.trim()).matches()) {
            throw new InvalidInputException("Invalid email address format.");
        }
    }

    public static void validatePhone(String phone) throws InvalidInputException {
        if (phone == null || !PHONE_PATTERN.matcher(phone.trim()).matches()) {
            throw new InvalidInputException("Invalid phone number. Must be a 10-digit number starting with 6-9.");
        }
    }

    public static void validateAddress(String address) throws InvalidInputException {
        if (address == null || address.trim().isEmpty() || address.trim().length() > 100) {
            throw new InvalidInputException("Address cannot be empty and must be under 100 characters.");
        }
    }

    /**
     * Validates and parses a monetary amount. Amount must be a positive number.
     */
    public static double validateAmount(String amountStr) throws InvalidInputException {
        double amount;
        try {
            amount = Double.parseDouble(amountStr.trim());
        } catch (NumberFormatException | NullPointerException e) {
            throw new InvalidInputException("Amount must be a valid number.");
        }
        if (amount <= 0) {
            throw new InvalidInputException("Amount must be greater than zero.");
        }
        return amount;
    }

    /**
     * Validates account type and normalizes it to upper case.
     */
    public static String validateAccountType(String type) throws InvalidInputException {
        if (type == null || type.trim().isEmpty()) {
            throw new InvalidInputException("Account type cannot be empty.");
        }
        String upper = type.trim().toUpperCase();
        if (!upper.equals("SAVINGS") && !upper.equals("CURRENT")) {
            throw new InvalidInputException("Account type must be SAVINGS or CURRENT.");
        }
        return upper;
    }

    /**
     * Validates that a menu choice is a whole number within the given inclusive range.
     */
    public static int validateMenuChoice(String input, int min, int max) throws InvalidInputException {
        int choice;
        try {
            choice = Integer.parseInt(input.trim());
        } catch (NumberFormatException | NullPointerException e) {
            throw new InvalidInputException("Please enter a valid number between " + min + " and " + max + ".");
        }
        if (choice < min || choice > max) {
            throw new InvalidInputException("Choice out of range. Enter a number between " + min + " and " + max + ".");
        }
        return choice;
    }

    public static void validateNotEmpty(String value, String fieldName) throws InvalidInputException {
        if (value == null || value.trim().isEmpty()) {
            throw new InvalidInputException(fieldName + " cannot be empty.");
        }
    }
}
