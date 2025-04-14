package it.unipd.bookly.dao.order;

import org.junit.jupiter.api.*;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

class CancelOrderDAOTest {

    private Connection connection;
    private int testOrderId;

    @BeforeEach
    void setUp() throws Exception {
        connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/bookly", "postgres", "postgres");

        try (PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO orders (total_price, payment_method, order_date, status) VALUES (?, ?, NOW(), ?) RETURNING order_id")) {
            stmt.setDouble(1, 100.0);
            stmt.setString(2, "Test Method");
            stmt.setString(3, "pending");
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                testOrderId = rs.getInt("order_id");
            }
        }
    }

    @Test
    void testCancelOrder() throws Exception {
        CancelOrderDAO dao = new CancelOrderDAO(connection, testOrderId);
        dao.access();

        Boolean result = dao.getOutputParam();
        assertTrue(result);

        try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT status FROM orders WHERE order_id = ?")) {
            stmt.setInt(1, testOrderId);
            ResultSet rs = stmt.executeQuery();
            assertTrue(rs.next());
            assertEquals("cancelled", rs.getString("status"));
        }
    }

    @AfterEach
    void tearDown() throws Exception {
        try (PreparedStatement stmt = connection.prepareStatement(
                "DELETE FROM orders WHERE order_id = ?")) {
            stmt.setInt(1, testOrderId);
            stmt.executeUpdate();
        }
        connection.close();
    }
}
