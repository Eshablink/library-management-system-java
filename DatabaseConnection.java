import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Manages SQLite JDBC connectivity for the LibraryManager application.
 */
public class DatabaseConnection {
    private static final String JDBC_URL = "jdbc:sqlite:library.db";

    /**
     * Opens and returns a JDBC connection to the SQLite database file.
     *
     * @return an active JDBC connection
     * @throws SQLException if the driver cannot create the connection
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL);
    }

    /**
     * Creates the Books table when it does not already exist.
     * The method opens a JDBC connection, creates a statement,
     * and executes the schema DDL against SQLite.
     */
    public static void initializeDatabase() {
        String createTableSql = """
                CREATE TABLE IF NOT EXISTS Books (
                    id INTEGER PRIMARY KEY,
                    title TEXT NOT NULL,
                    author TEXT NOT NULL,
                    year INTEGER NOT NULL,
                    available BOOLEAN NOT NULL
                )
                """;

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(createTableSql);
            System.out.println("Database initialized successfully.");
        } catch (SQLException exception) {
            System.err.println("Failed to initialize database: " + exception.getMessage());
        }
    }
}
