import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class DeleteBookUI extends JFrame {
    private DefaultTableModel model;
    private JTable table;
    private JTextField searchField;
    private TableRowSorter<DefaultTableModel> sorter;

    public DeleteBookUI() {
        setTitle("Delete Book");
        setSize(750, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Top panel with search box
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Search:"));
        searchField = new JTextField(20);
        topPanel.add(searchField);
        add(topPanel, BorderLayout.NORTH);

        // Table setup
        model = new DefaultTableModel(new String[]{"ID", "Title", "Author", "Quantity"}, 0);
        table = new JTable(model);
        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom panel with delete button
        JButton deleteBtn = new JButton("Delete Selected Book");
        deleteBtn.addActionListener(e -> deleteSelectedBook());
        add(deleteBtn, BorderLayout.SOUTH);

        // Load data and set search filter
        loadBooks();
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

    private void deleteSelectedBook() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to delete.");
            return;
        }

        int bookId = (int) table.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this book?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                Connection conn = DBConnection.getConnection();
                String query = "DELETE FROM books WHERE id = ?";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, bookId);

                int rows = ps.executeUpdate();
                if (rows > 0) {
                    LoggerUtil.log("Deleted book ID " + bookId, Session.getCurrentUsername());
                    model.removeRow(selectedRow);
                    JOptionPane.showMessageDialog(this, "Book deleted successfully.");
                } else {
                    JOptionPane.showMessageDialog(this, "Book could not be deleted.");
                }

                conn.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error deleting book: " + e.getMessage());
            }
        }
    }
}
