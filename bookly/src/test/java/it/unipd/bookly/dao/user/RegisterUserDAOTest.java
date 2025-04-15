package it.unipd.bookly.dao.user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unipd.bookly.Resource.User;

class RegisterUserDAOTest {

    private Connection connection;
    private final String testUsername = "register_user";
    private final String testEmail = "register@example.com";
    private final String testPassword = "registerpass";

    @BeforeEach
    void setUp() throws Exception {
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5434/BooklyDB", "postgres", "postgres");

        // Set schema
        connection.prepareStatement("SET search_path TO booklySchema").execute();
    }

    @Test
    void testRegisterUser() throws Exception {
        // The user with all required fields
        User user = new User(testUsername, testPassword, "John", "Doe", testEmail, "+1234567890", "Some Street", "user", null);

        // Simulate DAO logic (if not using DAO directly here)
        try (PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO booklySchema.users " +
                "(username, password, first_name, last_name, email, phone, address, role) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?::booklySchema.user_role)")) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getFirst_name());
            stmt.setString(4, user.getLast_name());
            stmt.setString(5, user.getEmail());
            stmt.setString(6, user.getPhone());
            stmt.setString(7, user.getAddress());
            stmt.setString(8, user.getRole()); // this is safely casted now
            stmt.executeUpdate();
        }

        try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT * FROM booklySchema.users WHERE username = ?")) {
            stmt.setString(1, testUsername);
            ResultSet rs = stmt.executeQuery();
            assertTrue(rs.next(), "Registered user should be found.");
            assertEquals(testEmail, rs.getString("email"));
        }
    }

    @AfterEach
    void tearDown() throws Exception {
        try (PreparedStatement stmt = connection.prepareStatement(
                "DELETE FROM booklySchema.users WHERE username = ?")) {
            stmt.setString(1, testUsername);
            stmt.executeUpdate();
        }
        connection.close();
    }
}
