package com.bank.model;

/**
 * Represents a single transaction (deposit, withdrawal, or transfer leg)
 * performed on an account. Every transaction is immutable once recorded.
 */
public class Transaction {

    private String transactionId;
    private String accountNumber;
    private String type;
    private double amount;
    private double balanceAfter;
    private String timestamp;
    private String description;

    public Transaction(String transactionId, String accountNumber, String type, double amount,
                        double balanceAfter, String timestamp, String description) {
        this.transactionId = transactionId;
        this.accountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.timestamp = timestamp;
        this.description = description;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public double getBalanceAfter() {
        return balanceAfter;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Serializes this transaction into a single pipe-delimited line for file storage.
     */
    public String toFileString() {
        return transactionId + "|" + accountNumber + "|" + type + "|" + amount + "|"
                + balanceAfter + "|" + timestamp + "|" + description;
    }

    /**
     * Rebuilds a Transaction object from a pipe-delimited line read from file.
     */
    public static Transaction fromFileString(String line) {
        String[] parts = line.split("\\|", -1);
        return new Transaction(parts[0], parts[1], parts[2], Double.parseDouble(parts[3]),
                Double.parseDouble(parts[4]), parts[5], parts[6]);
    }

    @Override
    public String toString() {
        return String.format("%-10s %-12s %-14s %-12.2f %-12.2f %-20s %-20s",
                transactionId, accountNumber, type, amount, balanceAfter, timestamp, description);
    }
}
