package it.unipd.bookly.dao.user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unipd.bookly.Resource.User;

import static org.junit.jupiter.api.Assertions.*;

class AuthenticateUserDAOTest {

    private Connection connection;
    private final String testEmail = "auth@example.com";
    private final String testPassword = "authpass123";
    private final String hashedPassword = DigestUtils.md5Hex(testPassword);
    private final String testUsername = "auth_user";
    private int testUserId;

    @BeforeEach
    void setUp() throws Exception {
        connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5434/BooklyDB", "postgres", "postgres");
        connection.prepareStatement("SET search_path TO booklySchema").execute();

        // Clean up any existing test user
        try (PreparedStatement stmt = connection.prepareStatement(
                "DELETE FROM booklySchema.users WHERE email = ?")) {
            stmt.setString(1, testEmail);
            stmt.executeUpdate();
        }

        // Insert user
        try (PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO booklySchema.users " +
                        "(username, password, email, first_name, last_name, phone, address, role) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?::booklySchema.user_role) RETURNING user_id")) {
            stmt.setString(1, testUsername);
            stmt.setString(2, hashedPassword);
            stmt.setString(3, testEmail);
            stmt.setString(4, "Auth");
            stmt.setString(5, "User");
            stmt.setString(6, "+1112223333");
            stmt.setString(7, "123 Auth Street");
            stmt.setString(8, "user");

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                testUserId = rs.getInt("user_id");
            } else {
                fail("Failed to insert test user");
            }
        }

        // Optional: Insert image for user
        try (PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO booklySchema.user_image (user_id, image, image_type) VALUES (?, ?, ?)")) {
            stmt.setInt(1, testUserId);
            stmt.setBytes(2, "fake_image_data".getBytes());
            stmt.setString(3, "image/png");
            stmt.executeUpdate();
        }
    }

    @Test
    void testAuthenticateSuccess() throws Exception {
        AuthenticateUserDAO dao = new AuthenticateUserDAO(connection, testEmail, hashedPassword);
        dao.doAccess();

        User user = dao.getOutputParam();
        assertNotNull(user, "Expected user object for valid credentials");
        assertEquals(testEmail, user.getEmail());
        assertEquals(testUsername, user.getUsername());
        assertNotNull(user.getProfileImage(), "User image should be returned");
        assertEquals("image/png", user.getProfileImage());
    }

    @Test
    void testAuthenticateFailure() throws Exception {
        AuthenticateUserDAO dao = new AuthenticateUserDAO(connection, testEmail, DigestUtils.md5Hex("wrongpass"));
        dao.doAccess();

        assertNull(dao.getOutputParam(), "Expected null for invalid credentials");
    }

    @AfterEach
    void tearDown() throws Exception {
        try (PreparedStatement stmt1 = connection.prepareStatement(
                "DELETE FROM booklySchema.user_image WHERE user_id = ?")) {
            stmt1.setInt(1, testUserId);
            stmt1.executeUpdate();
        }
        try (PreparedStatement stmt2 = connection.prepareStatement(
                "DELETE FROM booklySchema.users WHERE user_id = ?")) {
            stmt2.setInt(1, testUserId);
            stmt2.executeUpdate();
        }
        connection.close();
    }
}
