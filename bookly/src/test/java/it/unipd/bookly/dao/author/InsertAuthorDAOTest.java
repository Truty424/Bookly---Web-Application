package it.unipd.bookly.dao.author;

import it.unipd.bookly.Resource.Author;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;

class InsertAuthorDAOTest {

    private Connection connection;
    private final String testAuthorFirstName = "InsertTestFirst";
    private final String testAuthorLastName = "InsertTestLast";
    private final String testAuthorBiography = "InsertTestBio";
    private final String testAuthorNationality = "InsertTestNationality";

    @BeforeEach
    void setUp() throws Exception {
        connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/bookly", "postgres", "postgres");
    }

    @Test
    void testInsertAuthor() throws Exception {
        Author author = new Author(testAuthorFirstName, testAuthorLastName, testAuthorBiography, testAuthorNationality);
        InsertAuthorDAO dao = new InsertAuthorDAO(connection, author);
        dao.access();

        Boolean inserted = dao.getOutputParam();
        assertNotNull(inserted);
        assertTrue(inserted);

        // Verify author exists in DB
        try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT first_name, last_name FROM author WHERE first_name = ? AND last_name = ?")) {
            stmt.setString(1, testAuthorFirstName);
            stmt.setString(2, testAuthorLastName);
            ResultSet rs = stmt.executeQuery();
            assertTrue(rs.next());
            assertEquals(testAuthorFirstName, rs.getString("first_name"));
            assertEquals(testAuthorLastName, rs.getString("last_name"));
        }
    }

    @AfterEach
    void tearDown() throws Exception {
        try (PreparedStatement stmt = connection.prepareStatement(
                "DELETE FROM author WHERE first_name = ? AND last_name = ?")) {
            stmt.setString(1, testAuthorFirstName);
            stmt.setString(2, testAuthorLastName);
            stmt.executeUpdate();
        }

        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
