package it.unipd.bookly.dao.book;

import it.unipd.bookly.Resource.Book;
import org.junit.jupiter.api.*;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

class UpdateBookDAOTest {
    private Connection connection;
    private int bookId;

    @BeforeEach
    void setUp() throws Exception {
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/bookly", "postgres", "postgres");

        try (PreparedStatement stmt = connection.prepareStatement(
            "INSERT INTO book (title, language, isbn, price) VALUES (?, ?, ?, ?) RETURNING book_id")) {
            stmt.setString(1, "Old Title");
            stmt.setString(2, "English");
            stmt.setString(3, "999999999");
            stmt.setDouble(4, 12.99);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                bookId = rs.getInt("book_id");
            } else {
                fail("Failed to insert book for update test.");
            }
        }
    }

    @Test
    void testUpdateBook() throws Exception {
        // Create updated book info
        String newTitle = "Updated Title";
        String newLanguage = "English";
        String newIsbn = "999999999";
        double newPrice = 15.99;
        String edition = "2nd";
        int pubYear = 2024;
        int pages = 200;
        int stock = 20;
        double avgRate = 4.0;
        String summary = "Updated";

        UpdateBookDAO dao = new UpdateBookDAO(
                connection, bookId, newTitle, newLanguage, newIsbn, newPrice,
                edition, pubYear, pages, stock, avgRate, summary
        );
        dao.access();  // No return value, validate with DB query

        // Query DB to confirm update
        try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT * FROM book WHERE book_id = ?")) {
            stmt.setInt(1, bookId);
            ResultSet rs = stmt.executeQuery();
            assertTrue(rs.next());
            assertEquals(newTitle, rs.getString("title"));
            assertEquals(newPrice, rs.getDouble("price"));
            assertEquals(stock, rs.getInt("stock_quantity"));
            assertEquals(summary, rs.getString("summary"));
        }
    }

    @AfterEach
    void tearDown() throws Exception {
        try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM book WHERE book_id = ?")) {
            stmt.setInt(1, bookId);
            stmt.executeUpdate();
        }
        connection.close();
    }
}
