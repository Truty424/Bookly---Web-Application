package it.unipd.bookly.dao.order;

import org.junit.jupiter.api.*;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

class UpdateOrderPaymentInfoDAOTest {

    private Connection connection;
    private int orderId;

    @BeforeEach
    void setUp() throws Exception {
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/bookly", "postgres", "postgres");

        // Insert a dummy order with initial payment info
        try (PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO orders (total_price, payment_method, order_date, status) VALUES (?, ?, NOW(), ?) RETURNING order_id")) {
            stmt.setDouble(1, 100.0);
            stmt.setString(2, "OriginalMethod");
            stmt.setString(3, "pending");
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                orderId = rs.getInt("order_id");
            }
        }
    }

    @Test
    void testUpdatePaymentInfo() throws Exception {
        double newAmount = 150.0;
        String newMethod = "CreditCard";
        Timestamp paymentDate = new Timestamp(System.currentTimeMillis());

        UpdateOrderPaymentInfoDAO dao = new UpdateOrderPaymentInfoDAO(connection, orderId, newAmount, newMethod, paymentDate);
        dao.access();

        assertTrue(dao.getOutputParam());

        // Verify updated data in DB
        try (PreparedStatement stmt = connection.prepareStatement("SELECT total_price, payment_method FROM orders WHERE order_id = ?")) {
            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();
            assertTrue(rs.next());
            assertEquals(newAmount, rs.getDouble("total_price"), 0.01);
            assertEquals(newMethod, rs.getString("payment_method"));
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
