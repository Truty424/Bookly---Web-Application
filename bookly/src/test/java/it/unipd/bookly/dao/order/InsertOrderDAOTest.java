package it.unipd.bookly.dao.order;

import it.unipd.bookly.Resource.Order;
import org.junit.jupiter.api.*;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

class InsertOrderDAOTest {

    private Connection connection;
    private InsertOrderDAO insertOrderDAO;
    private final double testTotalPrice = 99.99;
    private final String testPaymentMethod = "Credit Card";
    private final String testStatus = "pending";

    @BeforeEach
    void setUp() throws Exception {
        connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/bookly", "postgres", "postgres");

        Order order = new Order();
        order.setTotalPrice(testTotalPrice);
        order.setPaymentMethod(testPaymentMethod);
        order.setStatus(testStatus);

        insertOrderDAO = new InsertOrderDAO(connection, order);
    }

    @Test
    void testInsertOrder() throws Exception {
        insertOrderDAO.access();
        Boolean result = insertOrderDAO.getOutputParam();
        assertTrue(result);

        try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT * FROM orders WHERE total_price = ? AND payment_method = ?")) {
            stmt.setDouble(1, testTotalPrice);
            stmt.setString(2, testPaymentMethod);
            ResultSet rs = stmt.executeQuery();
            assertTrue(rs.next());
            assertEquals(testStatus, rs.getString("status"));
        }
    }

    @AfterEach
    void tearDown() throws Exception {
        try (PreparedStatement stmt = connection.prepareStatement(
                "DELETE FROM orders WHERE total_price = ? AND payment_method = ?")) {
            stmt.setDouble(1, testTotalPrice);
            stmt.setString(2, testPaymentMethod);
            stmt.executeUpdate();
        }
        connection.close();
    }
}
