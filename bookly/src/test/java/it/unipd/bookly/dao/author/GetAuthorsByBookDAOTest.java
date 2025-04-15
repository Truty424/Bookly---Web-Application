// package it.unipd.bookly.dao.author;

// import java.sql.Connection;
// import java.sql.DriverManager;
// import java.sql.PreparedStatement;
// import java.sql.ResultSet;
// import java.util.List;

// import org.junit.jupiter.api.AfterEach;
// import static org.junit.jupiter.api.Assertions.*;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;

// import it.unipd.bookly.Resource.Author;

// class GetAuthorsByBookDAOTest {

//     private Connection connection;
//     private int testAuthorId;
//     private int testBookId;

//     @BeforeEach
//     void setUp() throws Exception {
//         connection = DriverManager.getConnection(
//                 "jdbc:postgresql://localhost:5434/BooklyDB", "postgres", "postgres");

//         connection.prepareStatement("SET search_path TO booklySchema").execute();

//         // Insert a test book
//         try (PreparedStatement stmt = connection.prepareStatement(
//                 "INSERT INTO booklySchema.books (title, language, isbn, price, edition, publication_year, number_of_pages, stock_quantity, average_rate, summary) " +
//                         "VALUES ('Test Book', 'EN', '0000000000', 19.99, '1st', 2024, 100, 10, 4.5, 'Summary') RETURNING book_id")) {
//             ResultSet rs = stmt.executeQuery();
//             if (rs.next()) {
//                 testBookId = rs.getInt("book_id");
//             }
//         }

//         // Insert a test author
//         try (PreparedStatement stmt = connection.prepareStatement(
//                 "INSERT INTO booklySchema.authors (first_name, last_name, biography, nationality) " +
//                         "VALUES ('Test', 'Author', 'Bio', 'Nowhere') RETURNING author_id")) {
//             ResultSet rs = stmt.executeQuery();
//             if (rs.next()) {
//                 testAuthorId = rs.getInt("author_id");
//             }
//         }

//         // Assign author to book
//         try (PreparedStatement stmt = connection.prepareStatement(
//                 "INSERT INTO booklySchema.writes (book_id, author_id) VALUES (?, ?)")) {
//             stmt.setInt(1, testBookId);
//             stmt.setInt(2, testAuthorId);
//             stmt.executeUpdate();
//         }
//     }

//     @Test
//     void testDoAccess() throws Exception {
//         GetAuthorsByBookDAO dao = new GetAuthorsByBookDAO(connection, testBookId);
//         dao.doAccess();

//         List<Author> authors = dao.getOutputParam();
//         assertNotNull(authors);
//         assertFalse(authors.isEmpty());
//         assertTrue(authors.stream().anyMatch(a ->
//             "Test".equalsIgnoreCase(a.getFirst_name()) &&
//             "Author".equalsIgnoreCase(a.getLast_name())
//         ));
//     }

//     @AfterEach
//     void tearDown() throws Exception {
//         try (PreparedStatement stmt = connection.prepareStatement(
//                 "DELETE FROM booklySchema.writes WHERE book_id = ? AND author_id = ?")) {
//             stmt.setInt(1, testBookId);
//             stmt.setInt(2, testAuthorId);
//             stmt.executeUpdate();
//         }

//         try (PreparedStatement stmt = connection.prepareStatement(
//                 "DELETE FROM booklySchema.authors WHERE author_id = ?")) {
//             stmt.setInt(1, testAuthorId);
//             stmt.executeUpdate();
//         }

//         try (PreparedStatement stmt = connection.prepareStatement(
//                 "DELETE FROM booklySchema.books WHERE book_id = ?")) {
//             stmt.setInt(1, testBookId);
//             stmt.executeUpdate();
//         }

//         connection.close();
//     }
// }
