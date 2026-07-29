package com.bank.util;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Generates unique, sequential identifiers for customers, accounts, and
 * transactions. Counters are seeded from the highest existing ID found in
 * the persisted data at startup, so IDs stay unique across application runs.
 */
public final class IdGenerator {

    private static final AtomicInteger customerCounter = new AtomicInteger(1000);
    private static final AtomicInteger accountCounter = new AtomicInteger(100000);
    private static final AtomicInteger transactionCounter = new AtomicInteger(1000000);

    private IdGenerator() {
        // Utility class: prevent instantiation.
    }

    public static void initCustomerCounter(int lastValue) {
        customerCounter.set(Math.max(customerCounter.get(), lastValue));
    }

    public static void initAccountCounter(int lastValue) {
        accountCounter.set(Math.max(accountCounter.get(), lastValue));
    }

    public static void initTransactionCounter(int lastValue) {
        transactionCounter.set(Math.max(transactionCounter.get(), lastValue));
    }

    public static String nextCustomerId() {
        return "CUST" + customerCounter.incrementAndGet();
    }

    public static String nextAccountNumber() {
        return "ACC" + accountCounter.incrementAndGet();
    }

    public static String nextTransactionId() {
        return "TXN" + transactionCounter.incrementAndGet();
    }
}
