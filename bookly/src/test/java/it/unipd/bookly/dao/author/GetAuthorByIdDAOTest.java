// package it.unipd.bookly.dao.author;

// import java.sql.Connection;
// import java.sql.DriverManager;
// import java.sql.PreparedStatement;
// import java.sql.ResultSet;

// import org.junit.jupiter.api.AfterEach;
// import static org.junit.jupiter.api.Assertions.*;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;

// import it.unipd.bookly.Resource.Author;

// class GetAuthorByIdDAOTest {

//     private Connection connection;
//     private int testAuthorId;

//     @BeforeEach
//     void setUp() throws Exception {
//         connection = DriverManager.getConnection(
//                 "jdbc:postgresql://localhost:5434/BooklyDB", "postgres", "postgres");

//         // Optional: set search path
//         connection.prepareStatement("SET search_path TO booklySchema").execute();

//         // Insert test author
//         try (PreparedStatement stmt = connection.prepareStatement(
//                 "INSERT INTO booklySchema.authors (first_name, last_name, biography, nationality) " +
//                 "VALUES (?, ?, ?, ?) RETURNING author_id")) {
//             stmt.setString(1, "Test");
//             stmt.setString(2, "Author");
//             stmt.setString(3, "Bio");
//             stmt.setString(4, "Nowhere");
//             ResultSet rs = stmt.executeQuery();
//             if (rs.next()) {
//                 testAuthorId = rs.getInt("author_id");
//             }
//         }
//     }

//     @Test
//     void testDoAccess() throws Exception {
//         GetAuthorByIdDAO dao = new GetAuthorByIdDAO(connection, testAuthorId);
//         dao.doAccess();

//         Author author = dao.getOutputParam();
//         assertNotNull(author, "Returned author should not be null.");
//         assertEquals("Test", author.getFirst_name());
//         assertEquals("Author", author.getLast_name());
//         assertEquals(testAuthorId, author.getAuthor_id());
//     }

//     @AfterEach
//     void tearDown() throws Exception {
//         try (PreparedStatement stmt = connection.prepareStatement(
//                 "DELETE FROM booklySchema.authors WHERE author_id = ?")) {
//             stmt.setInt(1, testAuthorId);
//             stmt.executeUpdate();
//         }

//         connection.close();
//     }
// }
