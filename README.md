# BANK-MANAGEMENT-SYSTEM
A desktop banking and ATM simulation system built with Java Swing and local file persistence.


# README
# National Bank — ATM & Management System

A clean, desktop banking app I built with **Java** and **Java Swing**. It mimics real-world ATM workflows and essential banking features, with full data persistence so your accounts don't disappear when you close the app.

---

## Why I Built This

I wanted to build a practical desktop app that connects GUI design with real object-oriented principles. Instead of just printing outputs to a console, this project focuses on:
- Designing an intuitive, cohesive UI that actually looks good.
- Managing application state properly using OOP design patterns.
- Saving session data locally without needing heavy external database setups.

---

## What It Can Do

- **Open New Accounts:** Sign up with your details (Name, DOB, Mobile), choose a 4-digit PIN, and start with an initial deposit (minimum ₹500 / $500).
- **Secure Logins:** Access your account using your generated account number and PIN.
- **Deposit & Withdraw:** Move money in and out with live balance updates and input checks.
- **Fast Cash:** Quick-withdraw common preset amounts in a single click.
- **Balance Enquiry & Mini Statement:** Check current funds or look through your 10 most recent transactions with exact timestamps.
- **PIN Management:** Update your security PIN directly from the dashboard.
- **No Database Needed:** Everything saves automatically to a local serialized file (`bank_data.ser`) so your data is right there when you relaunch.

---

## Tech Stack

- **Language:** Java (JDK 8+)
- **UI:** Java Swing & AWT (custom styled components + native look-and-feel)
- **Data Storage:** Java Object Serialization (`Serializable`)
- **Design Pattern:** Singleton (`BankDataStore` for centralized state management)
