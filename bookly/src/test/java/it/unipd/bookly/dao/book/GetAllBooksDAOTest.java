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

// class GetAllBooksDAOTest {

//     private Connection connection;
//     private int insertedBookId;

//     @BeforeEach
//     void setUp() throws Exception {
//         connection = DriverManager.getConnection("jdbc:postgresql://localhost:5434/BooklyDB", "postgres", "postgres");

//         // Set schema
//         connection.prepareStatement("SET search_path TO booklySchema").execute();

//         // Insert a test book with all required fields
//         try (PreparedStatement stmt = connection.prepareStatement(
//                 "INSERT INTO booklySchema.books " +
//                         "(title, language, isbn, price, edition, publication_year, number_of_pages, stock_quantity, average_rate, summary) " +
//                         "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING book_id")) {
//             stmt.setString(1, "DAO Test Book");
//             stmt.setString(2, "English");
//             stmt.setString(3, "1234567890");
//             stmt.setDouble(4, 19.99);
//             stmt.setString(5, "1st");
//             stmt.setInt(6, 2023);
//             stmt.setInt(7, 250);
//             stmt.setInt(8, 10);
//             stmt.setDouble(9, 4.5);
//             stmt.setString(10, "A book inserted for DAO testing.");
//             ResultSet rs = stmt.executeQuery();
//             if (rs.next()) {
//                 insertedBookId = rs.getInt("book_id");
//             } else {
//                 fail("Failed to insert test book.");
//             }
//         }
//     }

//     @Test
//     void testGetAllBooks() throws Exception {
//         GetAllBooksDAO dao = new GetAllBooksDAO(connection);
//         dao.access();

//         List<Book> books = dao.getOutputParam();
//         assertNotNull(books, "Books list should not be null");
//         assertTrue(books.size() > 0, "Books list should contain at least one book");

//         boolean found = books.stream()
//                 .anyMatch(book -> book.getBookId() == insertedBookId && "DAO Test Book".equalsIgnoreCase(book.getTitle()));
//         assertTrue(found, "Inserted test book should be found in result");
//     }

//     @AfterEach
//     void tearDown() throws Exception {
//         try (PreparedStatement stmt = connection.prepareStatement(
//                 "DELETE FROM booklySchema.books WHERE book_id = ?")) {
//             stmt.setInt(1, insertedBookId);
//             stmt.executeUpdate();
//         }
//         connection.close();
//     }
// }
