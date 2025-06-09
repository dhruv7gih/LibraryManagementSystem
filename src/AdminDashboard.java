// AdminDashboard.java
import javax.swing.*;

public class AdminDashboard extends JFrame {
    private String username;
    private String role;

    public AdminDashboard(String username, String role) {
        this.username = username;
        this.role = role;

        setTitle("Admin Dashboard - " + username);
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        JLabel welcomeLabel = new JLabel("Welcome Admin: " + username);
        welcomeLabel.setBounds(20, 20, 300, 30);
        add(welcomeLabel);

        JButton addBookBtn = new JButton("Add Book");
        addBookBtn.setBounds(160, 70, 150, 30);
        add(addBookBtn);

        JButton viewBooksBtn = new JButton("View Books");
        viewBooksBtn.setBounds(160, 110, 150, 30);
        add(viewBooksBtn);

        JButton deleteBookBtn = new JButton("Delete Book");
        deleteBookBtn.setBounds(160, 150, 150, 30);
        add(deleteBookBtn);

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBounds(160, 190, 150, 30);
        add(logoutBtn);

        addBookBtn.addActionListener(e -> new AddBookUI());
        viewBooksBtn.addActionListener(e -> new ViewBooksUI());
        deleteBookBtn.addActionListener(e -> new DeleteBookUI());
        logoutBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Logged out successfully.");
            dispose();
            new LoginUI();
        });

        setVisible(true);
    }
}