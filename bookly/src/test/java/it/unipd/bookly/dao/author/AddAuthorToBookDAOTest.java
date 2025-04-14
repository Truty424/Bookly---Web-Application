package it.unipd.bookly.dao.author;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

class AddAuthorToBookDAOTest {
    private Connection connection;
    private AddAuthorToBookDAO dao;
    private int bookId = 1;  // You should replace with valid test data
    private int authorId = 1;

    @BeforeEach
    void setUp() throws Exception {
        connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/bookly", "postgres", "postgres");

        // Insert dummy book and author if needed for testing

        dao = new AddAuthorToBookDAO(connection, bookId, authorId);
    }

    @Test
    void testDoAccess() throws Exception {
        dao.doAccess();

        // Verify the author-book link exists
        try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT * FROM book_author WHERE book_id = ? AND author_id = ?")) {
            stmt.setInt(1, bookId);
            stmt.setInt(2, authorId);
            ResultSet rs = stmt.executeQuery();
            assertTrue(rs.next(), "Author was not added to book.");
        }
    }

    @AfterEach
    void tearDown() throws Exception {
        try (PreparedStatement stmt = connection.prepareStatement(
                "DELETE FROM book_author WHERE book_id = ? AND author_id = ?")) {
            stmt.setInt(1, bookId);
            stmt.setInt(2, authorId);
            stmt.execute();
        }
        connection.close();
    }
}
