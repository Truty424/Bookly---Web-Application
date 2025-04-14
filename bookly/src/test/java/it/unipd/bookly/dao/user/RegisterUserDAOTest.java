package it.unipd.bookly.dao.user;

import it.unipd.bookly.Resource.User;
import org.junit.jupiter.api.*;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

class RegisterUserDAOTest {

    private Connection connection;
    private final String testUsername = "register_user";
    private final String testEmail = "register@example.com";
    private final String testPassword = "registerpass";

    @BeforeEach
    void setUp() throws Exception {
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/bookly", "postgres", "postgres");
    }

    @Test
    void testRegisterUser() throws Exception {
        User user = new User(testUsername, testPassword, "John", "Doe", testEmail, "123456789", "Some Street", "user", null);
        RegisterUserDAO dao = new RegisterUserDAO(connection, user);
        dao.doAccess();

        User output = dao.getOutputParam();
        assertNotNull(output);
        assertEquals(testUsername, output.getUsername()); 

        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM users WHERE username = ?")) {
            stmt.setString(1, testUsername);
            ResultSet rs = stmt.executeQuery();
            assertTrue(rs.next());
        }
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
