// package it.unipd.bookly.dao.user;

// import java.sql.Connection;
// import java.sql.DriverManager;
// import java.sql.PreparedStatement;

// import org.junit.jupiter.api.AfterEach;
// import static org.junit.jupiter.api.Assertions.*;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;

// import it.unipd.bookly.Resource.User;

// class AuthenticateUserDAOTest {

//     private Connection connection;
//     private final String testUsername = "auth_user";
//     private final String testPassword = "authpass123"; // must match password domain rule (>= 8 chars)
//     private final String testEmail = "auth@example.com";

//     @BeforeEach
//     void setUp() throws Exception {
//         connection = DriverManager.getConnection("jdbc:postgresql://localhost:5434/BooklyDB", "postgres", "postgres");

//         // Set schema
//         connection.prepareStatement("SET search_path TO booklySchema").execute();

//         try (PreparedStatement stmt = connection.prepareStatement(
//                 "INSERT INTO booklySchema.users " +
//                         "(username, password, email, first_name, last_name, phone, address, role) " +
//                         "VALUES (?, ?, ?, ?, ?, ?, ?, ?) ON CONFLICT DO NOTHING")) {
//             stmt.setString(1, testUsername);
//             stmt.setString(2, testPassword);
//             stmt.setString(3, testEmail);
//             stmt.setString(4, "Test");
//             stmt.setString(5, "User");
//             stmt.setString(6, "+1234567890");  // should satisfy phone_domain
//             stmt.setString(7, "123 Test St");
//             stmt.setString(8, "user");
//             stmt.executeUpdate();
//         }
//     }

//     @Test
//     void testAuthenticateValidUser() throws Exception {
//         AuthenticateUserDAO dao = new AuthenticateUserDAO(connection, testUsername, testPassword);
//         dao.doAccess();

//         User user = dao.getOutputParam();
//         assertNotNull(user, "Valid user should be returned");
//         assertEquals(testUsername, user.getUsername());
//     }

//     @Test
//     void testAuthenticateInvalidUser() throws Exception {
//         AuthenticateUserDAO dao = new AuthenticateUserDAO(connection, testUsername, "wrongpass");
//         dao.doAccess();

//         assertNull(dao.getOutputParam(), "Invalid password should result in null output");
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
