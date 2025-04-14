package it.unipd.bookly.dao.order;

import it.unipd.bookly.Resource.Order;
import org.junit.jupiter.api.*;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

class GetLatestOrderForUserDAOTest {

    private Connection connection;
    private int testUserId = 103;
    private int orderId;

    @BeforeEach
    void setUp() throws Exception {
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/bookly", "postgres", "postgres");

        try (PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO orders (user_id, total_price, payment_method, order_date, status) VALUES (?, ?, ?, NOW(), ?) RETURNING order_id")) {
            stmt.setInt(1, testUserId);
            stmt.setDouble(2, 123.45);
            stmt.setString(3, "LatestOrder");
            stmt.setString(4, "pending");
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) orderId = rs.getInt("order_id");
        }
    }

    @Test
    void testLatestOrder() throws Exception {
        GetLatestOrderForUserDAO dao = new GetLatestOrderForUserDAO(connection, testUserId);
        dao.access();

        Order latestOrder = dao.getOutputParam();
        assertNotNull(latestOrder);
        assertEquals(orderId, latestOrder.getOrderId());
    }

    @AfterEach
    void tearDown() throws Exception {
        try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM orders WHERE user_id = ?")) {
            stmt.setInt(1, testUserId);
            stmt.executeUpdate();
        }
        connection.close();
    }
}
