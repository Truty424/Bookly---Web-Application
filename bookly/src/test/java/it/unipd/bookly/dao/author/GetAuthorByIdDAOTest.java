package it.unipd.bookly.dao.author;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unipd.bookly.Resource.Author;

class GetAuthorByIdDAOTest {

    private Connection connection;
    private int testAuthorId;

    @BeforeEach
    void setUp() throws Exception {
        connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5434/BooklyDB", "postgres", "postgres");

        // Insert test author
        try (PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO author (name) VALUES (?) RETURNING author_id")) {
            stmt.setString(1, "Test Author");
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                testAuthorId = rs.getInt("author_id");
            }
        }
    }

    @Test
    void testDoAccess() throws Exception {
        GetAuthorByIdDAO dao = new GetAuthorByIdDAO(connection, testAuthorId);
        dao.doAccess();

        Author author = dao.getOutputParam();
        assertNotNull(author);
        assertEquals("Test Author", author.getName());
        assertEquals(testAuthorId, author.getAuthor_id());
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
