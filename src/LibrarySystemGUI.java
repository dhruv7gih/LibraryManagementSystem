import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

/**
 * Library Management System GUI
 *
 * Based on Default Design Guidelines (Minimal, Elegant Component Library UI):
 * - Light background with generous whitespace
 * - Bold, elegant typography for headline
 * - Neutral gray readable body text
 * - Cards with subtle rounded corners and light shadows
 * - Sticky top navigation bar with logo & nav items
 * - Hero section with strong headline, subtext, and prominent CTA button
 * - Large, well-spaced buttons with gentle hover effects
 * - Theme toggle (light/dark) included
 *
 * Core functionality: login, add/view/delete books, issue/return books.
 *
 * Note: Assumes DBConnection.getConnection() provides a valid DB connection.
 */
public class LibrarySystemGUI extends JFrame {

    // Database connection
    private Connection conn;

    // Theme colors
    private static final Color LIGHT_BG = new Color(0xFFFFFF);
    private static final Color LIGHT_TEXT = new Color(0x6B7280);
    private static final Color LIGHT_BUTTON_BG = new Color(0x000000);
    private static final Color LIGHT_BUTTON_FG = new Color(0xFFFFFF);

    private static final Color DARK_BG = new Color(0x121212);
    private static final Color DARK_TEXT = new Color(0xB0B0B0);
    private static final Color DARK_BUTTON_BG = new Color(0xFFFFFF);
    private static final Color DARK_BUTTON_FG = new Color(0x000000);

    private boolean darkMode = false;

    // UI Components for theme toggling
    private JPanel heroPanel;
    private JPanel featuresPanel;
    private JLabel heroHeadline;
    private JLabel heroSubtext;
    private JButton ctaButton;
    private JButton themeToggleBtn;
    private JButton[] mainButtons;

    public LibrarySystemGUI() {
        try {
            conn = DBConnection.getConnection();
        } catch (Exception e) {
            showErrorDialog("DB Connection Error: " + e.getMessage());
            System.exit(1);
        }

        setTitle("Library Management System");
        setSize(1100, 800);
        setMinimumSize(new Dimension(900, 700));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(buildTopNav(), BorderLayout.NORTH);
        add(buildHeroSection(), BorderLayout.CENTER);
        add(buildFooter(), BorderLayout.SOUTH);

        applyTheme();

        setVisible(true);
    }

