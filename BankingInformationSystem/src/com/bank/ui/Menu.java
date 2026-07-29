package com.bank.ui;

import com.bank.model.Account;
import com.bank.model.Customer;
import com.bank.model.Transaction;
import com.bank.service.AuthenticationService;
import com.bank.service.BankService;
import com.bank.util.CustomException;
import com.bank.util.InputValidator;

import java.util.List;
import java.util.Scanner;

/**
 * Console user interface for the Banking Information System.
 * Responsible only for displaying menus, reading input, and delegating
 * every business decision to BankService / AuthenticationService.
 */
public class Menu {

    private final Scanner scanner;
    private final BankService bankService;
    private final AuthenticationService authService;

    public Menu(BankService bankService, AuthenticationService authService) {
        this.scanner = new Scanner(System.in);
        this.bankService = bankService;
        this.authService = authService;
    }

    /**
     * Entry point for the UI: shows the banner, handles login, runs the
     * main menu loop, then saves all data before returning.
     */
    public void start() {
        printWelcomeBanner();
        if (!authenticateUser()) {
            System.out.println("Too many failed login attempts. Exiting application.");
            return;
        }
        runMainMenu();
        bankService.saveAll();
        System.out.println("\nAll data saved successfully. Thank you for using the Banking Information System!");
    }

    private void printWelcomeBanner() {
        System.out.println("=================================================");
        System.out.println("   WELCOME TO THE BANKING INFORMATION SYSTEM");
        System.out.println("=================================================");
    }

    private boolean authenticateUser() {
        int attempts = 3;
        while (attempts > 0) {
            System.out.print("Username: ");
            String username = scanner.nextLine();
            System.out.print("Password: ");
            String password = scanner.nextLine();
            if (authService.login(username, password)) {
                System.out.println("\nLogin successful. Welcome, " + username + "!\n");
                return true;
            }
            attempts--;
            System.out.println("Invalid credentials. Attempts remaining: " + attempts + "\n");
        }
        return false;
    }

    private void runMainMenu() {
        boolean running = true;
        while (running) {
            printMainMenu();
            String input = scanner.nextLine();
            try {
                int choice = InputValidator.validateMenuChoice(input, 0, 13);
                switch (choice) {
                    case 1:
                        addCustomer();
                        break;
                    case 2:
                        updateCustomer();
                        break;
                    case 3:
                        deleteCustomer();
                        break;
                    case 4:
                        searchCustomer();
                        break;
                    case 5:
                        listAllCustomers();
                        break;
                    case 6:
                        createAccount();
                        break;
                    case 7:
                        deposit();
                        break;
                    case 8:
                        withdraw();
                        break;
                    case 9:
                        transferFunds();
                        break;
                    case 10:
                        balanceEnquiry();
                        break;
                    case 11:
                        transactionHistory();
                        break;
                    case 12:
                        listAllAccounts();
                        break;
                    case 13:
                        showReport();
                        break;
                    case 0:
                        running = false;
                        break;
                    default:
                        break;
                }
            } catch (CustomException e) {
                System.out.println("\n[ERROR] " + e.getMessage() + "\n");
            }
        }
    }

    private void printMainMenu() {
        System.out.println("---------------- MAIN MENU ----------------");
        System.out.println(" 1. Add Customer");
        System.out.println(" 2. Update Customer");
        System.out.println(" 3. Delete Customer");
        System.out.println(" 4. Search Customer");
        System.out.println(" 5. List All Customers");
        System.out.println(" 6. Create Account");
        System.out.println(" 7. Deposit");
        System.out.println(" 8. Withdraw");
        System.out.println(" 9. Transfer Funds");
        System.out.println("10. Balance Enquiry");
        System.out.println("11. Transaction History");
        System.out.println("12. List All Accounts");
        System.out.println("13. Bank Summary Report");
        System.out.println(" 0. Exit");
        System.out.println("--------------------------------------------");
        System.out.print("Enter your choice: ");
    }

    private void addCustomer() throws CustomException {
        System.out.println("\n-- Add New Customer --");
        String name = prompt("Name: ");
        String email = prompt("Email: ");
        String phone = prompt("Phone (10 digits): ");
        String address = prompt("Address: ");
        Customer customer = bankService.addCustomer(name, email, phone, address);
        System.out.println("Customer added successfully. Customer ID: " + customer.getCustomerId() + "\n");
    }

    private void updateCustomer() throws CustomException {
        System.out.println("\n-- Update Customer --");
        String id = prompt("Customer ID: ");
        String name = prompt("New Name: ");
        String email = prompt("New Email: ");
        String phone = prompt("New Phone: ");
        String address = prompt("New Address: ");
        bankService.updateCustomer(id, name, email, phone, address);
        System.out.println("Customer updated successfully.\n");
    }

    private void deleteCustomer() throws CustomException {
        System.out.println("\n-- Delete Customer --");
        String id = prompt("Customer ID: ");
        bankService.deleteCustomer(id);
        System.out.println("Customer deleted successfully.\n");
    }

