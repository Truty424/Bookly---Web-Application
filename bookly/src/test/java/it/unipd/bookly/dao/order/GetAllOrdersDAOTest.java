// package it.unipd.bookly.dao.order;

// import java.sql.Connection;
// import java.sql.DriverManager;
// import java.sql.PreparedStatement;
// import java.sql.ResultSet;
// import java.util.List;

// import org.junit.jupiter.api.AfterEach;
// import static org.junit.jupiter.api.Assertions.*;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;

// import it.unipd.bookly.Resource.Order;

// class GetAllOrdersDAOTest {

//     private Connection connection;
//     private int orderId;

//     @BeforeEach
//     void setUp() throws Exception {
//         connection = DriverManager.getConnection("jdbc:postgresql://localhost:5434/BooklyDB", "postgres", "postgres");

//         // Ensure correct schema is used
//         connection.prepareStatement("SET search_path TO booklySchema").execute();

//         // Insert a valid test order
//         try (PreparedStatement stmt = connection.prepareStatement(
//                 "INSERT INTO booklySchema.orders (total_price, payment_method, order_date, address, shipment_code, status) " +
//                         "VALUES (?, ?, NOW(), ?, ?, ?) RETURNING order_id")) {
//             stmt.setDouble(1, 99.99);
//             stmt.setString(2, "GetAllTest");
//             stmt.setString(3, "123 Test Ave");
//             stmt.setString(4, "SHIP1234");
//             stmt.setString(5, "pending");
//             ResultSet rs = stmt.executeQuery();
//             if (rs.next()) {
//                 orderId = rs.getInt("order_id");
//             } else {
//                 fail("Failed to insert test order.");
//             }
//         }
//     }

//     @Test
//     void testRetrieveAllOrders() throws Exception {
//         GetAllOrdersDAO dao = new GetAllOrdersDAO(connection);
//         dao.access();

//         List<Order> orders = dao.getOutputParam();
//         assertNotNull(orders, "Order list should not be null");
//         assertTrue(orders.stream().anyMatch(o -> o.getOrderId() == orderId), "Test order should be found in result");
//     }

//     @AfterEach
//     void tearDown() throws Exception {
//         try (PreparedStatement stmt = connection.prepareStatement(
//                 "DELETE FROM booklySchema.orders WHERE order_id = ?")) {
//             stmt.setInt(1, orderId);
//             stmt.executeUpdate();
//         }
//         if (connection != null && !connection.isClosed()) {
//             connection.close();
//         }
//     }
// }
