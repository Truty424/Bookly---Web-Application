// package it.unipd.bookly.dao.author;

// import org.junit.jupiter.api.AfterEach;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;

// import java.sql.*;

// import static org.junit.jupiter.api.Assertions.*;

// class DeleteAuthorDAOTest {
//     private Connection connection;
//     private int authorId;
//     private DeleteAuthorDAO dao;

//     @BeforeEach
//     void setUp() throws Exception {
//         connection = DriverManager.getConnection(
//                 "jdbc:postgresql://localhost:5434/BooklyDB", "postgres", "postgres");

//         // Optional: Set search_path if you prefer unqualified table names
//         connection.prepareStatement("SET search_path TO booklySchema").execute();

//         // Insert a test author
//         try (PreparedStatement stmt = connection.prepareStatement(
//                 "INSERT INTO booklySchema.authors (first_name, last_name, biography, nationality) " +
//                         "VALUES ('Test', 'Author', 'Test bio', 'Testland') RETURNING author_id")) {
//             ResultSet rs = stmt.executeQuery();
//             if (rs.next()) {
//                 authorId = rs.getInt("author_id");
//             }
//         }

//         dao = new DeleteAuthorDAO(connection, authorId);
//     }

//     @Test
//     void testDoAccess() throws Exception {
//         dao.doAccess();

//         // Verify the author was deleted
//         try (PreparedStatement stmt = connection.prepareStatement(
//                 "SELECT * FROM booklySchema.authors WHERE author_id = ?")) {
//             stmt.setInt(1, authorId);
//             ResultSet rs = stmt.executeQuery();
//             assertFalse(rs.next(), "Author was not deleted.");
//         }
//     }

//     @AfterEach
//     void tearDown() throws Exception {
//         // Just in case deletion didn't happen
//         try (PreparedStatement stmt = connection.prepareStatement(
//                 "DELETE FROM booklySchema.authors WHERE author_id = ?")) {
//             stmt.setInt(1, authorId);
//             stmt.execute();
//         }
//         connection.close();
//     }
// }
