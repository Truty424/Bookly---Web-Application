// package it.unipd.bookly.dao.book;

// import java.sql.Connection;
// import java.sql.DriverManager;
// import java.sql.PreparedStatement;
// import java.sql.ResultSet;

// import org.junit.jupiter.api.AfterEach;
// import static org.junit.jupiter.api.Assertions.*;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;

// import it.unipd.bookly.Resource.Book;

// class InsertBookDAOTest {

//     private Connection connection;
//     private Book testBook;

//     @BeforeEach
//     void setUp() throws Exception {
//         connection = DriverManager.getConnection(
//                 "jdbc:postgresql://localhost:5434/BooklyDB", "postgres", "postgres");

//         // Create a test book object with all required fields
//         testBook = new Book(
//                 1,
//                 "Test Book", // title
//                 "English", // language
//                 "1234567890", // ISBN
//                 9.99, // price
//                 "1st", // edition
//                 2024, // publication year
//                 100, // number of pages
//                 10, // stock quantity
//                 4.5, // average rating
//                 "Sample book" // summary
//         );
//     }

//     @Test
//     void testInsertBook() throws Exception {
//         // Insert book using DAO
//         InsertBookDAO dao = new InsertBookDAO(connection, testBook);
//         dao.access();

//         Book inserted = dao.getOutputParam();
//         assertNotNull(inserted);

//         // Check that the book exists in DB (use the correct schema)
//         try (PreparedStatement stmt = connection.prepareStatement(
//                 "SELECT * FROM booklySchema.books WHERE title = ?")) {
//             stmt.setString(1, testBook.getTitle());
//             try (ResultSet rs = stmt.executeQuery()) {
//                 assertTrue(rs.next(), "Inserted book should be found in DB");
//                 assertEquals(testBook.getTitle(), rs.getString("title"));
//                 assertEquals(testBook.getIsbn(), rs.getString("isbn"));
//             }
//         }
//     }

//     @AfterEach
//     void tearDown() throws Exception {
//         // Clean up test data (use the correct schema)
//         try (PreparedStatement stmt = connection.prepareStatement(
//                 "DELETE FROM booklySchema.books WHERE title = ?")) {
//             stmt.setString(1, testBook.getTitle());
//             stmt.executeUpdate();
//         }
//         connection.close();
//     }
// }
