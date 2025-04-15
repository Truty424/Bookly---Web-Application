// package it.unipd.bookly.dao.user;

// import java.sql.Connection;
// import java.sql.DriverManager;
// import java.sql.PreparedStatement;
// import java.sql.ResultSet;

// import org.junit.jupiter.api.AfterEach;
// import static org.junit.jupiter.api.Assertions.*;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;

// import org.apache.commons.codec.digest.DigestUtils;  // to hash passwords

// class ChangeUserPasswordDAOTest {

//     private Connection connection;
//     private int testUserId;
//     private final String testUsername = "pass_test_user";

//     @BeforeEach
//     void setUp() throws Exception {
//         connection = DriverManager.getConnection(
//             "jdbc:postgresql://localhost:5434/BooklyDB", "postgres", "postgres");

//         connection.prepareStatement("SET search_path TO booklySchema").execute();

//         // Clean up in case user already exists
//         try (PreparedStatement stmt = connection.prepareStatement(
//             "DELETE FROM booklySchema.users WHERE username = ?")) {
//             stmt.setString(1, testUsername);
//             stmt.executeUpdate();
//         }

//         // Insert test user with all required fields
//         try (PreparedStatement stmt = connection.prepareStatement(
//             "INSERT INTO booklySchema.users " +
//             "(username, password, email, first_name, last_name, phone, address, role) " +
//             "VALUES (?, ?, ?, ?, ?, ?, ?, ?::booklySchema.user_role) RETURNING user_id")) {

//             stmt.setString(1, testUsername);
//             stmt.setString(2, DigestUtils.md5Hex("old_pass"));  // hashed password
//             stmt.setString(3, "test2@example.com");
//             stmt.setString(4, "Pass");
//             stmt.setString(5, "Test");
//             stmt.setString(6, "+1234567890");
//             stmt.setString(7, "Test Street");
//             stmt.setString(8, "user");

//             ResultSet rs = stmt.executeQuery();
//             if (rs.next()) {
//                 testUserId = rs.getInt("user_id");
//             } else {
//                 fail("Failed to insert test user");
//             }
//         }
//     }

//     @Test
//     void testChangePassword() throws Exception {
//         String newHashedPassword = DigestUtils.md5Hex("new_secure_pass");

//         ChangeUserPasswordDAO dao = new ChangeUserPasswordDAO(connection, testUserId, newHashedPassword);
//         dao.doAccess();

//         assertTrue(dao.getOutputParam(), "Password update should return true");

//         // Verify in DB
//         try (PreparedStatement stmt = connection.prepareStatement(
//             "SELECT password FROM booklySchema.users WHERE user_id = ?")) {
//             stmt.setInt(1, testUserId);
//             ResultSet rs = stmt.executeQuery();
//             assertTrue(rs.next());
//             assertEquals(newHashedPassword, rs.getString("password"));
//         }
//     }

//     @AfterEach
//     void tearDown() throws Exception {
//         try (PreparedStatement stmt = connection.prepareStatement(
//             "DELETE FROM booklySchema.users WHERE user_id = ?")) {
//             stmt.setInt(1, testUserId);
//             stmt.executeUpdate();
//         }
//         connection.close();
//     }
// }
