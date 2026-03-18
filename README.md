# LibraryManager

## Project overview
LibraryManager is a small Java console backend application that uses JDBC to connect to a SQLite database. It creates a `library.db` file at runtime, ensures the `Books` table exists, and offers CRUD-style operations through a DAO class and a text menu.

## Project files
- `DatabaseConnection.java` sets up the SQLite JDBC connection and initializes the schema.
- `Book.java` defines the book model used across the application.
- `BookDAO.java` contains data access logic using prepared statements.
- `Main.java` provides the console interface.

## How to run
1. Install Java 17 or newer.
2. Download the SQLite JDBC driver JAR, for example `sqlite-jdbc-<version>.jar`, from the Xerial SQLite JDBC project.
3. Place the driver jar somewhere outside the repository, because `lib/` is intentionally gitignored.
4. Compile the project:
   ```bash
   javac -cp .:/path/to/sqlite-jdbc-<version>.jar Main.java Book.java BookDAO.java DatabaseConnection.java
   ```
5. Run the application:
   ```bash
   java -cp .:/path/to/sqlite-jdbc-<version>.jar Main
   ```
6. On first launch, the application creates `library.db` automatically and creates the `Books` table if it does not exist.

> On Windows, replace `:` in the classpath with `;`.

## What JDBC means
JDBC stands for Java Database Connectivity. It is the standard Java API for opening database connections, sending SQL statements, reading result sets, and handling SQL exceptions in a database-independent way.

## What the DAO pattern means
DAO stands for Data Access Object. The DAO pattern separates persistence logic from the rest of the application. In this project, `BookDAO` owns the SQL queries while `Main` focuses on user interaction and `Book` represents the data model.

## JDBC connection flow in this project
1. `Main` starts and calls `DatabaseConnection.initializeDatabase()`.
2. `DatabaseConnection` opens a JDBC connection with `DriverManager.getConnection("jdbc:sqlite:library.db")`.
3. The schema is created if needed.
4. `Main` calls methods on `BookDAO` when the user chooses a menu option.
5. `BookDAO` opens a connection for each operation, uses prepared statements, and maps rows into `Book` objects.
6. Try-with-resources closes connections, statements, and result sets automatically.

## Database schema
The application creates the following table:

```sql
CREATE TABLE IF NOT EXISTS Books (
    id INTEGER PRIMARY KEY,
    title TEXT NOT NULL,
    author TEXT NOT NULL,
    year INTEGER NOT NULL,
    available BOOLEAN NOT NULL
);
```

## Repository cleanup
This repository intentionally keeps only source and documentation files. Generated files such as `*.class`, `*.db`, `out/`, and `lib/` are ignored in `.gitignore` and should not be committed.
