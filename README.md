# SQL Injection Testing Automation

This project demonstrates automated testing for common SQL injection attacks using Java and SQLite.

## 📁 Files

- `SQL_inject_automation.java`: Main Java program that tests multiple SQL injection payloads against both vulnerable and safe login implementations.
- `safelogin.sql`: SQL template using parameterized placeholders (`?`) for secure login.
- `vulnlogin.sql`: SQL template using raw string substitution for insecure login.
- `users.db`: SQLite database file containing a sample `users` table.
- `CreateUser.py`: Create users.db
- `VulnerableLogin.java`: Log in form using vulnerable login
- `SafeLogin.java`: Log in form using safe login

## ✅ How It Works

- Reads predefined SQL injection payloads.
- Tests them against both a **vulnerable** and a **secure** SQL login query.
- Displays the result of each login attempt to show whether the injection was successful.

## 🧪 Observations

- SQL injection works by inserting malicious SQL code into a query through user input. In the vulnerable login, the program directly adds what the user types into the SQL command. This means if the user types something like `' OR 1=1 --`, it can change the meaning of the query and trick the system into logging in without the correct password. This is the developer's fault for trusting user input; by adding the input value directly into the code, the input works as code rather than just data.

- The safe login fixes this by using `?` placeholders in the SQL command. These placeholders are filled with the user’s input, but the input is treated only as data—not as part of the SQL code. This prevents the database from being tricked, no matter what the user types.

## 🔧 Setup Instructions

1. Make sure you have `sqlite-jdbc` added to your classpath.
2. Compile with:

   ```bash
   javac -cp ".;sqlite-jdbc-<version>.jar" SQL_inject_automation.java
