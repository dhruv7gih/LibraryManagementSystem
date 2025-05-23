import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LibrarySystemGUI extends JFrame {

    Connection conn;

    public LibrarySystemGUI() {
        // DB connection
        try {
            conn = DBConnection.getConnection();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "DB Connection Error: " + e.getMessage());
            System.exit(1);
        }

        // GUI Setup
        setTitle("Library Management System");
        setSize(400, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Buttons
        JButton loginBtn = new JButton("Login");
        JButton addBookBtn = new JButton("Add Book");
        JButton viewBooksBtn = new JButton("View Books");
        JButton deleteBookBtn = new JButton("Delete Book");
        JButton issueBookBtn = new JButton("Issue Book");
        JButton returnBookBtn = new JButton("Return Book");
        JButton exitBtn = new JButton("Exit");

        // Button Actions
        loginBtn.addActionListener(e -> login());
        addBookBtn.addActionListener(e -> addBook());
        viewBooksBtn.addActionListener(e -> viewBooks());
        deleteBookBtn.addActionListener(e -> deleteBook());
        issueBookBtn.addActionListener(e -> issueBook());
        returnBookBtn.addActionListener(e -> returnBook());
        exitBtn.addActionListener(e -> System.exit(0));

        // Layout
        setLayout(new GridLayout(7, 1, 10, 10));
        add(loginBtn);
        add(addBookBtn);
        add(viewBooksBtn);
        add(deleteBookBtn);
        add(issueBookBtn);
        add(returnBookBtn);
        add(exitBtn);

        setVisible(true);
    }

    void login() {
        String username = JOptionPane.showInputDialog(this, "Enter Username:");
        String password = JOptionPane.showInputDialog(this, "Enter Password:");

        try {
            PreparedStatement pst = conn.prepareStatement("SELECT * FROM users WHERE username=? AND password=?");
            pst.setString(1, username);
            pst.setString(2, password);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Login successful!");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Login Error: " + e.getMessage());
        }
    }

    void addBook() {
        String title = JOptionPane.showInputDialog(this, "Enter Book Title:");
        String author = JOptionPane.showInputDialog(this, "Enter Book Author:");
        String qtyStr = JOptionPane.showInputDialog(this, "Enter Quantity:");

        try {
            int quantity = Integer.parseInt(qtyStr);
            PreparedStatement pst = conn.prepareStatement("INSERT INTO books (title, author, quantity) VALUES (?, ?, ?)");
            pst.setString(1, title);
            pst.setString(2, author);
            pst.setInt(3, quantity);
            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "Book added successfully!");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid quantity. Enter a number.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Add Book Error: " + e.getMessage());
        }
    }
    void viewBooks() {
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM books");

            StringBuilder sb = new StringBuilder("ID\tTitle\tAuthor\tQty\n");
            while (rs.next()) {
                sb.append(rs.getInt("id")).append("\t")
                        .append(rs.getString("title")).append("\t")
                        .append(rs.getString("author")).append("\t")
                        .append(rs.getInt("quantity")).append("\n");
            }

            JTextArea textArea = new JTextArea(sb.toString());
            textArea.setEditable(false);
            JOptionPane.showMessageDialog(this, new JScrollPane(textArea), "Books List", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "View Books Error: " + e.getMessage());
        }
    }

    void deleteBook() {
        String idStr = JOptionPane.showInputDialog(this, "Enter Book ID to delete:");

        try {
            int id = Integer.parseInt(idStr);
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this book?", "Confirm Delete", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                PreparedStatement pst = conn.prepareStatement("DELETE FROM books WHERE id=?");
                pst.setInt(1, id);
                int rows = pst.executeUpdate();

                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Book deleted successfully.");
                } else {
                    JOptionPane.showMessageDialog(this, "Book ID not found.");
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid ID. Enter a number.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Delete Book Error: " + e.getMessage());
        }
    }

    void issueBook() {
        String bookIdStr = JOptionPane.showInputDialog(this, "Enter Book ID to issue:");

        try {
            int bookId = Integer.parseInt(bookIdStr);
            String query = "UPDATE books SET quantity = quantity - 1 WHERE id = ? AND quantity > 0";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, bookId);
            int rows = pst.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Book issued successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Book not available or invalid ID.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid ID. Enter a number.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Issue Book Error: " + e.getMessage());
        }
    }

    void returnBook() {
        String bookIdStr = JOptionPane.showInputDialog(this, "Enter Book ID to return:");

        try {
            int bookId = Integer.parseInt(bookIdStr);
            String query = "UPDATE books SET quantity = quantity + 1 WHERE id = ?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, bookId);
            int rows = pst.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Book returned successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Book ID.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid ID. Enter a number.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Return Book Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new LibrarySystemGUI();
    }
}