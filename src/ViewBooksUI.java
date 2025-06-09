import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class ViewBooksUI extends JFrame {
    JTable bookTable;
    JTextField searchField;
    JButton searchBtn, refreshBtn;

    public ViewBooksUI() {
        setTitle("📚 View Books");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);

        JLabel searchLabel = new JLabel("Search Title:");
        searchLabel.setBounds(20, 20, 100, 25);
        add(searchLabel);

        searchField = new JTextField();
        searchField.setBounds(120, 20, 200, 25);
        add(searchField);

        searchBtn = new JButton("Search");
        searchBtn.setBounds(330, 20, 100, 25);
        add(searchBtn);

        refreshBtn = new JButton("Refresh");
        refreshBtn.setBounds(440, 20, 100, 25);
        add(refreshBtn);

        bookTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(bookTable);
        scrollPane.setBounds(20, 60, 540, 280);
        add(scrollPane);

        loadBooks(""); // load all initially

        searchBtn.addActionListener(e -> loadBooks(searchField.getText()));
        refreshBtn.addActionListener(e -> {
            searchField.setText("");
            loadBooks("");
        });

        setVisible(true);
    }

    private void loadBooks(String keyword) {
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"ID", "Title", "Author", "Quantity"});

        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) {
                JOptionPane.showMessageDialog(this, "Connection failed.");
                return;
            }

            String query = (keyword == null || keyword.trim().isEmpty())
                    ? "SELECT * FROM books"
                    : "SELECT * FROM books WHERE title LIKE ?";

            PreparedStatement pst = conn.prepareStatement(query);

            if (query.contains("LIKE")) {
                pst.setString(1, "%" + keyword + "%");
            }

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getInt("quantity")
                });
            }

            bookTable.setModel(model);

        } catch (Exception e) {
            e.printStackTrace(); // show full error in console
            JOptionPane.showMessageDialog(this, "Error loading books: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new ViewBooksUI();
    }
}