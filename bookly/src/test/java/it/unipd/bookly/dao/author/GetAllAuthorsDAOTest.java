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

// class GetAllAuthorsDAOTest {

//     private Connection connection;
//     private int testAuthorId;

//     @BeforeEach
//     void setUp() throws Exception {
//         connection = DriverManager.getConnection(
//                 "jdbc:postgresql://localhost:5434/BooklyDB", "postgres", "postgres");

//         // Set schema
//         connection.prepareStatement("SET search_path TO booklySchema").execute();

//         // Insert a test author with valid fields
//         try (PreparedStatement stmt = connection.prepareStatement(
//                 "INSERT INTO booklySchema.authors (first_name, last_name, biography, nationality) "
//                         + "VALUES (?, ?, ?, ?) RETURNING author_id")) {
//             stmt.setString(1, "Sample");
//             stmt.setString(2, "Author");
//             stmt.setString(3, "Test bio");
//             stmt.setString(4, "Testland");
//             ResultSet rs = stmt.executeQuery();
//             if (rs.next()) {
//                 testAuthorId = rs.getInt("author_id");
//             } else {
//                 fail("Failed to insert test author.");
//             }
//         }
//     }

//     @Test
//     void testDoAccess() {
//         GetAllAuthorsDAO dao = new GetAllAuthorsDAO(connection);

//         try {
//             dao.doAccess();
//         } catch (Exception e) {
//             e.printStackTrace();
//             fail("doAccess threw an exception: " + e.getMessage());
//         }

//         try {
//             List<Author> authors = dao.getOutputParam();
//             assertNotNull(authors, "Returned author list is null.");
//             assertTrue(authors.stream().anyMatch(
//                     a -> "Sample".equalsIgnoreCase(a.getFirst_name()) &&
//                          "Author".equalsIgnoreCase(a.getLast_name())
//             ), "Sample Author not found in retrieved authors.");
//         } catch (IllegalStateException e) {
//             fail("Output parameter was not set: " + e.getMessage());
//         }
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
