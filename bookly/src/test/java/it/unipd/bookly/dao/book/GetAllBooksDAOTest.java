package it.unipd.bookly.dao.book;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unipd.bookly.Resource.Book;

class GetAllBooksDAOTest {

    private Connection connection;

    @BeforeEach
    void setUp() throws Exception {
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5434/BooklyDB", "postgres", "postgres");
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