    private void searchCustomer() {
        System.out.println("\n-- Search Customer --");
        String keyword = prompt("Enter name, ID, or email keyword: ");
        List<Customer> results = bankService.searchCustomers(keyword);
        if (results.isEmpty()) {
            System.out.println("No matching customers found.\n");
            return;
        }
        printCustomerHeader();
        for (Customer customer : results) {
            System.out.println(customer);
        }
        System.out.println();
    }

    private void listAllCustomers() {
        System.out.println("\n-- All Customers --");
        List<Customer> allCustomers = bankService.getAllCustomers();
        if (allCustomers.isEmpty()) {
            System.out.println("No customers found.\n");
            return;
        }
        printCustomerHeader();
        for (Customer customer : allCustomers) {
            System.out.println(customer);
        }
        System.out.println();
    }

    private void printCustomerHeader() {
        System.out.println(String.format("%-10s %-20s %-28s %-14s %-25s", "ID", "Name", "Email", "Phone", "Address"));
        System.out.println(repeat("-", 97));
    }

    private void createAccount() throws CustomException {
        System.out.println("\n-- Create Account --");
        String customerId = prompt("Customer ID: ");
        String type = prompt("Account Type (SAVINGS/CURRENT): ");
        String depositStr = prompt("Initial Deposit (0 for none): ");
        double deposit = depositStr.trim().isEmpty() ? 0 : InputValidator.validateAmount(depositStr);
        Account account = bankService.createAccount(customerId, type, deposit);
        System.out.println("Account created successfully. Account Number: " + account.getAccountNumber() + "\n");
    }

    private void deposit() throws CustomException {
        System.out.println("\n-- Deposit --");
        String accountNumber = prompt("Account Number: ");
        String amountStr = prompt("Amount: ");
        double amount = InputValidator.validateAmount(amountStr);
        bankService.deposit(accountNumber, amount);
        System.out.printf("Deposit successful. New balance: %.2f%n%n", bankService.balanceEnquiry(accountNumber));
    }

    private void withdraw() throws CustomException {
        System.out.println("\n-- Withdraw --");
        String accountNumber = prompt("Account Number: ");
        String amountStr = prompt("Amount: ");
        double amount = InputValidator.validateAmount(amountStr);
        bankService.withdraw(accountNumber, amount);
        System.out.printf("Withdrawal successful. New balance: %.2f%n%n", bankService.balanceEnquiry(accountNumber));
    }

    private void transferFunds() throws CustomException {
        System.out.println("\n-- Transfer Funds --");
        String fromAccount = prompt("From Account Number: ");
        String toAccount = prompt("To Account Number: ");
        String amountStr = prompt("Amount: ");
        double amount = InputValidator.validateAmount(amountStr);
        bankService.transferFunds(fromAccount, toAccount, amount);
        System.out.println("Transfer completed successfully.\n");
    }

    private void balanceEnquiry() throws CustomException {
        System.out.println("\n-- Balance Enquiry --");
        String accountNumber = prompt("Account Number: ");
        double balance = bankService.balanceEnquiry(accountNumber);
        System.out.printf("Current balance: %.2f%n%n", balance);
    }

    private void transactionHistory() throws CustomException {
        System.out.println("\n-- Transaction History --");
        String accountNumber = prompt("Account Number: ");
        List<Transaction> history = bankService.getTransactionHistory(accountNumber);
        if (history.isEmpty()) {
            System.out.println("No transactions found for this account.\n");
            return;
        }
        System.out.println(String.format("%-10s %-12s %-14s %-12s %-12s %-20s %-20s",
                "TxnID", "Account", "Type", "Amount", "Balance", "Timestamp", "Description"));
        System.out.println(repeat("-", 112));
        for (Transaction transaction : history) {
            System.out.println(transaction);
        }
        System.out.println();
    }

    private void listAllAccounts() {
        System.out.println("\n-- All Accounts --");
        List<Account> allAccounts = bankService.getAllAccounts();
        if (allAccounts.isEmpty()) {
            System.out.println("No accounts found.\n");
            return;
        }
        System.out.println(String.format("%-12s %-10s %-10s %-15s %-12s", "AccNumber", "CustID", "Type", "Balance", "Opened"));
        System.out.println(repeat("-", 65));
        for (Account account : allAccounts) {
            System.out.println(account);
        }
        System.out.println();
    }

    private void showReport() {
        System.out.println();
        System.out.println(bankService.generateSummaryReport());
        System.out.println();
    }

    private String prompt(String label) {
        System.out.print(label);
        return scanner.nextLine();
    }

    /**
     * Builds a repeated-character separator line. Avoids relying on the
     * Java 11+ String.repeat() method so this compiles on older JDKs too.
     */
    private String repeat(String s, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(s);
        }
        return sb.toString();
    }
}
