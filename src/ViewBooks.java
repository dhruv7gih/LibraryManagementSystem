import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ViewBooks {
    public static void main(String[] args) {
        try {
            Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM books"; // assumes a table named 'books'
            ResultSet rs = stmt.executeQuery(sql);

            System.out.println("Book List:");
            System.out.println("ID\tTitle\t\tAuthor\t\tQuantity");
            System.out.println("----------------------------------------------------");

            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                int quantity = rs.getInt("quantity");

                System.out.printf("%d\t%s\t\t%s\t\t%d\n", id, title, author, quantity);
            }

            conn.close();
        } catch (Exception e) {
            System.out.println("Error fetching books: " + e.getMessage());
        }
    }
}