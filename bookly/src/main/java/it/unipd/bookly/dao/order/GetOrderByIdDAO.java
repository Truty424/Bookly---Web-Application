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
        try (PreparedStatement stmt = con.prepareStatement(GET_ORDER_BY_ID);
             ResultSet rs = stmt.executeQuery()) {

            stmt.setInt(1, orderId);

            if (rs.next()) {
                int order_id = rs.getInt("order_id");
                double total_price = rs.getDouble("total_price");
                String payment_method = rs.getString("payment_method");
                java.sql.Timestamp order_date = rs.getTimestamp("order_date");
                String address = rs.getString("address");
                String shipment_code = rs.getString("shipment_code");
                String status = rs.getString("status");

                this.outputParam = new Order(
                        order_id,
                        total_price,
                        payment_method,
                        order_date,
                        address,
                        shipment_code,
                        status
                );
            }

        } catch (Exception e) {
            LOGGER.error("Error retrieving order by ID {}: {}", orderId, e.getMessage());
        }
    }
}