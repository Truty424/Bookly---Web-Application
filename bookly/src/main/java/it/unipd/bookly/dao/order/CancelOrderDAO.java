package it.unipd.bookly.dao.order;

import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;

import static it.unipd.bookly.dao.order.OrderQueries.CANCEL_ORDER;

/**
 * DAO to cancel an order by updating its status to 'cancelled'.
 */
public class CancelOrderDAO extends AbstractDAO<Boolean> {

    private final int orderId;

    public CancelOrderDAO(Connection con, int orderId) {
        super(con);
        this.orderId = orderId;
    }

    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmt = con.prepareStatement(CANCEL_ORDER)) {
            stmt.setString(1, "cancelled");  // REQUIRED: status enum as string
            stmt.setInt(2, orderId);

            int rowsUpdated = stmt.executeUpdate();
            this.outputParam = rowsUpdated > 0;

            if (outputParam) {
                LOGGER.info("Order {} successfully cancelled.", orderId);
            } else {
                LOGGER.warn("Order {} could not be cancelled (not found or already cancelled).", orderId);
            }

        } catch (Exception e) {
            LOGGER.error("Failed to cancel order {}: {}", orderId, e.getMessage());
            throw e;
        }
    }
}
