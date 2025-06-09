import java.sql.Connection;
import java.sql.PreparedStatement;

public class LoggerUtil {
    public static void log(String user, String action) {
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "INSERT INTO logs (username, action) VALUES (?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, user);
            pst.setString(2, action);
            pst.executeUpdate();
            conn.close();
        } catch (Exception e) {
            System.out.println("Logging failed: " + e.getMessage());
        }
    }
}