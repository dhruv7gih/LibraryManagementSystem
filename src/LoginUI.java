import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class LoginUI extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginBtn;

    public LoginUI() {
        setTitle("Login");
        setSize(350, 200);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(30, 30, 80, 25);
        add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(120, 30, 150, 25);
        add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(30, 70, 80, 25);
        add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(120, 70, 150, 25);
        add(passwordField);

        loginBtn = new JButton("Login");
        loginBtn.setBounds(120, 110, 100, 30);
        add(loginBtn);

        loginBtn.addActionListener(e -> login());

        setVisible(true);
    }

    private void login() {
        String username = usernameField.getText();
        String password = String.valueOf(passwordField.getPassword());

        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, username);
            pst.setString(2, password);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                String role = rs.getString("role");
                Session.setCurrentUser(username, role);
                JOptionPane.showMessageDialog(this, "Login successful as " + role + "!");

                if (role.equalsIgnoreCase("admin")) {
                    new AdminDashboard(username, role);
                } else {
                    new UserDashboard(username, role);  // Ensure constructor is defined
                }
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials!");
            }

            conn.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new LoginUI();
    }
}