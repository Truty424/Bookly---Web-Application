package it.unipd.bookly.dao.author;

import it.unipd.bookly.Resource.Author;
import org.junit.jupiter.api.*;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

class UpdateAuthorDAOTest {

    private Connection connection;
    private int insertedAuthorId;
    private final String originalFirstName = "UpdateTestFirst";
    private final String originalLastName = "UpdateTestLast";
    private final String updatedFirstName = "UpdatedFirstName";
    private final String updatedLastName = "UpdatedLastName";

    @BeforeEach
    void setUp() throws Exception {
        connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/bookly", "postgres", "postgres");

        // Insert a dummy author to update
        try (PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO author (first_name, last_name, biography, nationality) VALUES (?, ?, ?, ?) RETURNING author_id")) {
            stmt.setString(1, originalFirstName);
            stmt.setString(2, originalLastName);
            stmt.setString(3, "Test bio");
            stmt.setString(4, "Test nationality");
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                insertedAuthorId = rs.getInt("author_id");
            }
        }
    }

    @Test
    void testUpdateAuthor() throws Exception {
        Author updatedAuthor = new Author(
                insertedAuthorId,
                updatedFirstName,
                updatedLastName,
                "Updated bio",
                "Updated nationality"
        );

        UpdateAuthorDAO dao = new UpdateAuthorDAO(connection, updatedAuthor);
        dao.access();

        Boolean result = dao.getOutputParam();
        assertNotNull(result);
        assertTrue(result);

        // Verify update in DB
        try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT first_name, last_name FROM author WHERE author_id = ?")) {
            stmt.setInt(1, insertedAuthorId);
            ResultSet rs = stmt.executeQuery();
            assertTrue(rs.next());
            assertEquals(updatedFirstName, rs.getString("first_name"));
            assertEquals(updatedLastName, rs.getString("last_name"));
        }
    }

    @AfterEach
    void tearDown() throws Exception {
        if (insertedAuthorId > 0) {
            try (PreparedStatement stmt = connection.prepareStatement(
                    "DELETE FROM author WHERE author_id = ?")) {
                stmt.setInt(1, insertedAuthorId);
                stmt.executeUpdate();
            }
        }

        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
