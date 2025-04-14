package it.unipd.bookly.dao.book;

import it.unipd.bookly.Resource.Book;
import org.junit.jupiter.api.*;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GetAllBooksDAOTest {
    private Connection connection;

    @BeforeEach
    void setUp() throws Exception {
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/bookly", "postgres", "postgres");
    }

    @Test
    void testGetAllBooks() throws Exception {
        GetAllBooksDAO dao = new GetAllBooksDAO(connection);
        dao.access();

        List<Book> books = dao.getOutputParam();
        assertNotNull(books);
        assertTrue(books.size() >= 0);
    }

    @AfterEach
    void tearDown() throws Exception {
        connection.close();
    }
}
