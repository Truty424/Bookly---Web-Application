// package it.unipd.bookly.dao.user;

// import java.sql.Connection;
// import java.sql.DriverManager;
// import java.sql.PreparedStatement;
// import java.sql.ResultSet;

// import org.junit.jupiter.api.AfterEach;
// import static org.junit.jupiter.api.Assertions.*;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;

// import it.unipd.bookly.Resource.User;

// class RegisterUserDAOTest {

//     private Connection connection;
//     private final String testUsername = "register_user";
//     private final String testEmail = "register@example.com";
//     private final String testPassword = "registerpass";

//     @BeforeEach
//     void setUp() throws Exception {
//         connection = DriverManager.getConnection("jdbc:postgresql://localhost:5434/BooklyDB", "postgres", "postgres");

//         // Set correct schema
//         connection.prepareStatement("SET search_path TO booklySchema").execute();
//     }

//     @Test
//     void testRegisterUser() throws Exception {
//         User user = new User(
//                 testUsername,
//                 testPassword,
//                 "John",
//                 "Doe",
//                 testEmail,
//                 "+1234567890",
//                 "Some Street",
//                 "user",
//                 null
//         );

//         RegisterUserDAO dao = new RegisterUserDAO(connection, user);
//         dao.doAccess();

//         User output = dao.getOutputParam();
//         assertNotNull(output, "Output user should not be null");
//         assertEquals(testUsername, output.getUsername(), "Username should match inserted value");

//         try (PreparedStatement stmt = connection.prepareStatement(
//                 "SELECT * FROM booklySchema.users WHERE username = ?")) {
//             stmt.setString(1, testUsername);
//             ResultSet rs = stmt.executeQuery();
//             assertTrue(rs.next(), "User should be found in the database");
//         }
//     }

//     @AfterEach
//     void tearDown() throws Exception {
//         try (PreparedStatement stmt = connection.prepareStatement(
//                 "DELETE FROM booklySchema.users WHERE username = ?")) {
//             stmt.setString(1, testUsername);
//             stmt.executeUpdate();
//         }
//         connection.close();
//     }
// }
