// package it.unipd.bookly.dao.order;

// import java.sql.Connection;
// import java.sql.DriverManager;
// import java.sql.PreparedStatement;
// import java.sql.ResultSet;

// import org.junit.jupiter.api.AfterEach;
// import static org.junit.jupiter.api.Assertions.*;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;

// class CancelOrderDAOTest {

//     private Connection connection;
//     private int testOrderId;

//     @BeforeEach
//     void setUp() throws Exception {
//         connection = DriverManager.getConnection(
//                 "jdbc:postgresql://localhost:5434/BooklyDB", "postgres", "postgres");

//         // Ensure the correct schema is used
//         connection.prepareStatement("SET search_path TO booklySchema").execute();

//         // Insert a test order (satisfying all NOT NULL fields)
//         try (PreparedStatement stmt = connection.prepareStatement(
//                 "INSERT INTO booklySchema.orders (total_price, payment_method, order_date, address, shipment_code, status) " +
//                         "VALUES (?, ?, NOW(), ?, ?, ?) RETURNING order_id")) {
//             stmt.setDouble(1, 100.0);
//             stmt.setString(2, "Test Method");
//             stmt.setString(3, "123 Test St");
//             stmt.setString(4, "SHIP123");
//             stmt.setString(5, "pending");

//             ResultSet rs = stmt.executeQuery();
//             if (rs.next()) {
//                 testOrderId = rs.getInt("order_id");
//             } else {
//                 fail("Failed to insert test order.");
//             }
//         }
//     }

//     @Test
//     void testCancelOrder() throws Exception {
//         CancelOrderDAO dao = new CancelOrderDAO(connection, testOrderId);
//         dao.access();

//         Boolean result = dao.getOutputParam();
//         assertNotNull(result);
//         assertTrue(result);

//         try (PreparedStatement stmt = connection.prepareStatement(
//                 "SELECT status FROM booklySchema.orders WHERE order_id = ?")) {
//             stmt.setInt(1, testOrderId);
//             ResultSet rs = stmt.executeQuery();
//             assertTrue(rs.next(), "Order should exist");
//             assertEquals("cancelled", rs.getString("status"), "Order status should be updated to 'cancelled'");
//         }
//     }

//     @AfterEach
//     void tearDown() throws Exception {
//         try (PreparedStatement stmt = connection.prepareStatement(
//                 "DELETE FROM booklySchema.orders WHERE order_id = ?")) {
//             stmt.setInt(1, testOrderId);
//             stmt.executeUpdate();
//         }
//         connection.close();
//     }
// }
