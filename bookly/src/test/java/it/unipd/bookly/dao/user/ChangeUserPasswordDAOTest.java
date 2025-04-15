// package it.unipd.bookly.dao.user;

// import java.sql.Connection;
// import java.sql.DriverManager;
// import java.sql.PreparedStatement;
// import java.sql.ResultSet;

// import org.junit.jupiter.api.AfterEach;
// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertTrue;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;

// class ChangeUserPasswordDAOTest {

//     private Connection connection;
//     private int testUserId;

//     @BeforeEach
//     void setUp() throws Exception {
//         connection = DriverManager.getConnection(
//                 "jdbc:postgresql://localhost:5434/BooklyDB", "postgres", "postgres");

//         try (PreparedStatement stmt = connection.prepareStatement(
//                 "INSERT INTO users (username, password, email) VALUES ('pass_test_user', 'old_pass', 'test2@example.com') RETURNING user_id")) {
//             ResultSet rs = stmt.executeQuery();
//             if (rs.next()) {
//                 testUserId = rs.getInt("user_id");
//             }
//         }
//     }

//     @Test
//     void testChangePassword() throws Exception {
//         String newHashedPassword = "new_hashed_pass123";
//         ChangeUserPasswordDAO dao = new ChangeUserPasswordDAO(connection, testUserId, newHashedPassword);
//         dao.doAccess();

//         assertTrue(dao.getOutputParam());

//         // Verify in DB
//         try (PreparedStatement stmt = connection.prepareStatement(
//                 "SELECT password FROM users WHERE user_id = ?")) {
//             stmt.setInt(1, testUserId);
//             ResultSet rs = stmt.executeQuery();
//             assertTrue(rs.next());
//             assertEquals(newHashedPassword, rs.getString("password"));
//         }
//     }

//     @AfterEach
//     void tearDown() throws Exception {
//         try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM users WHERE user_id = ?")) {
//             stmt.setInt(1, testUserId);
//             stmt.executeUpdate();
//         }

//         connection.close();
//     }
// }
