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
            stmt.setInt(1, orderId);

            int rowsUpdated = stmt.executeUpdate();
            this.outputParam = rowsUpdated > 0;
        }
    }
}