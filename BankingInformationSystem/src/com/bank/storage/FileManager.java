package com.bank.storage;

import com.bank.model.Account;
import com.bank.model.Customer;
import com.bank.model.Transaction;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Handles all reading and writing of application data to disk.
 * Data is stored as simple pipe-delimited text files under the "data"
 * directory, which is created automatically (with sample data) on first run.
 */
public class FileManager {

    private static final String DATA_DIR = "data";
    private static final String CUSTOMERS_FILE = DATA_DIR + File.separator + "customers.txt";
    private static final String ACCOUNTS_FILE = DATA_DIR + File.separator + "accounts.txt";
    private static final String TRANSACTIONS_FILE = DATA_DIR + File.separator + "transactions.txt";
    private static final String CREDENTIALS_FILE = DATA_DIR + File.separator + "credentials.txt";

    public FileManager() {
        ensureDataFiles();
    }

    /**
     * Creates the data directory and any missing data files, seeding them
     * with sample data so the application is usable immediately after setup.
     */
    private void ensureDataFiles() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        createIfMissing(CREDENTIALS_FILE, defaultCredentials());
        createIfMissing(CUSTOMERS_FILE, defaultCustomers());
        createIfMissing(ACCOUNTS_FILE, defaultAccounts());
        createIfMissing(TRANSACTIONS_FILE, defaultTransactions());
    }

    private void createIfMissing(String path, List<String> defaultLines) {
        File file = new File(path);
        if (!file.exists()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (String line : defaultLines) {
                    writer.write(line);
                    writer.newLine();
                }
            } catch (IOException e) {
                System.out.println("Warning: could not create file " + path + " -> " + e.getMessage());
            }
        }
    }

    private List<String> defaultCredentials() {
        List<String> lines = new ArrayList<>();
        lines.add("admin|admin123");
        return lines;
    }

    private List<String> defaultCustomers() {
        List<String> lines = new ArrayList<>();
        lines.add("CUST1001|Rohit Sharma|rohit.sharma@example.com|9876543210|Delhi, India");
        lines.add("CUST1002|Anjali Verma|anjali.verma@example.com|9123456780|Mumbai, India");
        return lines;
    }

    private List<String> defaultAccounts() {
        List<String> lines = new ArrayList<>();
        lines.add("ACC100001|CUST1001|SAVINGS|15000.0|2026-01-10");
        lines.add("ACC100002|CUST1002|CURRENT|50000.0|2026-02-15");
        return lines;
    }

    private List<String> defaultTransactions() {
        List<String> lines = new ArrayList<>();
        lines.add("TXN1000001|ACC100001|DEPOSIT|15000.0|15000.0|2026-01-10 10:00:00|Initial deposit");
        lines.add("TXN1000002|ACC100002|DEPOSIT|50000.0|50000.0|2026-02-15 11:30:00|Initial deposit");
        return lines;
    }

    public Map<String, String> loadCredentials() {
        Map<String, String> credentials = new HashMap<>();
        for (String line : readLines(CREDENTIALS_FILE)) {
            if (line.trim().isEmpty()) {
                continue;
            }
            String[] parts = line.split("\\|", -1);
            if (parts.length == 2) {
                credentials.put(parts[0], parts[1]);
            }
        }
        return credentials;
    }

    public Map<String, Customer> loadCustomers() {
        Map<String, Customer> customers = new LinkedHashMap<>();
        for (String line : readLines(CUSTOMERS_FILE)) {
            if (line.trim().isEmpty()) {
                continue;
            }
            Customer customer = Customer.fromFileString(line);
            customers.put(customer.getCustomerId(), customer);
        }
        return customers;
    }

    public void saveCustomers(Collection<Customer> customers) {
        List<String> lines = new ArrayList<>();
        for (Customer customer : customers) {
            lines.add(customer.toFileString());
        }
        writeLines(CUSTOMERS_FILE, lines);
    }

    public Map<String, Account> loadAccounts() {
        Map<String, Account> accounts = new LinkedHashMap<>();
        for (String line : readLines(ACCOUNTS_FILE)) {
            if (line.trim().isEmpty()) {
                continue;
            }
            Account account = Account.fromFileString(line);
            accounts.put(account.getAccountNumber(), account);
        }
        return accounts;
    }

    public void saveAccounts(Collection<Account> accounts) {
        List<String> lines = new ArrayList<>();
        for (Account account : accounts) {
            lines.add(account.toFileString());
        }
        writeLines(ACCOUNTS_FILE, lines);
    }

    public List<Transaction> loadTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        for (String line : readLines(TRANSACTIONS_FILE)) {
            if (line.trim().isEmpty()) {
                continue;
            }
            transactions.add(Transaction.fromFileString(line));
        }
        return transactions;
    }

    public void saveTransactions(List<Transaction> transactions) {
        List<String> lines = new ArrayList<>();
        for (Transaction transaction : transactions) {
            lines.add(transaction.toFileString());
        }
        writeLines(TRANSACTIONS_FILE, lines);
    }

    private List<String> readLines(String path) {
        List<String> lines = new ArrayList<>();
        File file = new File(path);
        if (!file.exists()) {
            return lines;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.out.println("Warning: could not read file " + path + " -> " + e.getMessage());
        }
        return lines;
    }

    private void writeLines(String path, List<String> lines) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Warning: could not write file " + path + " -> " + e.getMessage());
        }
    }
}
