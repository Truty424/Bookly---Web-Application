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

class LoginUserDAOTest {

    private Connection connection;
    private final String testUsername = "login_test_user";
    private final String testPassword = "securepass123";
    private final String testEmail = "login@example.com";

    @BeforeEach
    void setUp() throws Exception {
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5434/BooklyDB", "postgres", "postgres");

        try (PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO users (username, password, email) VALUES (?, ?, ?) ON CONFLICT DO NOTHING")) {
            stmt.setString(1, testUsername);
            stmt.setString(2, testPassword); // plain or hashed depending on system
            stmt.setString(3, testEmail);
            stmt.executeUpdate();
        }
    }

    @Test
    void testLoginSuccess() throws Exception {
        LoginUserDAO dao = new LoginUserDAO(connection, testUsername, testPassword);
        dao.doAccess();

        User result = dao.getOutputParam();
        assertNotNull(result);
        assertEquals(testUsername, result.getUsername());
    }

    @Test
    void testLoginFailure() throws Exception {
        LoginUserDAO dao = new LoginUserDAO(connection, testUsername, "wrongpass");
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
