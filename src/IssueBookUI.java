import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class IssueBookUI extends JFrame {
    private DefaultTableModel model;
    private JTable table;
    private JTextField searchField;
    private TableRowSorter<DefaultTableModel> sorter;

    public IssueBookUI() {
        setTitle("Issue Book");
        setSize(750, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Top panel for search
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Search:"));
        searchField = new JTextField(20);
        topPanel.add(searchField);
        add(topPanel, BorderLayout.NORTH);

        // Table to display books
        model = new DefaultTableModel(new String[]{"ID", "Title", "Author", "Quantity"}, 0);
        table = new JTable(model);
        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom panel for action
        JButton issueBtn = new JButton("Issue Selected Book");
        issueBtn.addActionListener(e -> issueSelectedBook());
        add(issueBtn, BorderLayout.SOUTH);

        loadBooks();

        // Add filtering
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filter(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filter(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filter(); }
        });

        setVisible(true);
    }

    private void loadBooks() {
        try {
            Connection conn = DBConnection.getConnection();
            String query = "SELECT * FROM books WHERE quantity > 0";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getInt("quantity")
                });
            }

            conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading books: " + e.getMessage());
        }
    }

    private void filter() {
        String text = searchField.getText();
        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
    }

    private void issueSelectedBook() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to issue.");
            return;
        }

        int bookId = (int) table.getValueAt(selectedRow, 0);
        int quantity = (int) table.getValueAt(selectedRow, 3);

        if (quantity <= 0) {
            JOptionPane.showMessageDialog(this, "This book is not available.");
            return;
        }

        try {
            Connection conn = DBConnection.getConnection();
            String query = "UPDATE books SET quantity = quantity - 1 WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, bookId);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                LoggerUtil.log("Issued book ID " + bookId, Session.getCurrentUsername());
                JOptionPane.showMessageDialog(this, "Book issued successfully.");
                model.setValueAt(quantity - 1, selectedRow, 3); // Update table value
            } else {
                JOptionPane.showMessageDialog(this, "Failed to issue the book.");
            }

            conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error issuing book: " + e.getMessage());
        }
    }
}