    // Sticky top navigation bar with logo and nav items + theme toggle
    private JPanel buildTopNav() {
        JPanel nav = new JPanel(new BorderLayout());
        nav.setBorder(BorderFactory.createEmptyBorder(12, 32, 12, 32));
        nav.setPreferredSize(new Dimension(900, 64));
        nav.setOpaque(true);

        JLabel logo = new JLabel("📚 LibSys");
        logo.setFont(new Font("Poppins", Font.BOLD, 28));
        nav.add(logo, BorderLayout.WEST);

        JPanel navItems = new JPanel(new FlowLayout(FlowLayout.RIGHT, 24, 0));
        navItems.setOpaque(false);

        String[] navLabels = {"Home", "Features", "About"};
        for (String label : navLabels) {
            JLabel navLabel = new JLabel(label);
            navLabel.setFont(new Font("Poppins", Font.PLAIN, 18));
            navLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            navItems.add(navLabel);
        }

        themeToggleBtn = new JButton("🌙");
        themeToggleBtn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));
        themeToggleBtn.setFocusPainted(false);
        themeToggleBtn.setBorderPainted(false);
        themeToggleBtn.setContentAreaFilled(false);
        themeToggleBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        themeToggleBtn.addActionListener(e -> toggleTheme());
        navItems.add(themeToggleBtn);

        nav.add(navItems, BorderLayout.EAST);

        nav.setBorder(BorderFactory.createCompoundBorder(
                new DropShadowBorder(3, new Color(0, 0, 0, 20)),
                nav.getBorder()
        ));

        return nav;
    }

    // Hero section containing headline, subtext and CTA plus features panel
    private JPanel buildHeroSection() {
        heroPanel = new JPanel();
        heroPanel.setLayout(new BoxLayout(heroPanel, BoxLayout.Y_AXIS));
        heroPanel.setBorder(BorderFactory.createEmptyBorder(80, 80, 80, 80));

        heroHeadline = new JLabel("Manage Your Library Seamlessly");
        heroHeadline.setFont(new Font("Poppins", Font.BOLD, 56));
        heroHeadline.setAlignmentX(Component.LEFT_ALIGNMENT);
        heroPanel.add(heroHeadline);

        heroPanel.add(Box.createRigidArea(new Dimension(0, 24)));

        heroSubtext = new JLabel("<html>Effortlessly track, issue, and return books with an intuitive system<br>built for efficiency and ease of use.</html>");
        heroSubtext.setFont(new Font("Poppins", Font.PLAIN, 22));
        heroSubtext.setAlignmentX(Component.LEFT_ALIGNMENT);
        heroPanel.add(heroSubtext);

        heroPanel.add(Box.createRigidArea(new Dimension(0, 48)));

        ctaButton = new JButton("Get Started");
        ctaButton.setFont(new Font("Poppins", Font.BOLD, 22));
        ctaButton.setFocusPainted(false);
        ctaButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        ctaButton.setPreferredSize(new Dimension(200, 60));
        ctaButton.setMaximumSize(new Dimension(200, 60));
        ctaButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        ctaButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Start by Logging in!"));
        heroPanel.add(ctaButton);

        heroPanel.add(Box.createRigidArea(new Dimension(0, 60)));

        heroPanel.add(buildFeatureCards());

        return heroPanel;
    }

    // Grid layout with cards for core features buttons with hover and shadows
    private JPanel buildFeatureCards() {
        featuresPanel = new JPanel();
        featuresPanel.setLayout(new GridLayout(2, 3, 32, 32));
        featuresPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        String[] features = {
                "Login", "Add Book", "View Books",
                "Delete Book", "Issue Book", "Return Book"
        };

        mainButtons = new JButton[features.length];

        for (int i = 0; i < features.length; i++) {
            JButton btn = new JButton(features[i]);
            btn.setFont(new Font("Poppins", Font.BOLD, 20));
            btn.setFocusPainted(false);
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btn.setBorder(new RoundedDropShadowBorder(16));
            btn.setBackground(darkMode ? DARK_BUTTON_BG : LIGHT_BUTTON_BG);
            btn.setForeground(darkMode ? DARK_BUTTON_FG : LIGHT_BUTTON_FG);

            final String actionCmd = features[i];
            btn.addActionListener(e -> handleButtonAction(actionCmd));

            btn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    btn.setBackground(darkMode ? LIGHT_BUTTON_BG : DARK_BUTTON_BG);
                    btn.setForeground(darkMode ? LIGHT_BUTTON_FG : LIGHT_BUTTON_FG);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    btn.setBackground(darkMode ? DARK_BUTTON_BG : LIGHT_BUTTON_BG);
                    btn.setForeground(darkMode ? DARK_BUTTON_FG : LIGHT_BUTTON_FG);
                }
            });

            mainButtons[i] = btn;
            featuresPanel.add(btn);
        }

        return featuresPanel;
    }

    // Empty footer for spacing
    private JPanel buildFooter() {
        JPanel footer = new JPanel();
        footer.setPreferredSize(new Dimension(900, 40));
        footer.setOpaque(false);
        return footer;
    }

    // Toggle dark/light theme and re-apply styles
    private void toggleTheme() {
        darkMode = !darkMode;
        applyTheme();
    }

    // Apply colors and fonts according to current theme
    private void applyTheme() {
        Color bg = darkMode ? DARK_BG : LIGHT_BG;
        Color text = darkMode ? DARK_TEXT : LIGHT_TEXT;
        Color btnBg = darkMode ? DARK_BUTTON_BG : LIGHT_BUTTON_BG;
        Color btnFg = darkMode ? DARK_BUTTON_FG : LIGHT_BUTTON_FG;

        getContentPane().setBackground(bg);

        Component nav = getContentPane().getComponent(0);
        if (nav instanceof JPanel) {
            nav.setBackground(bg);
            for (Component c : ((JPanel) nav).getComponents()) {
                if (c instanceof JLabel) {
                    c.setForeground(text);
                } else if (c instanceof JPanel) {
                    for (Component nc : ((JPanel) c).getComponents()) {
                        nc.setForeground(text);
                    }
                }
            }
        }

        heroPanel.setBackground(bg);
        heroHeadline.setForeground(text);
        heroSubtext.setForeground(text);

        ctaButton.setBackground(btnBg);
        ctaButton.setForeground(btnFg);
        ctaButton.setBorder(new RoundedDropShadowBorder(16));

        if (mainButtons != null) {
            for (JButton btn : mainButtons) {
                btn.setBackground(btnBg);
                btn.setForeground(btnFg);
            }
        }

        repaint();
        revalidate();

        themeToggleBtn.setText(darkMode ? "☀️" : "🌙");
    }

    // Central event handler for main buttons
    private void handleButtonAction(String action) {
        switch (action) {
            case "Login" -> login();
            case "Add Book" -> addBook();
            case "View Books" -> viewBooks();
            case "Delete Book" -> deleteBook();
            case "Issue Book" -> issueBook();
            case "Return Book" -> returnBook();
            default -> showErrorDialog("Unknown action: " + action);
        }
    }

    // Core feature implementations

    private void login() {
        String username = JOptionPane.showInputDialog(this, "Enter Username:");
        if (username == null || username.trim().isEmpty()) {
            showErrorDialog("Username cannot be empty.");
            return;
        }
        String password = JOptionPane.showInputDialog(this, "Enter Password:");
        if (password == null || password.trim().isEmpty()) {
            showErrorDialog("Password cannot be empty.");
            return;
        }
        try (PreparedStatement pst = conn.prepareStatement("SELECT * FROM users WHERE username=? AND password=?")) {
            pst.setString(1, username);
            pst.setString(2, password);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, "Login successful!");
                } else {
                    showErrorDialog("Invalid credentials.");
                }
            }
        } catch (Exception e) {
            showErrorDialog("Login Error: " + e.getMessage());
        }
    }

    private void addBook() {
        String title = JOptionPane.showInputDialog(this, "Enter Book Title:");
        if (title == null || title.trim().isEmpty()) {
            showErrorDialog("Book Title cannot be empty.");
            return;
        }

        String author = JOptionPane.showInputDialog(this, "Enter Book Author:");
        if (author == null || author.trim().isEmpty()) {
            showErrorDialog("Book Author cannot be empty.");
            return;
        }

        String qtyStr = JOptionPane.showInputDialog(this, "Enter Quantity:");
        if (qtyStr == null || qtyStr.trim().isEmpty()) {
            showErrorDialog("Quantity cannot be empty.");
            return;
        }

        try {
            int quantity = Integer.parseInt(qtyStr);
            if (quantity <= 0) {
                showErrorDialog("Quantity must be a positive number.");
                return;
            }
            try (PreparedStatement pst = conn.prepareStatement(
                    "INSERT INTO books (title, author, quantity) VALUES (?, ?, ?)")) {
                pst.setString(1, title);
                pst.setString(2, author);
                pst.setInt(3, quantity);
                pst.executeUpdate();
                JOptionPane.showMessageDialog(this, "Book added successfully!");
            }
        } catch (NumberFormatException e) {
            showErrorDialog("Invalid quantity. Enter a valid number.");
        } catch (Exception e) {
            showErrorDialog("Add Book Error: " + e.getMessage());
        }
    }

    private void viewBooks() {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM books")) {

            StringBuilder sb = new StringBuilder("ID\tTitle\tAuthor\tQuantity\n");
            sb.append("----------------------------------------------------\n");
            while (rs.next()) {
                sb.append(rs.getInt("id")).append("\t")
                        .append(rs.getString("title")).append("\t")
                        .append(rs.getString("author")).append("\t")
                        .append(rs.getInt("quantity")).append("\n");
            }

            JTextArea textArea = new JTextArea(sb.toString());
            textArea.setEditable(false);
            textArea.setFont(new Font("Poppins", Font.PLAIN, 18));
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(800, 450));

            JOptionPane.showMessageDialog(this, scrollPane, "Books List", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            showErrorDialog("View Books Error: " + e.getMessage());
        }
    }

    private void deleteBook() {
        String idStr = JOptionPane.showInputDialog(this, "Enter Book ID to delete:");
        if (idStr == null || idStr.trim().isEmpty()) {
            showErrorDialog("Book ID cannot be empty.");
            return;
        }

        try {
            int id = Integer.parseInt(idStr);
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this book?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                try (PreparedStatement pst = conn.prepareStatement("DELETE FROM books WHERE id=?")) {
                    pst.setInt(1, id);
                    int rows = pst.executeUpdate();

                    if (rows > 0) {
                        JOptionPane.showMessageDialog(this, "Book deleted successfully.");
                    } else {
                        showErrorDialog("Book ID not found.");
                    }
                }
            }
        } catch (NumberFormatException e) {
            showErrorDialog("Invalid ID. Enter a valid number.");
        } catch (Exception e) {
            showErrorDialog("Delete Book Error: " + e.getMessage());
        }
    }

    private void issueBook() {
        String bookIdStr = JOptionPane.showInputDialog(this, "Enter Book ID to issue:");
        if (bookIdStr == null || bookIdStr.trim().isEmpty()) {
            showErrorDialog("Book ID cannot be empty.");
            return;
        }

        try {
            int bookId = Integer.parseInt(bookIdStr);
            try (PreparedStatement pst = conn.prepareStatement(
                    "UPDATE books SET quantity = quantity - 1 WHERE id = ? AND quantity > 0")) {
                pst.setInt(1, bookId);
                int rows = pst.executeUpdate();

                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Book issued successfully.");
                } else {
                    showErrorDialog("Book not available or invalid ID.");
                }
            }
        } catch (NumberFormatException e) {
            showErrorDialog("Invalid ID. Enter a valid number.");
        } catch (Exception e) {
            showErrorDialog("Issue Book Error: " + e.getMessage());
        }
    }

    private void returnBook() {
        String bookIdStr = JOptionPane.showInputDialog(this, "Enter Book ID to return:");
        if (bookIdStr == null || bookIdStr.trim().isEmpty()) {
            showErrorDialog("Book ID cannot be empty.");
            return;
        }

        try {
            int bookId = Integer.parseInt(bookIdStr);
            try (PreparedStatement pst = conn.prepareStatement(
                    "UPDATE books SET quantity = quantity + 1 WHERE id = ?")) {
                pst.setInt(1, bookId);
                int rows = pst.executeUpdate();

                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Book returned successfully.");
                } else {
                    showErrorDialog("Invalid Book ID.");
                }
            }
        } catch (NumberFormatException e) {
            showErrorDialog("Invalid ID. Enter a valid number.");
        } catch (Exception e) {
            showErrorDialog("Return Book Error: " + e.getMessage());
        }
    }

    private void showErrorDialog(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Custom drop shadow border class for UI components
     */
    private static class DropShadowBorder extends AbstractBorder {
        private final int shadowSize;
        private final Color shadowColor;

        public DropShadowBorder(int shadowSize, Color shadowColor) {
            this.shadowSize = shadowSize;
            this.shadowColor = shadowColor;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(shadowColor);
            g2.fillRoundRect(x + shadowSize, y + shadowSize, width - shadowSize, height - shadowSize, 15, 15);
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(shadowSize, shadowSize, shadowSize, shadowSize);
        }
    }

    /**
     * Custom rounded border with shadow for buttons and cards
     */
    private static class RoundedDropShadowBorder extends AbstractBorder {
        private final int radius;

        public RoundedDropShadowBorder(int radius) {
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Shadow
            g2.setColor(new Color(0, 0, 0, 30));
            g2.fillRoundRect(x + 3, y + 3, width - 6, height - 6, radius, radius);

            // Border
            g2.setColor(new Color(0, 0, 0, 100));
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawRoundRect(x + 1, y + 1, width - 3, height - 3, radius, radius);

            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(10, 16, 10, 16);
        }
    }

    // Close DB connection on exit
    private void closeConnection() {
        if (conn != null) {
            try {
                if (!conn.isClosed()) conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing DB connection: " + e.getMessage());
            }
        }
    }

    // Entry point
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }
        SwingUtilities.invokeLater(LibrarySystemGUI::new);
    }
}