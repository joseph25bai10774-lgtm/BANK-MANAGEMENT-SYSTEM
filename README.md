# BANK-MANAGEMENT-SYSTEM
A desktop banking and ATM simulation system built with Java Swing and local file persistence.


# README
# 🏦 Prosperity National Bank

## ATM & Banking Management System

**Prosperity National Bank** is a desktop-based ATM and banking management system developed using **Java and Java Swing**.

The main idea behind this project was to create a banking application that looks and works more like a real application instead of a simple Java console program. It provides a graphical interface where users can create an account, log in, deposit and withdraw money, check their balance, view transactions, and change their PIN.

This project was also developed to get practical experience with **Java, Object-Oriented Programming, GUI development, file handling, and data persistence**.

---

## 📖 Project Explanation

In a normal banking application, users need a way to securely access their account and perform different banking operations.

This project tries to simulate that process in a simple desktop environment.

When the application starts, the user can either create a new account or log in to an existing account. After successful login, the user is taken to the main banking dashboard.

From the dashboard, different operations can be performed.

For example:

```text
Login
  ↓
Banking Dashboard
  ↓
Choose an Operation
  ├── Deposit
  ├── Withdraw
  ├── Quick Cash
  ├── Balance Enquiry
  ├── Mini Statement
  └── Change PIN
```

After every transaction, the account balance is updated and the information is saved locally.

---

# 🎯 Why I Built This Project

I built this project mainly to understand how the concepts I learned in Java can be used to create an actual application.

Instead of working only with:

```text
System.out.println()
```

I wanted to create something with a proper graphical interface and real interaction.

While developing this project, I focused on:

* Creating a clean banking interface
* Understanding Java Swing
* Applying OOP concepts
* Handling user input and validation
* Managing account information
* Recording transactions
* Saving data between application sessions

It also helped me understand how different Java classes can work together to build one complete application.

---

# ✨ Features

## 👤 1. Create New Account

New users can create their own bank account by entering basic details such as:

* Name
* Date of Birth
* Mobile Number
* 4-digit PIN
* Initial Deposit

The application generates an account number for the newly created account.

The minimum initial deposit is **₹500**.

---

## 🔐 2. Secure Login

Existing users can log in using their:

* Account Number
* 4-digit PIN

The system checks the entered information before allowing access to the banking dashboard.

---

## 💰 3. Deposit Money

Users can add money to their account through the deposit option.

After a successful deposit:

```text
Old Balance
     ↓
Deposit Amount
     ↓
New Balance
```

The transaction is also added to the account's transaction history.

---

## 💸 4. Withdraw Money

Users can withdraw money from their account.

Before completing the transaction, the application checks whether the account has enough balance.

For example:

```text
Available Balance = ₹5000
Withdrawal        = ₹1000

Remaining Balance = ₹4000
```

If the requested amount is greater than the available balance, the withdrawal is rejected.

---

## ⚡ 5. Quick Cash

The Quick Cash option allows users to withdraw commonly used amounts without manually entering the amount every time.

This is designed to make the ATM-style interface faster and easier to use.

---

## 💳 6. Balance Enquiry

Users can check their current account balance whenever they want.

The balance is updated immediately after deposits and withdrawals.

---

## 🧾 7. Mini Statement

The Mini Statement section displays recent transactions made through the account.

It can show information such as:

```text
Transaction Type
Amount
Date
Time
Updated Balance
```

The system keeps the most recent transactions so users can quickly review their account activity.

---

## 🔑 8. Change PIN

Users can change their existing PIN from the banking dashboard.

The system first verifies the current PIN before allowing a new PIN to be created.

The PIN must follow the application's 4-digit requirement.

---

# 💾 Data Persistence

One of the important parts of this project is that the account information does not disappear when the application is closed.

Instead of using an external database, this project uses **Java Object Serialization**.

The information is stored in:

```text
bank_data.ser
```

When the application starts again, the saved information can be loaded from the file.

The basic process is:

```text
Application Starts
       ↓
Read bank_data.ser
       ↓
Load Existing Accounts
       ↓
User Performs Transaction
       ↓
Update Account Data
       ↓
Save Data to bank_data.ser
```

This makes the project easier to run because a separate database server is not required.

---

# 🏗️ How the Application Works

The project can be divided into a few main parts.

### 1. User Interface

The GUI is created using **Java Swing and AWT**.

It contains windows, buttons, text fields, labels, dialogs, and other components required for interacting with the application.

### 2. Account Management

The account-related classes handle information such as:

* Account number
* Customer name
* Date of birth
* Mobile number
* PIN
* Balance

