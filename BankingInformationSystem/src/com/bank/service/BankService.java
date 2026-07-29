package com.bank.service;

import com.bank.model.Account;
import com.bank.model.Customer;
import com.bank.model.Transaction;
import com.bank.storage.FileManager;
import com.bank.util.CustomException;
import com.bank.util.DuplicateRecordException;
import com.bank.util.IdGenerator;
import com.bank.util.InputValidator;
import com.bank.util.InsufficientBalanceException;
import com.bank.util.InvalidInputException;
import com.bank.util.RecordNotFoundException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Core business-logic layer of the Banking Information System.
 * Owns all customer, account, and transaction data in memory (loaded from
 * FileManager at startup) and enforces every banking business rule.
 * The UI layer never touches raw data structures directly; it always
 * goes through this service.
 */
public class BankService {

    private final FileManager fileManager;
    private final Map<String, Customer> customers;
    private final Map<String, Account> accounts;
    private final List<Transaction> transactions;

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public BankService(FileManager fileManager) {
        this.fileManager = fileManager;
        this.customers = fileManager.loadCustomers();
        this.accounts = fileManager.loadAccounts();
        this.transactions = fileManager.loadTransactions();
        initializeIdCounters();
    }

    /**
     * Seeds the ID generator counters from the highest IDs already present
     * in the loaded data, so newly generated IDs never collide with existing ones.
     */
    private void initializeIdCounters() {
        int maxCustomer = 1000;
        int maxAccount = 100000;
        int maxTxn = 1000000;

        for (String id : customers.keySet()) {
            maxCustomer = Math.max(maxCustomer, extractNumber(id));
        }
        for (String id : accounts.keySet()) {
            maxAccount = Math.max(maxAccount, extractNumber(id));
        }
        for (Transaction transaction : transactions) {
            maxTxn = Math.max(maxTxn, extractNumber(transaction.getTransactionId()));
        }

        IdGenerator.initCustomerCounter(maxCustomer);
        IdGenerator.initAccountCounter(maxAccount);
        IdGenerator.initTransactionCounter(maxTxn);
    }

