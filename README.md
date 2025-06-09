# 📚 Library Management System (Java + MySQL + Swing)

This project is a Java-based Library Management System using *Swing for GUI* and *MySQL for the database. It provides role-based access for **Admin* and *User*, allowing management of books, issuing, returning, and tracking user actions via logs.

---

## 🚀 Features

### Admin
- Add, View, Delete Books
- Issue and Return Books
- View Logs
- Manage Users

### User
- View and Search Books
- Issue and Return Books

---

## 🛠 Tech Stack

- *Language*: Java
- *GUI*: Java Swing
- *Database*: MySQL (JDBC)
- *IDE*: IntelliJ / Eclipse / NetBeans
- *Version Control*: Git & GitHub

---

## 🗃 Database Schema

- users: stores user credentials and roles
- - books: holds book details.
- issued_books: tracks book issuing and returns.
- logs: records all user actions (login, add, issue, return, etc.)

See [library_schema.sql](./library_schema.sql) for full database setup.

---

## ⚙ Setup Instructions

### 1. Prerequisites

- Java JDK 8 or higher
- MySQL Server
- MySQL JDBC Driver (mysql-connector-java)
- IDE (e.g., IntelliJ, Eclipse)
- Git

## Screenshots

### 1. Add Book
![Add Book](screenshots/addbookUI.png)

### 2. View Books
![View Books](screenshots/viewbooksUI.png)

### 3. Issue Book
![Issue Book](screenshots/issuebookUI.png)

### 4. Return Book
![Return Book](screenshots/returnbookUI.png)

### 5. Delete Book
![Delete Book](screenshots/deletebookUI.png)