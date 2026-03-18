import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides DAO operations for inserting, reading, updating, and deleting books.
 */
public class BookDAO {
    /**
     * Inserts a new book row using a prepared statement.
     *
     * @param book the book to save
     */
    public void addBook(Book book) {
        String sql = "INSERT INTO Books (id, title, author, year, available) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, book.getId());
            statement.setString(2, book.getTitle());
            statement.setString(3, book.getAuthor());
            statement.setInt(4, book.getYear());
            statement.setBoolean(5, book.isAvailable());
            statement.executeUpdate();
            System.out.println("Book added successfully.");
        } catch (SQLException exception) {
            System.err.println("Unable to add book: " + exception.getMessage());
        }
    }

    /**
     * Reads all books from the database and returns them as model objects.
     *
     * @return all stored books
     */
    public List<Book> getAllBooks() {
        String sql = "SELECT id, title, author, year, available FROM Books";
        List<Book> books = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                books.add(mapRowToBook(resultSet));
            }
        } catch (SQLException exception) {
            System.err.println("Unable to fetch books: " + exception.getMessage());
        }

        return books;
    }

    /**
     * Searches for books written by the supplied author.
     *
     * @param author the author name to search for
     * @return matching books
     */
    public List<Book> searchByAuthor(String author) {
        String sql = "SELECT id, title, author, year, available FROM Books WHERE author LIKE ?";
        List<Book> books = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "%" + author + "%");

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    books.add(mapRowToBook(resultSet));
                }
            }
        } catch (SQLException exception) {
            System.err.println("Unable to search books: " + exception.getMessage());
        }

        return books;
    }

    /**
     * Updates the availability flag for a given book id.
     *
     * @param id the book identifier
     * @param available the new availability state
     */
    public void updateAvailability(int id, boolean available) {
        String sql = "UPDATE Books SET available = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setBoolean(1, available);
            statement.setInt(2, id);
            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Book availability updated successfully.");
            } else {
                System.out.println("No book found with id " + id + ".");
            }
        } catch (SQLException exception) {
            System.err.println("Unable to update book availability: " + exception.getMessage());
        }
    }

    /**
     * Deletes a book row by id.
     *
     * @param id the book identifier to remove
     */
    public void deleteBook(int id) {
        String sql = "DELETE FROM Books WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            int rowsDeleted = statement.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Book deleted successfully.");
            } else {
                System.out.println("No book found with id " + id + ".");
            }
        } catch (SQLException exception) {
            System.err.println("Unable to delete book: " + exception.getMessage());
        }
    }

    /**
     * Converts a JDBC result row into a Book object.
     *
     * @param resultSet the current result row
     * @return mapped book model
     * @throws SQLException when result reading fails
     */
    private Book mapRowToBook(ResultSet resultSet) throws SQLException {
        return new Book(
                resultSet.getInt("id"),
                resultSet.getString("title"),
                resultSet.getString("author"),
                resultSet.getInt("year"),
                resultSet.getBoolean("available")
        );
    }
}
