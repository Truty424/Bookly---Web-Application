package it.unipd.bookly.dao.order;

import it.unipd.bookly.Resource.Order;
import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static it.unipd.bookly.dao.order.OrderQueries.GET_ORDER_BY_ID;

/**
 * DAO to retrieve an order by its ID.
 */
public class GetOrderByIdDAO extends AbstractDAO<Order> {

    private final int orderId;

    public GetOrderByIdDAO(final Connection con, final int orderId) {
        super(con);
        this.orderId = orderId;
    }

    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmt = con.prepareStatement(GET_ORDER_BY_ID)) {
            stmt.setInt(1, orderId);  // set parameter first

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    this.outputParam = new Order(
                            rs.getInt("order_id"),
                            rs.getDouble("total_price"),
                            rs.getString("payment_method"),
                            rs.getTimestamp("order_date"),
                            rs.getString("address"),
                            rs.getString("shipment_code"),
                            rs.getString("status")
                    );
                } else {
                    this.outputParam = null;
                    LOGGER.warn("No order found with ID {}", orderId);
                }
            }

        } catch (Exception e) {
            LOGGER.error("Error retrieving order by ID {}: {}", orderId, e.getMessage(), e);
            throw e;
        }
    }
}
