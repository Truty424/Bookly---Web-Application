package it.unipd.bookly.dao.book;

import org.junit.jupiter.api.*;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

class DeleteBookDAOTest {
    private Connection connection;
    private int bookId;

    @BeforeEach
    void setUp() throws Exception {
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/bookly", "postgres", "postgres");

        PreparedStatement stmt = connection.prepareStatement(
            "INSERT INTO book (title, language, isbn, price) VALUES (?, ?, ?, ?) RETURNING book_id");
        stmt.setString(1, "To Delete");
        stmt.setString(2, "English");
        stmt.setString(3, "999998888");
        stmt.setDouble(4, 10.00);

        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            bookId = rs.getInt("book_id");
        }
    }

    @Test
    void testDeleteBook() throws Exception {
        DeleteBookDAO dao = new DeleteBookDAO(connection, bookId);
        dao.access();
        assertTrue(dao.getOutputParam());

        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM book WHERE book_id = ?");
        stmt.setInt(1, bookId);
        ResultSet rs = stmt.executeQuery();
        assertFalse(rs.next());
    }

    @AfterEach
    void tearDown() throws Exception {
        connection.close();
    }
}
