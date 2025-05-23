import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class DeleteBook {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Book ID to delete: ");
        int bookId = scanner.nextInt();

        try {
            Connection conn = DBConnection.getConnection();
            String sql = "DELETE FROM books WHERE id = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, bookId);

            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Book deleted successfully.");
            } else {
                System.out.println("Book not found.");
            }

            conn.close();
        } catch (Exception e) {
            System.out.println("Error deleting book: " + e.getMessage());
        }
   }
}