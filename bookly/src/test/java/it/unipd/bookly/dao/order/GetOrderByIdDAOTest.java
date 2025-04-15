// package it.unipd.bookly.dao.order;

// import java.sql.Connection;
// import java.sql.DriverManager;
// import java.sql.PreparedStatement;
// import java.sql.ResultSet;

// import org.junit.jupiter.api.AfterEach;
// import static org.junit.jupiter.api.Assertions.*;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;

// import it.unipd.bookly.Resource.Order;

// class GetOrderByIdDAOTest {

//     private Connection connection;
//     private int orderId;

//     @BeforeEach
//     void setUp() throws Exception {
//         connection = DriverManager.getConnection("jdbc:postgresql://localhost:5434/BooklyDB", "postgres", "postgres");

//         // Set schema path
//         connection.prepareStatement("SET search_path TO booklySchema").execute();

//         // Insert a test order with all required fields
//         try (PreparedStatement stmt = connection.prepareStatement(
//                 "INSERT INTO booklySchema.orders " +
//                         "(total_price, payment_method, order_date, address, shipment_code, status) " +
//                         "VALUES (?, ?, NOW(), ?, ?, ?) RETURNING order_id")) {
//             stmt.setDouble(1, 120.0);
//             stmt.setString(2, "GetByIdTest");
//             stmt.setString(3, "456 Example St");
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
//     void testRetrieveOrderById() throws Exception {
//         GetOrderByIdDAO dao = new GetOrderByIdDAO(connection, orderId);
//         dao.access();

//         Order result = dao.getOutputParam();
//         assertNotNull(result, "Returned Order should not be null");
//         assertEquals(orderId, result.getOrderId(), "Returned Order ID should match the inserted ID");
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
