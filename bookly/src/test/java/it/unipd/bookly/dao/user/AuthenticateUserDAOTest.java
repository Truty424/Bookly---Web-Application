package it.unipd.bookly.dao.user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unipd.bookly.Resource.User;

class AuthenticateUserDAOTest {

    private Connection connection;
    private final String testUsername = "auth_user";
    private final String testPassword = "authpass";
    private final String testEmail = "auth@example.com";

    @BeforeEach
    void setUp() throws Exception {
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5434/BooklyDB", "postgres", "postgres");

        try (PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO users (username, password, email) VALUES (?, ?, ?) ON CONFLICT DO NOTHING")) {
            stmt.setString(1, testUsername);
            stmt.setString(2, testPassword);
            stmt.setString(3, testEmail);
            stmt.executeUpdate();
        }
    }

    @Test
    void testAuthenticateValidUser() throws Exception {
        AuthenticateUserDAO dao = new AuthenticateUserDAO(connection, testUsername, testPassword);
        dao.doAccess();

        User user = dao.getOutputParam();
        assertNotNull(user);
        assertEquals(testUsername, user.getUsername());
    }

    @Test
    void testAuthenticateInvalidUser() throws Exception {
        AuthenticateUserDAO dao = new AuthenticateUserDAO(connection, testUsername, "wrongpass");
        dao.doAccess();

        assertNull(dao.getOutputParam());
    }

    @AfterEach
    void tearDown() throws Exception {
        try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM users WHERE username = ?")) {
            stmt.setString(1, testUsername);
            stmt.executeUpdate();
        }
        connection.close();
    }
}
