import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ViewLogsUI extends JFrame {

    public ViewLogsUI() {
        setTitle("View Logs");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Center window
        setLayout(new BorderLayout());

        String[] columns = {"ID", "Username", "Action", "Timestamp"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        try {
            Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM logs ORDER BY timestamp DESC");

            while (rs.next()) {
                int id = rs.getInt("id");
                String username = rs.getString("username");
                String action = rs.getString("action");
                String timestamp = rs.getString("timestamp");
                model.addRow(new Object[]{id, username, action, timestamp});
            }

            conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading logs: " + e.getMessage());
        }

        add(scrollPane, BorderLayout.CENTER);
        setVisible(true);
    }

    public static void main(String[] args) {
        new ViewLogsUI();
    }
}