// package it.unipd.bookly.dao.book;

// import java.sql.Connection;
// import java.sql.DriverManager;
// import java.sql.PreparedStatement;
// import java.sql.ResultSet;

// import org.junit.jupiter.api.AfterEach;
// import static org.junit.jupiter.api.Assertions.*;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;

// class UpdateBookStockDAOTest {

//     private Connection connection;
//     private int bookId;

//     @BeforeEach
//     void setUp() throws Exception {
//         connection = DriverManager.getConnection("jdbc:postgresql://localhost:5434/BooklyDB", "postgres", "postgres");

//         // Set schema to booklySchema
//         connection.prepareStatement("SET search_path TO booklySchema").execute();

//         // Insert a full book row with required fields
//         try (PreparedStatement stmt = connection.prepareStatement(
//                 "INSERT INTO booklySchema.books " +
//                         "(title, language, isbn, price, edition, publication_year, number_of_pages, stock_quantity, average_rate, summary) " +
//                         "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING book_id")) {
//             stmt.setString(1, "Stock Test Book");
//             stmt.setString(2, "English");
//             stmt.setString(3, "1111222233");
//             stmt.setDouble(4, 15.0);
//             stmt.setString(5, "1st");
//             stmt.setInt(6, 2023);
//             stmt.setInt(7, 150);
//             stmt.setInt(8, 5);
//             stmt.setDouble(9, 4.0);
//             stmt.setString(10, "Testing stock update");

//             ResultSet rs = stmt.executeQuery();
//             if (rs.next()) {
//                 bookId = rs.getInt("book_id");
//             } else {
//                 fail("Failed to insert test book");
//             }
//         }
//     }

//     @Test
//     void testUpdateBookStock() throws Exception {
//         int newStock = 20;

//         UpdateBookStockDAO dao = new UpdateBookStockDAO(connection, bookId, newStock);
//         dao.access();  // No return value; we verify via DB query

//         // Verify stock update
//         try (PreparedStatement stmt = connection.prepareStatement(
//                 "SELECT stock_quantity FROM booklySchema.books WHERE book_id = ?")) {
//             stmt.setInt(1, bookId);
//             ResultSet rs = stmt.executeQuery();
//             assertTrue(rs.next(), "Book should exist in DB");
//             assertEquals(newStock, rs.getInt("stock_quantity"), "Stock quantity should be updated to " + newStock);
//         }
//     }

//     @AfterEach
//     void tearDown() throws Exception {
//         try (PreparedStatement stmt = connection.prepareStatement(
//                 "DELETE FROM booklySchema.books WHERE book_id = ?")) {
//             stmt.setInt(1, bookId);
//             stmt.executeUpdate();
//         }
//         connection.close();
//     }
// }