### 3. Transaction Management

Whenever the user deposits or withdraws money, a transaction record is created.

This information is later used by the Mini Statement feature.

### 4. Data Storage

`BankDataStore` is responsible for managing the application's stored banking information.

The Singleton approach is used so that the application can work with a centralized data store.

### 5. File Storage

Java Serialization is used to save and load the data from:

```text
bank_data.ser
```

---

# 🛠️ Technologies Used

| Technology        | Purpose                         |
| ----------------- | ------------------------------- |
| Java              | Main programming language       |
| Java Swing        | Graphical User Interface        |
| Java AWT          | GUI components and layouts      |
| OOP               | Organizing application logic    |
| Serialization     | Saving application data         |
| File Handling     | Reading and writing stored data |
| Singleton Pattern | Centralized data management     |

---

# 📂 Project Structure

A possible project structure is:

```text
BankManagementSystem/
│
├── src/
│   ├── BankManagementSystem.java
│   ├── BankDataStore.java
│   ├── BankAccount.java
│   ├── Transaction.java
│   └── Other Java Classes
│
├── screenshots/
│   ├── login.png
│   ├── account-creation.png
│   ├── dashboard.png
│   ├── deposit.png
│   ├── withdrawal.png
│   └── statement.png
│
├── bank_data.ser
├── README.md
└── .gitignore
```

The exact file names may be different depending on the final version of the source code.

---

# 💻 Requirements

To run the project, you need:

* **JDK 8 or later**
* Any Java-supported operating system
* A Java IDE or terminal

You can use:

* IntelliJ IDEA
* Eclipse
* NetBeans
* Visual Studio Code

No external database is required.

---

# 🚀 How to Run the Project

## Step 1 — Clone the Repository

```bash
git clone https://github.com/YOUR_USERNAME/bank-management-system.git
```

## Step 2 — Open the Project

Open the downloaded project in your preferred Java IDE.

## Step 3 — Locate the Main Class

Find:

```text
BankManagementSystem.java
```

## Step 4 — Run the Program

Run the main class from your IDE.

If using the terminal, compile the Java files and run the main class:

```bash
javac *.java
java BankManagementSystem
```

---

# 🖼️ Screenshots

Screenshots can be added to show the different parts of the application.

For example:

### Login Screen

```markdown
![Login Screen](screenshots/login.png)
```

### Dashboard

```markdown
![Dashboard](screenshots/dashboard.png)
```

### Deposit

```markdown
![Deposit](screenshots/deposit.png)
```

### Withdrawal

```markdown
![Withdrawal](screenshots/withdrawal.png)
```

### Mini Statement

```markdown
![Mini Statement](screenshots/statement.png)
```

---

# 🧠 Java Concepts Used

This project helped me practice several important Java concepts.

### Object-Oriented Programming

Classes and objects are used to represent accounts, transactions, and other parts of the banking system.

### Encapsulation

Account-related information is managed through class methods instead of directly accessing everything from the GUI.

### Collections

Collections are used to manage multiple accounts and transaction records.

### Exception Handling

Input errors and invalid operations are handled to prevent the application from unexpectedly stopping.

### File Handling

Files are used to store the serialized banking data.

### Serialization

Java's `Serializable` interface is used to save objects and load them again later.

### Singleton Pattern

The `BankDataStore` class uses the Singleton concept to provide centralized access to the application's data.

---

# 🔮 Future Improvements

This project can be improved further by adding features such as:

* MySQL or PostgreSQL database
* Admin login and dashboard
* Better encryption for sensitive information
* ATM card management
* UPI/QR payment support
* PDF transaction statements
* Email or SMS notifications
* Advanced transaction history
* Account search and management
* Improved authentication system
* Online banking functionality

---

# ⚠️ Disclaimer

This project is made for **educational and learning purposes**.

It is a simulation of a banking system and is **not connected to any real bank or financial service**.

The current version stores data locally using Java Serialization, so it should not be used for storing real banking or sensitive financial information.

---

# 👨‍💻 About the Developer

**Joseph Binson**

B.Tech CSE — Artificial Intelligence & Machine Learning

I created this project as part of my journey of learning **Java, OOP, GUI development, and software application development**.

The project started as a simple banking application and was gradually improved with a graphical interface, transaction management, PIN management, and persistent local storage.

---

# 📌 Project Status

**Status:** Completed / Educational Project

More features and improvements can be added in future versions.

---

## ⭐ If you like the project

If you find this project useful for learning Java or Swing, feel free to **star ⭐ the repository** and explore the source code.

**Built with Java ☕ and lots of learning.**
