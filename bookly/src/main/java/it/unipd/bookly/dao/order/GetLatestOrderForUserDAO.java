package it.unipd.bookly.dao.order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import it.unipd.bookly.Resource.Order;
import it.unipd.bookly.dao.AbstractDAO;
import static it.unipd.bookly.dao.order.OrderQueries.GET_LATEST_ORDER_FOR_USER;

/**
 * DAO to retrieve the latest order for a specific user.
 */
public class GetLatestOrderForUserDAO extends AbstractDAO<Order> {

    private final int userId;

    public GetLatestOrderForUserDAO(Connection con, int userId) {
        super(con);
        this.userId = userId;
    }

    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmt = con.prepareStatement(GET_LATEST_ORDER_FOR_USER)) {
            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int orderId = rs.getInt("order_id");
                    double totalPrice = rs.getDouble("total_price");
                    String paymentMethod = rs.getString("payment_method");
                    java.sql.Timestamp orderDate = rs.getTimestamp("order_date");
                    String address = rs.getString("address");
                    String shipmentCode = rs.getString("shipment_code");
                    String status = rs.getString("status");

                    this.outputParam = new Order(
                            orderId,
                            totalPrice,
                            paymentMethod,
                            orderDate,
                            address,
                            shipmentCode,
                            status
                    );
                } else {
                    this.outputParam = null; // No result
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error retrieving the latest order for user {}: {}", userId, e.getMessage(), e);
            throw e; // <-- Rethrow to signal failure
        }
    }
}
