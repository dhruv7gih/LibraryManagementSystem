import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddBook {
    public static void main(String[] args) {
        Connection conn = DBConnection.getConnection();
        if (conn != null) {
            try {
                String query = "INSERT INTO books (title, author, quantity) VALUES (?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setString(1, "The Alchemist");
                ps.setString(2, "Paulo Coelho");
                ps.setInt(3, 5);

                int result = ps.executeUpdate();
                if (result > 0) {
                    System.out.println("Book added successfully!");
                }

                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}