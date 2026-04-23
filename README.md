# Library Management System (Java + JDBC)

## 📌 Description
A console-based Library Management System built using Java and JDBC.  
The application allows users to manage book records with basic CRUD operations.

---

## 🚀 Features
- Add new books
- View all books
- Update book quantity
- Delete books
- Custom exception handling for duplicate and missing books

---

## 🛠 Tech Stack
- Java
- JDBC (Java Database Connectivity)
- MySQL

---

## ⚙️ Setup Instructions

### 1. Create Database
```sql
CREATE DATABASE library;

USE library;

CREATE TABLE books (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255),
    author VARCHAR(255),
    quantity INT
);
```

### 2. Configure Database Credentials
Update these in LibraryManagement.java:

private static final String DB_USER = "root";
private static final String DB_PASSWORD = "your_password_here";

### 3. Add MySQL Connector
Download MySQL Connector/J and add the .jar file to your project.

### 4. Compile & Run
javac -cp ".;mysql-connector-j-9.x.x.jar" LibraryManagement.java
java -cp ".;mysql-connector-j-9.x.x.jar;." LibraryManagement


###⚠️ Important Note
This project requires a local MySQL setup.
Make sure MySQL server is running and credentials are correct.

📚 Concepts Used
JDBC (Connection, PreparedStatement, ResultSet)
Exception Handling
CRUD Operations
Basic Database Design
📌 Future Improvements
Convert to Spring Boot REST API
Add GUI (JavaFX / Web UI)
Improve project structure (DAO, Service layers)
