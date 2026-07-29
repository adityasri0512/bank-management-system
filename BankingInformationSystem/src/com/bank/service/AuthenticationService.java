package com.bank.service;

import java.util.Map;

/**
 * Handles staff login authentication against credentials loaded from disk.
 * Default sample credentials are admin / admin123 (see data/credentials.txt).
 */
public class AuthenticationService {

    private final Map<String, String> credentials;

    public AuthenticationService(Map<String, String> credentials) {
        this.credentials = credentials;
    }

    /**
     * Returns true if the supplied username/password pair matches a stored credential.
     */
    public boolean login(String username, String password) {
        if (username == null || password == null) {
            return false;
        }
        String stored = credentials.get(username.trim());
        return stored != null && stored.equals(password);
    }
}
