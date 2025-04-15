package it.unipd.bookly.dao.order;

import it.unipd.bookly.Resource.Order;
import org.junit.jupiter.api.*;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GetAllOrdersDAOTest {

    private Connection connection;
    private int orderId;

    @BeforeEach
    void setUp() throws Exception {
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/bookly", "postgres", "postgres");

        try (PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO orders (total_price, payment_method, order_date, status) VALUES (?, ?, NOW(), ?) RETURNING order_id")) {
            stmt.setDouble(1, 99.99);
            stmt.setString(2, "GetAllTest");
            stmt.setString(3, "pending");
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) orderId = rs.getInt("order_id");
        }
    }

    @Test
    void testRetrieveAllOrders() throws Exception {
        GetAllOrdersDAO dao = new GetAllOrdersDAO(connection);
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
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
