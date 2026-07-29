package com.bank.model;

/**
 * Represents a bank customer.
 * A customer can own one or more accounts.
 */
public class Customer {

    private String customerId;
    private String name;
    private String email;
    private String phone;
    private String address;

    public Customer(String customerId, String name, String email, String phone, String address) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Serializes this customer into a single pipe-delimited line for file storage.
     */
    public String toFileString() {
        return customerId + "|" + name + "|" + email + "|" + phone + "|" + address;
    }

    /**
     * Rebuilds a Customer object from a pipe-delimited line read from file.
     */
    public static Customer fromFileString(String line) {
        String[] parts = line.split("\\|", -1);
        return new Customer(parts[0], parts[1], parts[2], parts[3], parts[4]);
    }

    @Override
    public String toString() {
        return String.format("%-10s %-20s %-28s %-14s %-25s", customerId, name, email, phone, address);
    }
}
