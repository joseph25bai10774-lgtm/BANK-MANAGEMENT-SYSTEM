# BANK-MANAGEMENT-SYSTEM
A desktop banking and ATM simulation system built with Java Swing and local file persistence.


# National Bank

## ATM and Banking Management System

Prosperity National Bank is a desktop banking application developed using Java Swing.

I created this project to practice Java by building something more practical than a basic console application. The project includes common banking operations such as creating an account, logging in, depositing and withdrawing money, checking the balance, viewing transactions, and changing the PIN.

The application also saves account data locally, so the information can be accessed again when the program is restarted.

## Features

* Create a new bank account
* Generate an account number
* Login using account number and PIN
* Deposit money
* Withdraw money
* Quick cash withdrawal
* Check account balance
* View recent transactions
* Change account PIN
* Save account and transaction data locally

## How It Works

The application starts with the login and account creation options.
After creating an account, the user receives an account number and can use it along with their PIN to log in.
After login, the main dashboard provides access to the banking operations.

```text
Login
  |
  v
Dashboard
  |
  +-- Deposit
  |
  +-- Withdraw
  |
  +-- Quick Cash
  |
  +-- Balance Enquiry
  |
  +-- Mini Statement
  |
  +-- Change PIN
```

Whenever a deposit or withdrawal is made, the account balance is updated and the transaction is stored.

## Data Storage

The project uses Java Serialization to save the account information locally.
The data is stored in:

```text
bank_data.ser
```

This means the account information can be loaded again when the application is opened.
No external database is required for the current version.

## Technologies Used

* Java
* Java Swing
* Java AWT
* Object-Oriented Programming
* File Handling
* Java Serialization
* Singleton Design Pattern

## Project Structure

```text
Prosperity-National-Bank/
│
├── src/
│   ├── BankManagementSystem.java
│   ├── BankDataStore.java
│   ├── BankAccount.java
│   ├── Transaction.java
│   └── Other Java Classes
│
├── screenshots/
│
├── bank_data.ser
├── README.md
└── .gitignore
```

The structure may vary depending on the version of the project.

## Requirements

* JDK 8 or later
* Any Java IDE such as IntelliJ IDEA, Eclipse, NetBeans, or VS Code

## How to Run

Clone the repository:

```bash
git clone https://github.com/YOUR_USERNAME/bank-management-system.git
```
Open the project in a Java IDE.

Find the main class:

```text
BankManagementSystem.java
```
Run the main class to start the application.
If running from the terminal:

```bash
javac *.java
java BankManagementSystem
```

## What I Learned

While working on this project, I got practical experience with Java Swing, event handling, classes and objects, file handling, serialization, collections, and exception handling.

I also learned how different parts of a Java application can be connected together to create a complete working program.

## Future Improvements

Some features I would like to add in future versions are:

* MySQL database integration
* Admin dashboard
* Better authentication and security
* ATM card management
* UPI and QR payment support
* PDF bank statements
* Improved transaction history
* Better UI and user experience
