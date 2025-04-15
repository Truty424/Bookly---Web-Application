// package it.unipd.bookly.dao.book;

// import java.sql.Connection;
// import java.sql.DriverManager;
// import java.sql.PreparedStatement;
// import java.sql.ResultSet;
// import java.util.List;

// import org.junit.jupiter.api.AfterEach;
// import static org.junit.jupiter.api.Assertions.*;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;

// import it.unipd.bookly.Resource.Book;

// class SearchBookByTitleDAOTest {

//     private Connection connection;
//     private final String testTitle = "Test Search Book";
//     private int insertedBookId;

//     @BeforeEach
//     void setUp() throws Exception {
//         connection = DriverManager.getConnection("jdbc:postgresql://localhost:5434/BooklyDB", "postgres", "postgres");

//         // Set correct schema search path
//         connection.prepareStatement("SET search_path TO booklySchema").execute();

//         // Insert a complete test book
//         try (PreparedStatement stmt = connection.prepareStatement(
//                 "INSERT INTO booklySchema.books (title, language, isbn, price, edition, publication_year, number_of_pages, stock_quantity, average_rate, summary) " +
//                         "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING book_id")) {
//             stmt.setString(1, testTitle);
//             stmt.setString(2, "English");
//             stmt.setString(3, "9876543210");
//             stmt.setDouble(4, 29.99);
//             stmt.setString(5, "1st");
//             stmt.setInt(6, 2024);
//             stmt.setInt(7, 200);
//             stmt.setInt(8, 15);
//             stmt.setDouble(9, 4.7);
//             stmt.setString(10, "Test summary for search");

//             ResultSet rs = stmt.executeQuery();
//             if (rs.next()) {
//                 insertedBookId = rs.getInt("book_id");
//             } else {
//                 fail("Failed to insert test book.");
//             }
//         }
//     }

//     @Test
//     void testSearchBookByTitle() throws Exception {
//         SearchBookByTitleDAO dao = new SearchBookByTitleDAO(connection, "Test Search");
//         dao.access();

//         List<Book> books = dao.getOutputParam();
//         assertNotNull(books, "Returned book list should not be null");
//         assertFalse(books.isEmpty(), "Book list should contain at least one book");

//         boolean found = books.stream()
//                 .anyMatch(book -> book.getBookId() == insertedBookId && book.getTitle().equalsIgnoreCase(testTitle));
//         assertTrue(found, "Inserted book should be found by search");
//     }

//     @AfterEach
//     void tearDown() throws Exception {
//         try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM booklySchema.books WHERE book_id = ?")) {
//             stmt.setInt(1, insertedBookId);
//             stmt.executeUpdate();
//         }
//         connection.close();
//     }
// }
