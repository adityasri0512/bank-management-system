# Banking Information System (Core Java)

A console-based Banking Information System built in Core Java, developed as an
internship project. It demonstrates Object-Oriented Programming, Collections,
File Handling, Exception Handling, and Input Validation using a clean,
layered package structure — no external libraries or frameworks required.

## Overview

The application simulates a small bank's back-office system: staff log in,
manage customers and their accounts, and process deposits, withdrawals,
transfers, and balance/history enquiries. All data is automatically saved to
and loaded from plain text files, so nothing is lost between runs.

## Technologies

- **Language:** Core Java (JDK 8 or higher — no external dependencies)
- **Persistence:** Plain text files (pipe-delimited), via `java.io`
- **Data structures:** `HashMap`, `LinkedHashMap`, `ArrayList` (`java.util`)
- **Date/Time:** `java.time` (`LocalDateTime`, `DateTimeFormatter`)

## Folder Structure

```
BankingInformationSystem/
├── src/
│   └── com/bank/
│       ├── Main.java                          # Application entry point
│       ├── model/
│       │   ├── Customer.java                  # Customer data model
│       │   ├── Account.java                   # Account data model
│       │   └── Transaction.java                # Transaction data model
│       ├── service/
│       │   ├── AuthenticationService.java      # Staff login logic
│       │   └── BankService.java                 # Core banking business logic
│       ├── storage/
│       │   └── FileManager.java                 # File persistence (load/save)
│       ├── util/
│       │   ├── InputValidator.java              # Input validation rules
│       │   ├── IdGenerator.java                  # Unique ID generation
│       │   ├── CustomException.java              # Base checked exception
│       │   ├── InsufficientBalanceException.java
│       │   ├── DuplicateRecordException.java
│       │   ├── RecordNotFoundException.java
│       │   └── InvalidInputException.java
│       └── ui/
│           └── Menu.java                          # Console menu / UI layer
├── data/                                          # Auto-created on first run
│   ├── credentials.txt
│   ├── customers.txt
│   ├── accounts.txt
│   └── transactions.txt
└── README.md
```

## Features

- **Authentication** — staff login with 3 attempts before the app exits
- **Customer Management** — add, update, delete, search, and list customers
- **Account Management** — create accounts (SAVINGS/CURRENT), delete empty accounts
- **Deposit / Withdraw** — with balance and amount validation
- **Fund Transfer** — atomic transfer between two accounts with transaction logging
- **Balance Enquiry** — instant lookup by account number
- **Transaction History** — full, timestamped history per account
- **Search** — keyword search across customers and accounts
- **Reports** — bank-wide summary (totals, balances, account-type breakdown)
- **File Persistence** — automatic load on startup, save on exit
- **Sample Data** — two demo customers/accounts are seeded automatically on first run
- **Input Validation** — names, emails, phone numbers, amounts, menu choices, and
  account types are all validated, with clear error messages
- **Custom Exceptions** — a dedicated exception hierarchy
  (`InsufficientBalanceException`, `DuplicateRecordException`,
  `RecordNotFoundException`, `InvalidInputException`) for precise, meaningful
  error handling instead of generic exceptions

## Compilation Instructions

From the project root directory:

```bash
# Compile all source files into a bin/ directory
javac -d bin $(find src -name "*.java")
```

On Windows (PowerShell), use instead:

```powershell
javac -d bin (Get-ChildItem -Recurse -Filter *.java -Path src).FullName
```

## Execution Steps

```bash
# Run from the project root so the data/ folder is created alongside it
java -cp bin com.bank.Main
```

### Default Login

The first run automatically creates `data/credentials.txt` with a default
staff account:

```
Username: admin
Password: admin123
```

You can add more username/password lines to `data/credentials.txt`
(pipe-delimited: `username|password`) to create additional staff logins.

### Sample Data

On first run, two sample customers and their accounts are created
automatically so you can immediately explore deposits, withdrawals,
transfers, and history without manual setup:

| Customer ID | Name          | Account Number | Type    | Balance   |
|-------------|---------------|-----------------|---------|-----------|
| CUST1001    | Rohit Sharma  | ACC100001       | SAVINGS | 15,000.00 |
| CUST1002    | Anjali Verma  | ACC100002       | CURRENT | 50,000.00 |

## Screenshots

_Add screenshots of the running application here, for example:_
- Login screen
- Main menu
- Add customer / create account flow
- Deposit / withdraw / transfer in action
- Transaction history output
- Bank summary report

## Future Enhancements

- Password hashing instead of plaintext credential storage
- Role-based access control (teller vs. administrator)
- Interest calculation for savings accounts
- Export transaction history and reports to CSV/PDF
- Migrate persistence from flat files to a relational database (JDBC)
- Unit tests with JUnit for the service layer
- REST API / GUI front end (Swing or JavaFX) built on top of the existing
  service layer, which is UI-agnostic by design

## Notes

- The project intentionally uses plain-text file storage instead of a database
  to keep the internship scope focused on Core Java fundamentals (OOP,
  Collections, File I/O, Exception Handling) without external dependencies.
- All business rules live in `BankService`; the `Menu` class is a thin UI
  layer, which keeps the codebase easy to extend (e.g., swapping in a GUI
  later would not require touching business logic).
