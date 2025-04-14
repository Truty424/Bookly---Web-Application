package it.unipd.bookly.dao.author;

import it.unipd.bookly.Resource.Author;
import org.junit.jupiter.api.*;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

class GetAuthorByIdDAOTest {

    private Connection connection;
    private int testAuthorId;

    @BeforeEach
    void setUp() throws Exception {
        connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/bookly", "postgres", "postgres");

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
