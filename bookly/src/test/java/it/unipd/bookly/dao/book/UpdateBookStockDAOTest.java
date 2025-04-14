package it.unipd.bookly.dao.book;

import org.junit.jupiter.api.*;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

class UpdateBookStockDAOTest {
    private Connection connection;
    private int bookId;

    @BeforeEach
    void setUp() throws Exception {
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/bookly", "postgres", "postgres");

        try (PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO book (title, language, isbn, price, stock_quantity) VALUES (?, ?, ?, ?, ?) RETURNING book_id")) {
            stmt.setString(1, "Stock Test Book");
            stmt.setString(2, "English");
            stmt.setString(3, "1111222233");
            stmt.setDouble(4, 15.0);
            stmt.setInt(5, 5);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                bookId = rs.getInt("book_id");
            } else {
                fail("Failed to insert test book");
            }
        }
    }

    @Test
    void testUpdateBookStock() throws Exception {
        int newStock = 20;

        UpdateBookStockDAO dao = new UpdateBookStockDAO(connection, bookId, newStock);
        dao.access();  // No return value; we verify via DB query

        // Check updated value in DB
        try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT stock_quantity FROM book WHERE book_id = ?")) {
            stmt.setInt(1, bookId);
            ResultSet rs = stmt.executeQuery();
            assertTrue(rs.next(), "Book should exist in DB");
            assertEquals(newStock, rs.getInt("stock_quantity"), "Stock quantity should be updated to " + newStock);
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
