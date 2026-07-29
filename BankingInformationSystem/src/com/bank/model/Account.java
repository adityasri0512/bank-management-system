package com.bank.model;

/**
 * Represents a bank account belonging to a customer.
 * Account type is either SAVINGS or CURRENT.
 */
public class Account {

    private String accountNumber;
    private String customerId;
    private String accountType;
    private double balance;
    private String dateOpened;

    public Account(String accountNumber, String customerId, String accountType, double balance, String dateOpened) {
        this.accountNumber = accountNumber;
        this.customerId = customerId;
        this.accountType = accountType;
        this.balance = balance;
        this.dateOpened = dateOpened;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getAccountType() {
        return accountType;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getDateOpened() {
        return dateOpened;
    }

    /**
     * Serializes this account into a single pipe-delimited line for file storage.
     */
    public String toFileString() {
        return accountNumber + "|" + customerId + "|" + accountType + "|" + balance + "|" + dateOpened;
    }

    /**
     * Rebuilds an Account object from a pipe-delimited line read from file.
     */
    public static Account fromFileString(String line) {
        String[] parts = line.split("\\|", -1);
        return new Account(parts[0], parts[1], parts[2], Double.parseDouble(parts[3]), parts[4]);
    }

    @Override
    public String toString() {
        return String.format("%-12s %-10s %-10s %-15.2f %-12s", accountNumber, customerId, accountType, balance, dateOpened);
    }
}
