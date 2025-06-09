💸 ExpenseTrackerUsingJava

A user-friendly, desktop-based Expense Tracker built using Java Swing for the UI and MySQL for storing expense data. This project helps users log and manage their daily expenses with categorized tracking and timestamps — all in a lightweight and intuitive design.


![WhatsApp Image 2025-06-08 at 18 21 26_93bb856e](https://github.com/user-attachments/assets/6be467aa-52dc-4dbb-b1e6-7f8fe487aea1)

🚀 Features

Add new expense entries with category and description

View all past expenses

Timestamp and date support

Minimal, clean UI using Java Swing

Persistent data storage using MySQL and JDBC


🛠️ Tech Stack

Tech	Description
Java	Core application logic
Swing	GUI framework
MySQL	Backend database
JDBC	Database connectivity
.jar	MySQL Connector (manual lib import)

📁 Project Structure (Explained in Words)

The project is organized into the following main folders and files:

src/
This is the root source folder containing all the Java source files, organized into packages:

dao/ – This package contains the data access layer:

ExpenseDAO.java – Contains methods to perform database operations like insert, update, delete, and fetch expenses.

ExpenseDAO.class – The compiled version of ExpenseDAO.java.

model/ – Contains the business logic or data model:

Expense.java – Defines the Expense object with properties like amount, category, date, etc.

Expense.class – The compiled class file.

ui/ – This package manages the user interface:

ExpenseTrackerUI.java – Handles the GUI (Graphical User Interface) using Java Swing.

ExpenseTrackerUI.class – The compiled UI class.

util/ – Utility classes, mainly for database setup:

DBConnection.java – Provides a reusable method to connect to the MySQL database.

DBConnection.class – The compiled connection class.

Main.java – The main entry point of the program which starts the UI.

Main.class – The compiled version of Main.java.

TestConnection.java – An optional test class to check if the database connection is working.

TestConnection.class – The compiled test class.

lib/

Contains external libraries required for the project.

Specifically: mysql-connector-j-9.3.0.jar – The MySQL JDBC driver used for database connectivity.

Expensetrackeruibased.iml

IntelliJ IDEA project module file, which stores project-specific configurations.



🧩 MySQL Database Setup

Open your SQL tool (e.g., MySQL Workbench).

Run the following SQL:

-- Create the database
CREATE DATABASE IF NOT EXISTS ExpenseTrackerDB;

-- Use the database
USE ExpenseTrackerDB;

-- Create the expenses table
CREATE TABLE IF NOT EXISTS expenses (
    id INT AUTO_INCREMENT PRIMARY KEY,
    amount DECIMAL(10, 2) NOT NULL,
    category VARCHAR(100) NOT NULL,
    description TEXT,
    date DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

In DBConnection.java, update your MySQL credentials:
private static final String URL = "jdbc:mysql://localhost:3306/ExpenseTrackerDB";
private static final String USER = "your_mysql_username";
private static final String PASSWORD = "your_mysql_password";

▶️ How to Compile & Run

1️⃣ Set up classpath

Ensure you reference the MySQL connector in your classpath when compiling and running. Example:

# Compile
javac -cp ".;lib/mysql-connector-j-9.3.0.jar" src/**/*.java

# Run
java -cp ".;lib/mysql-connector-j-9.3.0.jar;src" Main

Use : instead of ; on Linux/macOS:
javac -cp ".:lib/mysql-connector-j-9.3.0.jar" src/**/*.java
java -cp ".:lib/mysql-connector-j-9.3.0.jar:src" Main

✅ Future Enhancements

 Charts and graphical analytics

 Monthly summaries

 User login system

 Export to PDF/CSV

📬 Contact
Nikesh Gupta
GitHub: @nikesh38
Email: nikeshsvm@gmail.com


