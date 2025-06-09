import javax.swing.*;
import java.awt.event.*;

public class UserDashboard extends JFrame {
    private String username;
    private String role;

    public UserDashboard(String username, String role) {
        this.username = username;
        this.role = role;

        setTitle("User Dashboard - " + username);
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        JLabel welcomeLabel = new JLabel("Welcome, " + username + " (" + role + ")");
        welcomeLabel.setBounds(20, 20, 300, 30);
        add(welcomeLabel);

        JButton viewBooksBtn = new JButton("View Books");
        viewBooksBtn.setBounds(120, 70, 150, 30);
        add(viewBooksBtn);

        JButton issueBookBtn = new JButton("Issue Book");
        issueBookBtn.setBounds(120, 110, 150, 30);
        add(issueBookBtn);

        JButton returnBookBtn = new JButton("Return Book");
        returnBookBtn.setBounds(120, 150, 150, 30);
        add(returnBookBtn);

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBounds(120, 190, 150, 30);
        add(logoutBtn);

        viewBooksBtn.addActionListener(e -> new ViewBooksUI());
        issueBookBtn.addActionListener(e -> new IssueBookUI());
        returnBookBtn.addActionListener(e -> new ReturnBookUI());
        logoutBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Logged out successfully.");
            dispose();
            new LoginUI();
        });

        setVisible(true);
    }
}