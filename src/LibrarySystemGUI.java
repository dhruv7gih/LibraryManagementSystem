import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.prefs.Preferences;

public class LibrarySystemGUI extends JFrame {

    private JPanel contentPanel;
    private boolean darkMode = false;
    private Preferences prefs;
    private static String currentUser = null;

    public LibrarySystemGUI() {
        prefs = Preferences.userRoot().node(this.getClass().getName());
        darkMode = prefs.getBoolean("darkMode", false);

        setTitle("Library Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        getContentPane().setLayout(new BorderLayout());

        createNavigationBar();
        contentPanel = new JPanel(new GridLayout(2, 3, 30, 30));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        getContentPane().add(contentPanel, BorderLayout.CENTER);

        createFeatureCards();
        applyTheme();
        setVisible(true);
    }

    private void createNavigationBar() {
        JPanel navBar = new JPanel(new BorderLayout());
        navBar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel titleLabel = new JLabel("Library Management System");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        navBar.add(titleLabel, BorderLayout.WEST);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        String[] navItems = {"Home", "About", "Logout"};
        for (String item : navItems) {
            JButton btn = createNavButton(item);
            btn.addActionListener(e -> handleNavAction(item));
            buttonsPanel.add(btn);
        }

        JToggleButton themeToggle = new JToggleButton("🌞/🌙");
        themeToggle.setSelected(darkMode);
        themeToggle.addActionListener(e -> toggleTheme(themeToggle.isSelected()));
        buttonsPanel.add(themeToggle);

        navBar.add(buttonsPanel, BorderLayout.EAST);
        getContentPane().add(navBar, BorderLayout.NORTH);
    }

    private void createFeatureCards() {
        Object[][] features = {
                {"Add Book", "➕", (ActionListener) this::addBook},
                {"View Books", "📚", (ActionListener) this::viewBooks},
                {"Delete Book", "❌", (ActionListener) this::deleteBook},
                {"Issue Book", "📤", (ActionListener) this::issueBook},
                {"Return Book", "📥", (ActionListener) this::returnBook}
        };
        for (Object[] feature : features) {
            String label = (String) feature[0];
            String icon = (String) feature[1];
            ActionListener action = (ActionListener) feature[2];
            JButton btn = createFeatureButton(label, icon, action);
            contentPanel.add(btn);
        }
    }

    private JButton createNavButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 16));
        btn.setFocusPainted(false);
        return btn;
    }

    private JButton createFeatureButton(String label, String icon, ActionListener action) {
        JButton button = new JButton("<html><center>" + icon + "<br>" + label + "</center></html>");
        button.setFont(new Font("SansSerif", Font.BOLD, 18));
        button.setPreferredSize(new Dimension(200, 150));
        button.setBackground(new Color(230, 230, 250));
        button.setFocusPainted(false);
        button.setBorder(new RoundedDropShadowBorder(20));
        button.addActionListener(action);
        return button;
    }

    private void applyTheme() {
        Color bgColor = darkMode ? new Color(40, 40, 40) : Color.WHITE;
        Color fgColor = darkMode ? Color.WHITE : Color.BLACK;

        contentPanel.setBackground(bgColor);
        getContentPane().setBackground(bgColor);
        for (Component c : contentPanel.getComponents()) {
            if (c instanceof JButton) {
                JButton b = (JButton) c;
                b.setForeground(fgColor);
                b.setBackground(darkMode ? new Color(60, 60, 60) : new Color(230, 230, 250));
            }
        }
        repaint();
    }

    private void toggleTheme(boolean isDark) {
        darkMode = isDark;
        prefs.putBoolean("darkMode", darkMode);
        applyTheme();
    }

    private void handleNavAction(String item) {
        switch (item) {
            case "Logout":
                currentUser = null;
                JOptionPane.showMessageDialog(this, "Logged out successfully.");
                System.exit(0);
                break;
            case "About":
                JOptionPane.showMessageDialog(this, "Modern Library System - Java Swing Edition.");
                break;
        }
    }

    private void addBook(ActionEvent e) {
        JTextField bookField = new JTextField();
        JTextField authorField = new JTextField();
        Object[] fields = {
                "Book Name:", bookField,
                "Author:", authorField
        };

        int option = JOptionPane.showConfirmDialog(this, fields, "Add Book", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try (Connection conn = getConnection();
                 PreparedStatement stmt = conn.prepareStatement("INSERT INTO books (bookname, author) VALUES (?, ?)")) {
                stmt.setString(1, bookField.getText());
                stmt.setString(2, authorField.getText());
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Book added.");
            } catch (SQLException ex) {
                showError(ex);
            }
        }
    }

    private void viewBooks(ActionEvent e) {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id, bookname, author, issued_to FROM books")) {

            JTable table = new JTable(buildTableModel(rs));
            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setPreferredSize(new Dimension(600, 300));
            JOptionPane.showMessageDialog(this, scrollPane, "Books List", JOptionPane.PLAIN_MESSAGE);

        } catch (SQLException ex) {
            showError(ex);
        }
    }

    private void deleteBook(ActionEvent e) {
        String bookId = JOptionPane.showInputDialog(this, "Enter Book ID to delete:");
        if (bookId != null && !bookId.trim().isEmpty()) {
            try (Connection conn = getConnection();
                 PreparedStatement stmt = conn.prepareStatement("DELETE FROM books WHERE id = ?")) {
                stmt.setInt(1, Integer.parseInt(bookId));
                int deleted = stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, deleted > 0 ? "Book deleted." : "Book not found.");
            } catch (SQLException ex) {
                showError(ex);
            }
        }
    }

    private void issueBook(ActionEvent e) {
        String bookId = JOptionPane.showInputDialog(this, "Enter Book ID to issue:");
        String userId = JOptionPane.showInputDialog(this, "Enter User ID:");
        if (bookId != null && userId != null) {
            try (Connection conn = getConnection();
                 PreparedStatement stmt = conn.prepareStatement("UPDATE books SET issued_to = ? WHERE id = ?")) {
                stmt.setString(1, userId);
                stmt.setInt(2, Integer.parseInt(bookId));
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Book issued.");
            } catch (SQLException ex) {
                showError(ex);
            }
        }
    }

    private void returnBook(ActionEvent e) {
        String bookId = JOptionPane.showInputDialog(this, "Enter Book ID to return:");
        if (bookId != null) {
            try (Connection conn = getConnection();
                 PreparedStatement stmt = conn.prepareStatement("UPDATE books SET issued_to = NULL WHERE id = ?")) {
                stmt.setInt(1, Integer.parseInt(bookId));
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Book returned.");
            } catch (SQLException ex) {
                showError(ex);
            }
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "");
    }

    private void showError(Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }

    private DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {
        ResultSetMetaData meta = rs.getMetaData();
        int cols = meta.getColumnCount();
        String[] colNames = new String[cols];
        for (int i = 1; i <= cols; i++) colNames[i - 1] = meta.getColumnName(i);

        DefaultTableModel model = new DefaultTableModel(colNames, 0);
        while (rs.next()) {
            Object[] row = new Object[cols];
            for (int i = 1; i <= cols; i++) row[i - 1] = rs.getObject(i);
            model.addRow(row);
        }
        return model;
    }

    private static class RoundedDropShadowBorder extends AbstractBorder {
        private final int radius;

        public RoundedDropShadowBorder(int radius) {
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(new Color(0, 0, 0, 30));
            g2.fillRoundRect(x + 2, y + 2, width - 4, height - 4, radius, radius);
            g2.setColor(c.getBackground());
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);

            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(8, 8, 8, 8);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.left = insets.right = insets.top = insets.bottom = 8;
            return insets;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            currentUser = "admin"; // Simulate login
            new LibrarySystemGUI();
        });
    }
}