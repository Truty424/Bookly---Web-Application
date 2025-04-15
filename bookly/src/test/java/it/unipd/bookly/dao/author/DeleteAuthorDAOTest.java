package it.unipd.bookly.dao.author;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

class DeleteAuthorDAOTest {
    private Connection connection;
    private int authorId;
    private DeleteAuthorDAO dao;

    @BeforeEach
    void setUp() throws Exception {
        connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5434/bookly", "postgres", "postgres");

        // Insert a test author
        try (PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO author (name) VALUES (?) RETURNING author_id")) {
            stmt.setString(1, "Test Author");
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                authorId = rs.getInt("author_id");
            }
        }

        dao = new DeleteAuthorDAO(connection, authorId);
    }

    @Test
    void testDoAccess() throws Exception {
        dao.doAccess();

        // Verify the author was deleted
        try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT * FROM author WHERE author_id = ?")) {
            stmt.setInt(1, authorId);
            ResultSet rs = stmt.executeQuery();
            assertFalse(rs.next(), "Author was not deleted.");
        }
    }

    @AfterEach
    void tearDown() throws Exception {
        // Just in case deletion didn't happen, clean up
        try (PreparedStatement stmt = connection.prepareStatement(
                "DELETE FROM author WHERE author_id = ?")) {
            stmt.setInt(1, authorId);
            stmt.execute();
        }
        connection.close();
    }
}
