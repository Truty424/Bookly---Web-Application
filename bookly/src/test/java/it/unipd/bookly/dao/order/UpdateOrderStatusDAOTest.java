package it.unipd.bookly.dao.order;

import org.junit.jupiter.api.*;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

class UpdateOrderStatusDAOTest {

    private Connection connection;
    private int orderId;

    @BeforeEach
    void setUp() throws Exception {
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/bookly", "postgres", "postgres");

        try (PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO orders (total_price, payment_method, order_date, status) VALUES (?, ?, NOW(), ?) RETURNING order_id")) {
            stmt.setDouble(1, 150.0);
            stmt.setString(2, "Test");
            stmt.setString(3, "pending");
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) orderId = rs.getInt("order_id");
        }
    }

    @Test
    void testUpdateStatus() throws Exception {
        String newStatus = "shipped";
        UpdateOrderStatusDAO dao = new UpdateOrderStatusDAO(connection, orderId, newStatus);
        dao.access();

        assertTrue(dao.getOutputParam());

        try (PreparedStatement stmt = connection.prepareStatement("SELECT status FROM orders WHERE order_id = ?")) {
            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();
            assertTrue(rs.next());
            assertEquals(newStatus, rs.getString("status"));
        }
    }

    @AfterEach
    void tearDown() throws Exception {
        try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM orders WHERE order_id = ?")) {
            stmt.setInt(1, orderId);
            stmt.executeUpdate();
        }
        connection.close();
    }
}
