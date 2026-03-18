import java.util.List;
import java.util.Scanner;

/**
 * Console entry point for the LibraryManager application.
 */
public class Main {
    /**
     * Initializes the database and starts the interactive menu loop.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        DatabaseConnection.initializeDatabase();
        BookDAO bookDAO = new BookDAO();

        try (Scanner scanner = new Scanner(System.in)) {
            boolean running = true;

            while (running) {
                printMenu();
                System.out.print("Choose an option: ");
                String input = scanner.nextLine();

                switch (input) {
                    case "1" -> addBook(scanner, bookDAO);
                    case "2" -> displayBooks(bookDAO.getAllBooks());
                    case "3" -> searchBooksByAuthor(scanner, bookDAO);
                    case "4" -> updateBookAvailability(scanner, bookDAO);
                    case "5" -> deleteBook(scanner, bookDAO);
                    case "0" -> {
                        running = false;
                        System.out.println("Exiting LibraryManager.");
                    }
                    default -> System.out.println("Invalid option. Please try again.");
                }
            }
        }
    }

    /**
     * Prints the available menu actions for the user.
     */
    private static void printMenu() {
        System.out.println("\n=== LibraryManager Menu ===");
        System.out.println("1. Add book");
        System.out.println("2. List all books");
        System.out.println("3. Search by author");
        System.out.println("4. Update availability");
        System.out.println("5. Delete book");
        System.out.println("0. Exit");
    }

    /**
     * Reads new book data from the console and sends it to the DAO layer.
     */
    private static void addBook(Scanner scanner, BookDAO bookDAO) {
        try {
            System.out.print("Enter id: ");
            int id = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter title: ");
            String title = scanner.nextLine();
            System.out.print("Enter author: ");
            String author = scanner.nextLine();
            System.out.print("Enter publication year: ");
            int year = Integer.parseInt(scanner.nextLine());
            System.out.print("Is the book available? (true/false): ");
            boolean available = Boolean.parseBoolean(scanner.nextLine());

            bookDAO.addBook(new Book(id, title, author, year, available));
        } catch (NumberFormatException exception) {
            System.err.println("Invalid numeric input: " + exception.getMessage());
        }
    }

    /**
     * Displays a list of books returned by the DAO layer.
     */
    private static void displayBooks(List<Book> books) {
        if (books.isEmpty()) {
            System.out.println("No books found.");
            return;
        }

        books.forEach(System.out::println);
    }

    /**
     * Prompts for an author value and prints matching books.
     */
    private static void searchBooksByAuthor(Scanner scanner, BookDAO bookDAO) {
        System.out.print("Enter author name to search: ");
        String author = scanner.nextLine();
        displayBooks(bookDAO.searchByAuthor(author));
    }

    /**
     * Prompts for a book id and a new availability flag, then updates the row.
     */
    private static void updateBookAvailability(Scanner scanner, BookDAO bookDAO) {
        try {
            System.out.print("Enter book id: ");
            int id = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter availability (true/false): ");
            boolean available = Boolean.parseBoolean(scanner.nextLine());
            bookDAO.updateAvailability(id, available);
        } catch (NumberFormatException exception) {
            System.err.println("Invalid numeric input: " + exception.getMessage());
        }
    }

    /**
     * Prompts for a book id and removes the matching record.
     */
    private static void deleteBook(Scanner scanner, BookDAO bookDAO) {
        try {
            System.out.print("Enter book id to delete: ");
            int id = Integer.parseInt(scanner.nextLine());
            bookDAO.deleteBook(id);
        } catch (NumberFormatException exception) {
            System.err.println("Invalid numeric input: " + exception.getMessage());
        }
    }
}
