package it.unipd.bookly.dao.book;

import it.unipd.bookly.Resource.Book;
import org.junit.jupiter.api.*;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

class InsertBookDAOTest {
    private Connection connection;
    private Book testBook;

    @BeforeEach
    void setUp() throws Exception {
        connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/bookly", "postgres", "postgres");

        // Create a test book object (without author/publisher bindings here)
        testBook = new Book(
                1,
                "Test Book",        // title
                "English",          // language
                "1234567890",       // ISBN
                9.99,               // price
                "1st",              // edition
                2024,               // publication year
                100,                // number of pages
                10,                 // stock quantity
                4.5,                // average rating
                "Sample book"       // summary
        );
    }

    @Test
    void testInsertBook() throws Exception {
        // Insert book using DAO
        InsertBookDAO dao = new InsertBookDAO(connection, testBook);
        dao.access();

        Book inserted = dao.getOutputParam();
        assertNotNull(inserted);

        // Check that the book exists in DB
        try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT * FROM book WHERE title = ?")) {
            stmt.setString(1, testBook.getTitle());
            try (ResultSet rs = stmt.executeQuery()) {
                assertTrue(rs.next(), "Inserted book should be found in DB");
                assertEquals(testBook.getTitle(), rs.getString("title"));
                assertEquals(testBook.getIsbn(), rs.getString("isbn"));
            }
        }
    }

    @AfterEach
    void tearDown() throws Exception {
        // Clean up test data
        try (PreparedStatement stmt = connection.prepareStatement(
                "DELETE FROM book WHERE title = ?")) {
            stmt.setString(1, testBook.getTitle());
            stmt.executeUpdate();
        }
        connection.close();
    }
}
