# Library Management System

This is a Java Swing-based desktop application that helps manage books in a library. It allows users to add, view, delete, issue, and return books. The system uses *MySQL* for the backend database and *JDBC* for database connectivity.

---

## Features

- *Login System:* Authenticate users before accessing the system.
- *Add Book:* Insert new book details into the database.
- *View Books:* Display a list of all books in the library.
- *Delete Book:* Remove books using their ID.
- *Issue Book:* Decrease quantity when a book is issued.
- *Return Book:* Increase quantity when a book is returned.
- *Exit Option:* Close the application safely.

---

## Technologies Used

- Java (JDK 17+)
- Java Swing (GUI)
- MySQL
- JDBC (Java Database Connectivity)
- IntelliJ IDEA (Recommended IDE)

---

## Database Configuration

1. Create a MySQL database named Librarydb.
2. Create the following tables:

```sql
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50),
    password VARCHAR(50)
);

CREATE TABLE books (
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(100),
    author VARCHAR(100),
    quantity INT
);
## Screenshots

### 1. Add Book
![Add Book](screenshots/addbook.png)

### 2. View Books
![View Books](screenshots/viewbooks.png)

### 3. Issue Book
![Issue Book](screenshots/issuebook.png)

### 4. Return Book
![Return Book](screenshots/returnbook.png)

### 5. Delete Book
![Delete Book](screenshots/deletebook.png)