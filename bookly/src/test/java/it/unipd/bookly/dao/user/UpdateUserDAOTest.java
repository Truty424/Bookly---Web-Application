package it.unipd.bookly.dao.user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UpdateUserDAOTest {

    private Connection connection;
    private int userId;
    private final String originalUsername = "update_test_user";

    @BeforeEach
    void setUp() throws Exception {
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5434/BooklyDB", "postgres", "postgres");

        try (PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO users (username, password, email) VALUES (?, ?, ?) RETURNING user_id")) {
            stmt.setString(1, originalUsername);
            stmt.setString(2, "updatepass");
            stmt.setString(3, "update@example.com");
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                userId = rs.getInt("user_id");
            }
        }
    }

    @Test
    void testUpdateUserDetails() throws Exception {
        String newFirstName = "NewName";
        String newLastName = "NewLast";
        String newEmail = "newemail@example.com";
        String newPhone = "0987654321";
        String newAddress = "Updated St";
        String newRole = "admin";

        UpdateUserDAO dao = new UpdateUserDAO(connection, userId, newFirstName, newLastName, newEmail, newPhone, newAddress, newRole);
        dao.access();

        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM users WHERE user_id = ?")) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            assertTrue(rs.next());
            assertEquals(newFirstName, rs.getString("first_name"));
            assertEquals(newLastName, rs.getString("last_name"));
            assertEquals(newEmail, rs.getString("email"));
            assertEquals(newPhone, rs.getString("phone"));
            assertEquals(newAddress, rs.getString("address"));
            assertEquals(newRole, rs.getString("role"));
        }
    }

    @AfterEach
    void tearDown() throws Exception {
        try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM users WHERE user_id = ?")) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        }
        connection.close();
    }
}
