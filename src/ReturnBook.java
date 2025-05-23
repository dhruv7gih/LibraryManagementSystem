import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class ReturnBook {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Book ID to return: ");
        int bookId = scanner.nextInt();

        try {
            Connection conn = DBConnection.getConnection();
            String query = "UPDATE books SET quantity = quantity + 1 WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, bookId);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Book returned successfully.");
            } else {
                System.out.println("Book ID not found.");
            }

            conn.close();
        } catch (Exception e) {
            System.out.println("Error returning book: " + e.getMessage());
        }
    }
}