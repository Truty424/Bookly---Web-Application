package it.unipd.bookly.dao.order;

import it.unipd.bookly.Resource.Order;
import org.junit.jupiter.api.*;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

class GetOrderByIdDAOTest {

    private Connection connection;
    private int orderId;

    @BeforeEach
    void setUp() throws Exception {
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/bookly", "postgres", "postgres");

        try (PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO orders (total_price, payment_method, order_date, status) VALUES (?, ?, NOW(), ?) RETURNING order_id")) {
            stmt.setDouble(1, 120.0);
            stmt.setString(2, "GetByIdTest");
            stmt.setString(3, "pending");
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) orderId = rs.getInt("order_id");
        }
    }

    @Test
    void testRetrieveOrderById() throws Exception {
        GetOrderByIdDAO dao = new GetOrderByIdDAO(connection, orderId);
        dao.access();

        Order result = dao.getOutputParam();
        assertNotNull(result);
        assertEquals(orderId, result.getOrderId());
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
