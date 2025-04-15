package it.unipd.bookly.dao.author;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unipd.bookly.Resource.Author;

class GetAuthorsByBookDAOTest {

    private Connection connection;
    private int testAuthorId;
    private int testBookId;

    @BeforeEach
    void setUp() throws Exception {
        connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5434/BooklyDB", "postgres", "postgres");

        // Insert a test book
        try (PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO book (title, price, author_id, publisher_id) VALUES ('Test Book', 19.99, 1, 1) RETURNING book_id")) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                testBookId = rs.getInt("book_id");
            }
        }

        // Insert a test author
        try (PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO author (name) VALUES ('Test Author') RETURNING author_id")) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                testAuthorId = rs.getInt("author_id");
            }
        }

        // Assign author to book
        try (PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO book_author (book_id, author_id) VALUES (?, ?)")) {
            stmt.setInt(1, testBookId);
            stmt.setInt(2, testAuthorId);
            stmt.executeUpdate();
        }
    }

    @Test
    void testDoAccess() throws Exception {
        GetAuthorsByBookDAO dao = new GetAuthorsByBookDAO(connection, testBookId);
        dao.doAccess();

        List<Author> authors = dao.getOutputParam();
        assertNotNull(authors);
        assertFalse(authors.isEmpty());
        assertEquals("Test Author", authors.get(0).getName());
    }

    @AfterEach
    void tearDown() throws Exception {
        try (PreparedStatement stmt = connection.prepareStatement(
                "DELETE FROM book_author WHERE book_id = ? AND author_id = ?")) {
            stmt.setInt(1, testBookId);
            stmt.setInt(2, testAuthorId);
            stmt.executeUpdate();
        }

        try (PreparedStatement stmt = connection.prepareStatement(
                "DELETE FROM author WHERE author_id = ?")) {
            stmt.setInt(1, testAuthorId);
            stmt.executeUpdate();
        }

        try (PreparedStatement stmt = connection.prepareStatement(
                "DELETE FROM book WHERE book_id = ?")) {
            stmt.setInt(1, testBookId);
            stmt.executeUpdate();
        }

        connection.close();
    }
}
