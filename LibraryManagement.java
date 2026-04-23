
import java.sql.*;
import java.util.Scanner;

// Custom exception for book already exists
class BookAlreadyExistsException extends Exception {

    public BookAlreadyExistsException(String message) {
        super(message);
    }
}

// Custom exception for book not found
class BookNotFoundException extends Exception {

    public BookNotFoundException(String message) {
        super(message);
    }

}

public class LibraryManagement {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/library";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "your_password_here"; //!

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("\nLibrary Management System");
                System.out.println("1. Add Book");
                System.out.println("2. View Books");
                System.out.println("3. Update Book Quantity");
                System.out.println("4. Delete Book");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");

                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1 ->
                        addBook(connection, scanner);
                    case 2 ->
                        viewBooks(connection);
                    case 3 ->
                        updateBookQuantity(connection, scanner);
                    case 4 ->
                        deleteBook(connection, scanner);
                    case 5 -> {
                        System.out.println("Exiting...");
                        return;
                    }
                    default ->
                        System.out.println("Invalid choice. Try again.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }
    }

    private static void addBook(Connection connection, Scanner scanner) {
        try {
            System.out.print("Enter book title: ");
            String title = scanner.nextLine();
            System.out.print("Enter book author: ");
            String author = scanner.nextLine();
            System.out.print("Enter quantity: ");
            int quantity = Integer.parseInt(scanner.nextLine());

            // Check if the book already exists
            if (doesBookExist(connection, title, author)) {
                throw new BookAlreadyExistsException("Book with this title and author already exists!");
            }

            String sql = "INSERT INTO books (title, author, quantity) VALUES (?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, title);
                statement.setString(2, author);
                statement.setInt(3, quantity);
                int rows = statement.executeUpdate();
                System.out.println(rows + " book(s) added.");
            }
        } catch (BookAlreadyExistsException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("SQL error: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Invalid quantity. Please enter a number.");
        }
    }

    private static void viewBooks(Connection connection) {
        try {
            String sql = "SELECT * FROM books";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    System.out.printf("ID: %d, Title: %s, Author: %s, Quantity: %d%n",
                            resultSet.getInt("id"),
                            resultSet.getString("title"),
                            resultSet.getString("author"),
                            resultSet.getInt("quantity"));
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL error: " + e.getMessage());
        }
    }

    private static void updateBookQuantity(Connection connection, Scanner scanner) {
        try {
            System.out.print("Enter book ID: ");
            int id = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter new quantity: ");
            int quantity = Integer.parseInt(scanner.nextLine());

            if (!doesBookExistById(connection, id)) {
                throw new BookNotFoundException("Book with the specified ID does not exist!");
            }

            String sql = "UPDATE books SET quantity = ? WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, quantity);
                statement.setInt(2, id);
                int rows = statement.executeUpdate();
                System.out.println(rows + " book(s) updated.");
            }
        } catch (BookNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("SQL error: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Invalid input. Please enter a number.");
        }
    }

    private static void deleteBook(Connection connection, Scanner scanner) {
        try {
            System.out.print("Enter book ID to delete: ");
            int id = Integer.parseInt(scanner.nextLine());

            if (!doesBookExistById(connection, id)) {
                throw new BookNotFoundException("Book with the specified ID does not exist!");
            }

            String sql = "DELETE FROM books WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, id);
                int rows = statement.executeUpdate();
                System.out.println(rows + " book(s) deleted.");
            }
        } catch (BookNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("SQL error: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Invalid input. Please enter a number.");
        }
    }

    // Helper method to check if a book exists by title and author
    private static boolean doesBookExist(Connection connection, String title, String author) throws SQLException {
        String sql = "SELECT COUNT(*) FROM books WHERE title = ? AND author = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, title);
            statement.setString(2, author);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        }
        return false;
    }

    // Helper method to check if a book exists by ID
    private static boolean doesBookExistById(Connection connection, int id) throws SQLException {
        String sql = "SELECT COUNT(*) FROM books WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        }
        return false;
    }
}
