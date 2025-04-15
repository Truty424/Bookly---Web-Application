// package it.unipd.bookly.dao.author;

// import java.sql.Connection;
// import java.sql.DriverManager;
// import java.sql.PreparedStatement;
// import java.sql.ResultSet;

// import org.junit.jupiter.api.AfterEach;
// import static org.junit.jupiter.api.Assertions.assertTrue;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;

// class AddAuthorToBookDAOTest {

//     private Connection connection;
//     private AddAuthorToBookDAO dao;
//     private int bookId = 1;  // Make sure this book exists
//     private int authorId = 1;  // Make sure this author exists

//     @BeforeEach
//     void setUp() throws Exception {
//         connection = DriverManager.getConnection(
//                 "jdbc:postgresql://localhost:5434/BooklyDB", "postgres", "postgres");

//         // Set schema (optional, if not globally default)
//         connection.prepareStatement("SET search_path TO booklySchema").execute();

//         // Clean up if already exists
//         try (PreparedStatement stmt = connection.prepareStatement(
//                 "DELETE FROM booklySchema.writes WHERE book_id = ? AND author_id = ?")) {
//             stmt.setInt(1, bookId);
//             stmt.setInt(2, authorId);
//             stmt.executeUpdate();
//         }

//         dao = new AddAuthorToBookDAO(connection, bookId, authorId);
//     }

//     @Test
//     void testDoAccess() throws Exception {
//         dao.doAccess();

//         // Verify insert
//         try (PreparedStatement stmt = connection.prepareStatement(
//                 "SELECT * FROM booklySchema.writes WHERE book_id = ? AND author_id = ?")) {
//             stmt.setInt(1, bookId);
//             stmt.setInt(2, authorId);
//             ResultSet rs = stmt.executeQuery();
//             assertTrue(rs.next(), "Author was not linked to the book.");
//         }
//     }

//     @AfterEach
//     void tearDown() throws Exception {
//         try (PreparedStatement stmt = connection.prepareStatement(
//                 "DELETE FROM booklySchema.writes WHERE book_id = ? AND author_id = ?")) {
//             stmt.setInt(1, bookId);
//             stmt.setInt(2, authorId);
//             stmt.execute();
//         }
//         connection.close();
//     }
// }
