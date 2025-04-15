// package it.unipd.bookly.dao.order;

// import java.sql.Connection;
// import java.sql.DriverManager;
// import java.sql.PreparedStatement;
// import java.sql.ResultSet;

// import org.junit.jupiter.api.AfterEach;
// import static org.junit.jupiter.api.Assertions.*;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;

// class UpdateOrderStatusDAOTest {

//     private Connection connection;
//     private int orderId;

//     @BeforeEach
//     void setUp() throws Exception {
//         connection = DriverManager.getConnection("jdbc:postgresql://localhost:5434/BooklyDB", "postgres", "postgres");

//         // Set the schema to booklySchema
//         connection.prepareStatement("SET search_path TO booklySchema").execute();

//         // Insert a test order with all required fields
//         try (PreparedStatement stmt = connection.prepareStatement(
//                 "INSERT INTO booklySchema.orders " +
//                         "(total_price, payment_method, order_date, address, shipment_code, status) " +
//                         "VALUES (?, ?, NOW(), ?, ?, ?) RETURNING order_id")) {
//             stmt.setDouble(1, 150.0);
//             stmt.setString(2, "Test");
//             stmt.setString(3, "456 Test Road");
//             stmt.setString(4, "SHIP456");
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
//     void testUpdateStatus() throws Exception {
//         String newStatus = "shipped";

//         UpdateOrderStatusDAO dao = new UpdateOrderStatusDAO(connection, orderId, newStatus);
//         dao.access();

//         assertTrue(dao.getOutputParam(), "DAO should return true after successful update");

//         try (PreparedStatement stmt = connection.prepareStatement(
//                 "SELECT status FROM booklySchema.orders WHERE order_id = ?")) {
//             stmt.setInt(1, orderId);
//             ResultSet rs = stmt.executeQuery();
//             assertTrue(rs.next(), "Order should be found in database");
//             assertEquals(newStatus, rs.getString("status"), "Order status should match updated value");
//         }
//     }

//     @AfterEach
//     void tearDown() throws Exception {
//         try (PreparedStatement stmt = connection.prepareStatement(
//                 "DELETE FROM booklySchema.orders WHERE order_id = ?")) {
//             stmt.setInt(1, orderId);
//             stmt.executeUpdate();
//         }
//         connection.close();
//     }
// }
