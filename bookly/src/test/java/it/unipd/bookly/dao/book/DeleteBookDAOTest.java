// package it.unipd.bookly.dao.book;

// import java.sql.Connection;
// import java.sql.DriverManager;
// import java.sql.PreparedStatement;
// import java.sql.ResultSet;

// import org.junit.jupiter.api.AfterEach;
// import static org.junit.jupiter.api.Assertions.assertFalse;
// import static org.junit.jupiter.api.Assertions.assertTrue;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;

// class DeleteBookDAOTest {

//     private Connection connection;
//     private int bookId;

//     @BeforeEach
//     void setUp() throws Exception {
//         connection = DriverManager.getConnection("jdbc:postgresql://localhost:5434/BooklyDB", "postgres", "postgres");

//         // Set schema path
//         connection.prepareStatement("SET search_path TO booklySchema").execute();

//         // Insert full book entry into correct schema
//         try (PreparedStatement stmt = connection.prepareStatement(
//                 "INSERT INTO booklySchema.books (title, language, isbn, price, edition, publication_year, number_of_pages, stock_quantity, average_rate, summary) " +
//                         "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING book_id")) {
//             stmt.setString(1, "To Delete");
//             stmt.setString(2, "English");
//             stmt.setString(3, "999998888");
//             stmt.setDouble(4, 10.00);
//             stmt.setString(5, "1st");
//             stmt.setInt(6, 2024);
//             stmt.setInt(7, 200);
//             stmt.setInt(8, 10);
//             stmt.setDouble(9, 4.2);
//             stmt.setString(10, "A book to test deletion");

//             ResultSet rs = stmt.executeQuery();
//             if (rs.next()) {
//                 bookId = rs.getInt("book_id");
//             }
//         }
//     }

//     @Test
//     void testDeleteBook() throws Exception {
//         DeleteBookDAO dao = new DeleteBookDAO(connection, bookId);
//         dao.access();
//         assertTrue(dao.getOutputParam());

//         try (PreparedStatement stmt = connection.prepareStatement(
//                 "SELECT * FROM booklySchema.books WHERE book_id = ?")) {
//             stmt.setInt(1, bookId);
//             ResultSet rs = stmt.executeQuery();
//             assertFalse(rs.next());
//         }
//     }

//     @AfterEach
//     void tearDown() throws Exception {
//         try (PreparedStatement stmt = connection.prepareStatement(
//                 "DELETE FROM booklySchema.books WHERE book_id = ?")) {
//             stmt.setInt(1, bookId);
//             stmt.executeUpdate();
//         }

//         if (connection != null && !connection.isClosed()) {
//             connection.close();
//         }
//     }
// }