    private int extractNumber(String id) {
        try {
            return Integer.parseInt(id.replaceAll("[^0-9]", ""));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    // ==================== Customer Management ====================

    public Customer addCustomer(String name, String email, String phone, String address)
            throws InvalidInputException, DuplicateRecordException {
        InputValidator.validateName(name);
        InputValidator.validateEmail(email);
        InputValidator.validatePhone(phone);
        InputValidator.validateAddress(address);

        for (Customer existing : customers.values()) {
            if (existing.getEmail().equalsIgnoreCase(email.trim())) {
                throw new DuplicateRecordException("A customer with this email already exists.");
            }
        }

        String customerId = IdGenerator.nextCustomerId();
        Customer customer = new Customer(customerId, name.trim(), email.trim(), phone.trim(), address.trim());
        customers.put(customerId, customer);
        return customer;
    }

    public Customer updateCustomer(String customerId, String name, String email, String phone, String address)
            throws RecordNotFoundException, InvalidInputException {
        Customer customer = getCustomerOrThrow(customerId);
        InputValidator.validateName(name);
        InputValidator.validateEmail(email);
        InputValidator.validatePhone(phone);
        InputValidator.validateAddress(address);

        customer.setName(name.trim());
        customer.setEmail(email.trim());
        customer.setPhone(phone.trim());
        customer.setAddress(address.trim());
        return customer;
    }

    public void deleteCustomer(String customerId) throws RecordNotFoundException, CustomException {
        getCustomerOrThrow(customerId);
        for (Account account : accounts.values()) {
            if (account.getCustomerId().equals(customerId) && account.getBalance() > 0) {
                throw new CustomException("Cannot delete customer: linked account " + account.getAccountNumber()
                        + " still has a non-zero balance. Withdraw funds first.");
            }
        }
        accounts.values().removeIf(account -> account.getCustomerId().equals(customerId));
        customers.remove(customerId);
    }

    public List<Customer> searchCustomers(String keyword) {
        List<Customer> results = new ArrayList<>();
        String lower = keyword == null ? "" : keyword.trim().toLowerCase();
        for (Customer customer : customers.values()) {
            if (customer.getCustomerId().toLowerCase().contains(lower)
                    || customer.getName().toLowerCase().contains(lower)
                    || customer.getEmail().toLowerCase().contains(lower)) {
                results.add(customer);
            }
        }
        return results;
    }

    public List<Customer> getAllCustomers() {
        return new ArrayList<>(customers.values());
    }

    public Customer getCustomerOrThrow(String customerId) throws RecordNotFoundException {
        if (customerId == null) {
            throw new RecordNotFoundException("Customer ID cannot be empty.");
        }
        Customer customer = customers.get(customerId.trim());
        if (customer == null) {
            throw new RecordNotFoundException("No customer found with ID: " + customerId);
        }
        return customer;
    }

    // ==================== Account Management ====================

    public Account createAccount(String customerId, String accountType, double initialDeposit)
            throws RecordNotFoundException, InvalidInputException {
        getCustomerOrThrow(customerId);
        String type = InputValidator.validateAccountType(accountType);
        if (initialDeposit < 0) {
            throw new InvalidInputException("Initial deposit cannot be negative.");
        }

        String accountNumber = IdGenerator.nextAccountNumber();
        String dateOpened = LocalDateTime.now().format(DATE_FORMAT);
        Account account = new Account(accountNumber, customerId, type, initialDeposit, dateOpened);
        accounts.put(accountNumber, account);

        if (initialDeposit > 0) {
            recordTransaction(accountNumber, "DEPOSIT", initialDeposit, initialDeposit, "Initial deposit on account opening");
        }
        return account;
    }

    public void deleteAccount(String accountNumber) throws RecordNotFoundException, CustomException {
        Account account = getAccountOrThrow(accountNumber);
        if (account.getBalance() > 0) {
            throw new CustomException("Cannot delete an account with a non-zero balance. Withdraw funds first.");
        }
        accounts.remove(accountNumber);
    }

    public List<Account> searchAccounts(String keyword) {
        List<Account> results = new ArrayList<>();
        String lower = keyword == null ? "" : keyword.trim().toLowerCase();
        for (Account account : accounts.values()) {
            if (account.getAccountNumber().toLowerCase().contains(lower)
                    || account.getCustomerId().toLowerCase().contains(lower)
                    || account.getAccountType().toLowerCase().contains(lower)) {
                results.add(account);
            }
        }
        return results;
    }

    public List<Account> getAllAccounts() {
        return new ArrayList<>(accounts.values());
    }

    public List<Account> getAccountsForCustomer(String customerId) {
        List<Account> results = new ArrayList<>();
        for (Account account : accounts.values()) {
            if (account.getCustomerId().equals(customerId)) {
                results.add(account);
            }
        }
        return results;
    }

    public Account getAccountOrThrow(String accountNumber) throws RecordNotFoundException {
        if (accountNumber == null) {
            throw new RecordNotFoundException("Account number cannot be empty.");
        }
        Account account = accounts.get(accountNumber.trim());
        if (account == null) {
            throw new RecordNotFoundException("No account found with number: " + accountNumber);
        }
        return account;
    }

    // ==================== Transaction Operations ====================

    public void deposit(String accountNumber, double amount) throws RecordNotFoundException, InvalidInputException {
        Account account = getAccountOrThrow(accountNumber);
        if (amount <= 0) {
            throw new InvalidInputException("Deposit amount must be greater than zero.");
        }
        account.setBalance(account.getBalance() + amount);
        recordTransaction(accountNumber, "DEPOSIT", amount, account.getBalance(), "Cash deposit");
    }

    public void withdraw(String accountNumber, double amount)
            throws RecordNotFoundException, InsufficientBalanceException, InvalidInputException {
        Account account = getAccountOrThrow(accountNumber);
        if (amount <= 0) {
            throw new InvalidInputException("Withdrawal amount must be greater than zero.");
        }
        if (amount > account.getBalance()) {
            throw new InsufficientBalanceException(
                    String.format("Insufficient balance. Available balance: %.2f", account.getBalance()));
        }
        account.setBalance(account.getBalance() - amount);
        recordTransaction(accountNumber, "WITHDRAW", amount, account.getBalance(), "Cash withdrawal");
    }

    public void transferFunds(String fromAccountNumber, String toAccountNumber, double amount)
            throws RecordNotFoundException, InsufficientBalanceException, InvalidInputException {
        if (fromAccountNumber != null && fromAccountNumber.trim().equalsIgnoreCase(
                toAccountNumber == null ? "" : toAccountNumber.trim())) {
            throw new InvalidInputException("Source and destination accounts cannot be the same.");
        }
        Account fromAccount = getAccountOrThrow(fromAccountNumber);
        Account toAccount = getAccountOrThrow(toAccountNumber);
        if (amount <= 0) {
            throw new InvalidInputException("Transfer amount must be greater than zero.");
        }
        if (amount > fromAccount.getBalance()) {
            throw new InsufficientBalanceException(
                    String.format("Insufficient balance for transfer. Available balance: %.2f", fromAccount.getBalance()));
        }

        fromAccount.setBalance(fromAccount.getBalance() - amount);
        toAccount.setBalance(toAccount.getBalance() + amount);

        recordTransaction(fromAccount.getAccountNumber(), "TRANSFER_OUT", amount, fromAccount.getBalance(),
                "Transfer to " + toAccount.getAccountNumber());
        recordTransaction(toAccount.getAccountNumber(), "TRANSFER_IN", amount, toAccount.getBalance(),
                "Transfer from " + fromAccount.getAccountNumber());
    }

    public double balanceEnquiry(String accountNumber) throws RecordNotFoundException {
        return getAccountOrThrow(accountNumber).getBalance();
    }

    public List<Transaction> getTransactionHistory(String accountNumber) throws RecordNotFoundException {
        getAccountOrThrow(accountNumber);
        List<Transaction> history = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (transaction.getAccountNumber().equals(accountNumber.trim())) {
                history.add(transaction);
            }
        }
        return history;
    }

    private void recordTransaction(String accountNumber, String type, double amount, double balanceAfter, String description) {
        String transactionId = IdGenerator.nextTransactionId();
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        transactions.add(new Transaction(transactionId, accountNumber, type, amount, balanceAfter, timestamp, description));
    }

    // ==================== Reports ====================

    public String generateSummaryReport() {
        double totalBalance = 0;
        Map<String, Integer> accountTypeCount = new LinkedHashMap<>();
        for (Account account : accounts.values()) {
            totalBalance += account.getBalance();
            accountTypeCount.merge(account.getAccountType(), 1, Integer::sum);
        }

        StringBuilder report = new StringBuilder();
        report.append("===== BANK SUMMARY REPORT =====\n");
        report.append("Total Customers      : ").append(customers.size()).append("\n");
        report.append("Total Accounts       : ").append(accounts.size()).append("\n");
        report.append("Total Transactions   : ").append(transactions.size()).append("\n");
        report.append(String.format("Total Bank Balance   : %.2f%n", totalBalance));
        for (Map.Entry<String, Integer> entry : accountTypeCount.entrySet()) {
            report.append("  ").append(entry.getKey()).append(" accounts : ").append(entry.getValue()).append("\n");
        }
        report.append("================================");
        return report.toString();
    }

    // ==================== Persistence ====================

    /**
     * Persists all in-memory data (customers, accounts, transactions) back to disk.
     * Should be called before the application exits.
     */
    public void saveAll() {
        fileManager.saveCustomers(customers.values());
        fileManager.saveAccounts(accounts.values());
        fileManager.saveTransactions(transactions);
    }
}
