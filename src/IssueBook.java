import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class IssueBook {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Book ID to issue: ");
        int bookId = scanner.nextInt();

        try {
            Connection conn = DBConnection.getConnection();
            String query = "UPDATE books SET quantity = quantity - 1 WHERE id = ? AND quantity > 0";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, bookId);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Book issued successfully.");
            } else {
                System.out.println("Book unavailable or not found.");
            }

            conn.close();
        } catch (Exception e) {
            System.out.println("Error issuing book: " + e.getMessage());
        }
    }
}