package it.unipd.bookly.dao.order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import it.unipd.bookly.Resource.Order;
import it.unipd.bookly.dao.AbstractDAO;
import static it.unipd.bookly.dao.order.OrderQueries.GET_ORDERS_BY_STATUS;

/**
 * DAO to retrieve orders by their status.
 */
public class GetOrdersByStatusDAO extends AbstractDAO<List<Order>> {

    private final String status;

    public GetOrdersByStatusDAO(Connection con, String status) {
        super(con);
        this.status = status;
    }

    @Override
    protected void doAccess() throws Exception {
        List<Order> orders = new ArrayList<>();

        try (PreparedStatement stmt = con.prepareStatement(GET_ORDERS_BY_STATUS)) {
            stmt.setString(1, status);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int orderId = rs.getInt("order_id");
                    double totalPrice = rs.getDouble("total_price");
                    String paymentMethod = rs.getString("payment_method");
                    java.sql.Timestamp orderDate = rs.getTimestamp("order_date");
                    String address = rs.getString("address");
                    String shipmentCode = rs.getString("shipment_code");
                    String orderStatus = rs.getString("status");

                    Order order = new Order(
                        orderId,
                        totalPrice,
                        paymentMethod,
                        orderDate,
                        address,
                        shipmentCode,
                        orderStatus
                    );

                    orders.add(order);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error retrieving orders by status '{}': {}", status, e.getMessage());
        }

        this.outputParam = orders;
    }
}