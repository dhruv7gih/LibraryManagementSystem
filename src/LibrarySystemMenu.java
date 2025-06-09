import java.util.Scanner;

public class LibrarySystemMenu {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\nLibrary System Menu:");
            System.out.println("1. View Books");
            System.out.println("2. Add Book");
            System.out.println("3. Delete Book");
            System.out.println("4. Issue Book");
            System.out.println("5. Return Book");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    new ViewBooksUI(); // ✅ instantiate instead of calling .main
                    break;
                case 2:
                    new AddBookUI();
                    break;
                case 3:
                    new DeleteBookUI();
                    break;
                case 4:
                    new IssueBookUI();
                    break;
                case 5:
                    new ReturnBookUI();
                    break;
                case 6:
                    System.out.println("Exiting program...");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }

        scanner.close();
    }
}
