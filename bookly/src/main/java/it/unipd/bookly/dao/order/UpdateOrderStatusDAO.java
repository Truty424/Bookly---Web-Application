package it.unipd.bookly.dao.order;

import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;

import static it.unipd.bookly.dao.order.OrderQueries.UPDATE_ORDER_STATUS;

/**
 * DAO to update the status of an order.
 */
public class UpdateOrderStatusDAO extends AbstractDAO<Boolean> {

    private final int orderId;
    private final String newStatus;

    public UpdateOrderStatusDAO(Connection con, int orderId, String newStatus) {
        super(con);
        this.orderId = orderId;
        this.newStatus = newStatus;
    }

    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmt = con.prepareStatement(UPDATE_ORDER_STATUS)) {
            stmt.setString(1, newStatus);
            stmt.setInt(2, orderId);

            int rowsUpdated = stmt.executeUpdate();
            this.outputParam = rowsUpdated > 0;

            if (outputParam) {
                LOGGER.info("Order ID {} status updated to '{}'.", orderId, newStatus);
            } else {
                LOGGER.warn("No update occurred for Order ID {}. Possibly invalid ID.", orderId);
            }
        } catch (Exception e) {
            LOGGER.error("Error updating order status for ID {}: {}", orderId, e.getMessage());
            throw e;
        }
    }
}
