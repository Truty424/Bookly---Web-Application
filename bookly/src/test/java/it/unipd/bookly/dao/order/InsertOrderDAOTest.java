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

// class InsertOrderDAOTest {

//     private Connection connection;
//     private InsertOrderDAO insertOrderDAO;
//     private final double testTotalPrice = 99.99;
//     private final String testPaymentMethod = "Credit Card";
//     private final String testStatus = "pending";
//     private final String testAddress = "789 Book St";
//     private final String testShipmentCode = "SHIP999";

//     @BeforeEach
//     void setUp() throws Exception {
//         connection = DriverManager.getConnection(
//                 "jdbc:postgresql://localhost:5434/BooklyDB", "postgres", "postgres");

//         connection.prepareStatement("SET search_path TO booklySchema").execute();

//         Order order = new Order();
//         order.setTotalPrice(testTotalPrice);
//         order.setPaymentMethod(testPaymentMethod);
//         order.setStatus(testStatus);
//         order.setAddress(testAddress);
//         order.setShipmentCode(testShipmentCode);

//         insertOrderDAO = new InsertOrderDAO(connection, order);
//     }

//     @Test
//     void testInsertOrder() throws Exception {
//         insertOrderDAO.access();
//         Boolean result = insertOrderDAO.getOutputParam();
//         assertTrue(result, "Order should be inserted successfully.");

//         try (PreparedStatement stmt = connection.prepareStatement(
//                 "SELECT * FROM booklySchema.orders WHERE total_price = ? AND payment_method = ? AND address = ?")) {
//             stmt.setDouble(1, testTotalPrice);
//             stmt.setString(2, testPaymentMethod);
//             stmt.setString(3, testAddress);
//             ResultSet rs = stmt.executeQuery();
//             assertTrue(rs.next(), "Inserted order should be found.");
//             assertEquals(testStatus, rs.getString("status"));
//         }
//     }

//     @AfterEach
//     void tearDown() throws Exception {
//         try (PreparedStatement stmt = connection.prepareStatement(
//                 "DELETE FROM booklySchema.orders WHERE total_price = ? AND payment_method = ? AND address = ?")) {
//             stmt.setDouble(1, testTotalPrice);
//             stmt.setString(2, testPaymentMethod);
//             stmt.setString(3, testAddress);
//             stmt.executeUpdate();
//         }
//         connection.close();
//     }
// }
