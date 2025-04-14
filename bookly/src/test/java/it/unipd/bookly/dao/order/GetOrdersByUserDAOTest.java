package it.unipd.bookly.dao.order;

import it.unipd.bookly.Resource.Order;
import org.junit.jupiter.api.*;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GetOrdersByUserDAOTest {

    private Connection connection;
    private int orderId;
    private int testUserId = 101; // replace with a valid test user ID

    @BeforeEach
    void setUp() throws Exception {
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/bookly", "postgres", "postgres");

        try (PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO orders (user_id, total_price, payment_method, order_date, status) VALUES (?, ?, ?, NOW(), ?) RETURNING order_id")) {
            stmt.setInt(1, testUserId);
            stmt.setDouble(2, 80.0);
            stmt.setString(3, "ByUserTest");
            stmt.setString(4, "pending");
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) orderId = rs.getInt("order_id");
        }
    }

    @Test
    void testGetOrdersByUserId() throws Exception {
        GetOrdersByUserDAO dao = new GetOrdersByUserDAO(connection, testUserId);
        dao.access();

        List<Order> orders = dao.getOutputParam();
        assertNotNull(orders);
        assertTrue(orders.stream().anyMatch(o -> o.getOrderId() == orderId));
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
