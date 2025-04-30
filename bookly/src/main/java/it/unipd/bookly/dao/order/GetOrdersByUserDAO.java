package it.unipd.bookly.dao.order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import it.unipd.bookly.Resource.Order;
import it.unipd.bookly.dao.AbstractDAO;
import static it.unipd.bookly.dao.order.OrderQueries.GET_ORDERS_BY_USER;

/**
 * DAO to retrieve orders by a specific user.
 */
public class GetOrdersByUserDAO extends AbstractDAO<List<Order>> {

    private final int userId;

    public GetOrdersByUserDAO(Connection con, int userId) {
        super(con);
        this.userId = userId;
    }

    @Override
    protected void doAccess() throws Exception {
        List<Order> orders = new ArrayList<>();

        try (PreparedStatement stmt = con.prepareStatement(GET_ORDERS_BY_USER)) {
            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int orderId = rs.getInt("order_id");
                    double totalPrice = rs.getDouble("total_price");
                    String paymentMethod = rs.getString("payment_method");
                    java.sql.Timestamp orderDate = rs.getTimestamp("order_date");
                    String address = rs.getString("address");
                    String shipmentCode = rs.getString("shipment_code");
                    String status = rs.getString("status");

                    Order order = new Order(
                            orderId,
                            totalPrice,
                            paymentMethod,
                            orderDate,
                            address,
                            shipmentCode,
                            status
                    );

                    orders.add(order);
                }
            }
        } catch (Exception e) {
            this.outputParam = new ArrayList<>(); // Set to empty to prevent null issues
            LOGGER.error("Error retrieving orders for user {}: {}", userId, e.getMessage(), e);
            throw e;
        }

        this.outputParam = orders;
    }
}
