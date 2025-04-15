// package it.unipd.bookly.dao.user;

// import java.sql.Connection;
// import java.sql.DriverManager;
// import java.sql.PreparedStatement;

// import org.junit.jupiter.api.AfterEach;
// import static org.junit.jupiter.api.Assertions.*;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;

// import it.unipd.bookly.Resource.User;
// import org.apache.commons.codec.digest.DigestUtils; // 🔑

// class LoginUserDAOTest {

//     private Connection connection;
//     private final String testUsername = "login_test_user";
//     private final String testPassword = "securepass123";
//     private final String testEmail = "login@example.com";

//     @BeforeEach
//     void setUp() throws Exception {
//         connection = DriverManager.getConnection("jdbc:postgresql://localhost:5434/BooklyDB", "postgres", "postgres");

//         // Set correct schema
//         connection.prepareStatement("SET search_path TO booklySchema").execute();

//         // Clean up in case user already exists
//         try (PreparedStatement stmt = connection.prepareStatement(
//                 "DELETE FROM booklySchema.users WHERE username = ?")) {
//             stmt.setString(1, testUsername);
//             stmt.executeUpdate();
//         }

//         // Insert user with hashed password
//         try (PreparedStatement stmt = connection.prepareStatement(
//                 "INSERT INTO booklySchema.users " +
//                 "(username, password, email, first_name, last_name, phone, address, role) " +
//                 "VALUES (?, ?, ?, ?, ?, ?, ?, ?::booklySchema.user_role)")) {
//             stmt.setString(1, testUsername);
//             stmt.setString(2, DigestUtils.md5Hex(testPassword));  // ✅ hashed password
//             stmt.setString(3, testEmail);
//             stmt.setString(4, "Login");
//             stmt.setString(5, "Tester");
//             stmt.setString(6, "+1112223333");
//             stmt.setString(7, "Login Street 5");
//             stmt.setString(8, "user");
//             stmt.executeUpdate();
//         }
//     }

//     @Test
//     void testLoginSuccess() throws Exception {
//         LoginUserDAO dao = new LoginUserDAO(connection, testUsername, testPassword);
//         dao.doAccess();

//         User result = dao.getOutputParam();
//         assertNotNull(result, "User should be found with correct credentials");
//         assertEquals(testUsername, result.getUsername(), "Usernames should match");
//     }

//     @Test
//     void testLoginFailure() throws Exception {
//         LoginUserDAO dao = new LoginUserDAO(connection, testUsername, "wrongpass");
//         dao.doAccess();

//         assertNull(dao.getOutputParam(), "Output should be null for wrong password");
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
