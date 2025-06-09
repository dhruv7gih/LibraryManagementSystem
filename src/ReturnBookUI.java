import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ReturnBookUI extends JFrame {
    private DefaultTableModel model;
    private JTable table;
    private JTextField searchField;
    private TableRowSorter<DefaultTableModel> sorter;

    public ReturnBookUI() {
        setTitle("Return Book");
        setSize(750, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Top panel with search
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Search:"));
        searchField = new JTextField(20);
        topPanel.add(searchField);
        add(topPanel, BorderLayout.NORTH);

        // Table
        model = new DefaultTableModel(new String[]{"ID", "Title", "Author", "Quantity"}, 0);
        table = new JTable(model);
        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom panel with button
        JButton returnBtn = new JButton("Return Selected Book");
        returnBtn.addActionListener(e -> returnSelectedBook());
        add(returnBtn, BorderLayout.SOUTH);

        loadBooks();

        // Add search functionality
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
            String query = "SELECT * FROM books";
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

    private void returnSelectedBook() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to return.");
            return;
        }

        int bookId = (int) table.getValueAt(selectedRow, 0);
        int quantity = (int) table.getValueAt(selectedRow, 3);

        try {
            Connection conn = DBConnection.getConnection();
            String query = "UPDATE books SET quantity = quantity + 1 WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, bookId);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                LoggerUtil.log("Returned book ID " + bookId, Session.getCurrentUsername());
                JOptionPane.showMessageDialog(this, "Book returned successfully.");
                model.setValueAt(quantity + 1, selectedRow, 3); // update table value
            } else {
                JOptionPane.showMessageDialog(this, "Failed to return book.");
            }

            conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error returning book: " + e.getMessage());
        }
    }
}