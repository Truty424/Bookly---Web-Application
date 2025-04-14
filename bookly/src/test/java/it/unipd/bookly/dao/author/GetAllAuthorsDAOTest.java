package it.unipd.bookly.dao.author;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unipd.bookly.Resource.Author;

class GetAllAuthorsDAOTest {

    private Connection connection;
    private int testAuthorId;

    @BeforeEach
    void setUp() throws Exception {
        connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/bookly", "postgres", "postgres");

        // Insert a test author
        try (PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO author (name) VALUES (?) RETURNING author_id")) {
            stmt.setString(1, "Sample Author");
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                testAuthorId = rs.getInt("author_id");
            }
        }
    }

    @Test
    void testDoAccess() throws Exception {
        GetAllAuthorsDAO dao = new GetAllAuthorsDAO(connection);
        dao.doAccess();
        List<Author> authors = dao.getOutputParam();

        assertNotNull(authors, "Returned author list is null.");
        assertTrue(authors.stream().anyMatch(a -> "Sample Author".equalsIgnoreCase(a.getName())),
                "Sample Author not found in retrieved authors.");
    }

    @AfterEach
    void tearDown() throws Exception {
        try (PreparedStatement stmt = connection.prepareStatement(
                "DELETE FROM author WHERE author_id = ?")) {
            stmt.setInt(1, testAuthorId);
            stmt.executeUpdate();
        }
        connection.close();
    }
}
