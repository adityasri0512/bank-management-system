package com.bank;

import com.bank.service.AuthenticationService;
import com.bank.service.BankService;
import com.bank.storage.FileManager;
import com.bank.ui.Menu;

/**
 * Application entry point.
 * Wires together the storage, service, and UI layers and starts the console menu.
 *
 * Default login (created automatically on first run in data/credentials.txt):
 *   Username: admin
 *   Password: admin123
 */
public class Main {

    public static void main(String[] args) {
        FileManager fileManager = new FileManager();
        BankService bankService = new BankService(fileManager);
        AuthenticationService authService = new AuthenticationService(fileManager.loadCredentials());

        Menu menu = new Menu(bankService, authService);
        menu.start();
    }
}
