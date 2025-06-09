import javax.swing.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class AddBookUI extends JFrame {
    public AddBookUI() {
        // Ensure session is set and role is admin
        if (Session.getCurrentUsername() == null || !Session.getCurrentUsername().equalsIgnoreCase("admin")) {
            JOptionPane.showMessageDialog(null, "Access denied. Admins only.");
            dispose(); // close the window if not admin
            return;
        }

        setTitle("Add Book");
        setSize(300, 250);
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel nameLabel = new JLabel("Book Name:");
        nameLabel.setBounds(30, 30, 100, 25);
        add(nameLabel);

        JTextField nameField = new JTextField();
        nameField.setBounds(130, 30, 120, 25);
        add(nameField);

        JLabel authorLabel = new JLabel("Author:");
        authorLabel.setBounds(30, 70, 100, 25);
        add(authorLabel);

        JTextField authorField = new JTextField();
        authorField.setBounds(130, 70, 120, 25);
        add(authorField);

        JLabel quantityLabel = new JLabel("Quantity:");
        quantityLabel.setBounds(30, 110, 100, 25);
        add(quantityLabel);

        JTextField quantityField = new JTextField();
        quantityField.setBounds(130, 110, 120, 25);
        add(quantityField);

        JButton addButton = new JButton("Add Book");
        addButton.setBounds(90, 160, 120, 30);
        add(addButton);

        addButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String author = authorField.getText().trim();
            String quantityText = quantityField.getText().trim();

            if (name.isEmpty() || author.isEmpty() || quantityText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields.");
                return;
            }

            int quantity;
            try {
                quantity = Integer.parseInt(quantityText);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Quantity must be a number");
                return;
            }

            try {
                Connection conn = DBConnection.getConnection();
                String query = "INSERT INTO books (name, author, quantity) VALUES (?, ?, ?)";
                PreparedStatement pst = conn.prepareStatement(query);
                pst.setString(1, name);
                pst.setString(2, author);
                pst.setInt(3, quantity);
                pst.executeUpdate();
                conn.close();

                JOptionPane.showMessageDialog(this, "Book added successfully.");
                LoggerUtil.log("Add Book", Session.getCurrentUsername());
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        setVisible(true);
    }
}