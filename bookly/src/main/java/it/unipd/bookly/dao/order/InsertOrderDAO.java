package it.unipd.bookly.dao.order;

import it.unipd.bookly.Resource.Order;
import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import static it.unipd.bookly.dao.order.OrderQueries.INSERT_ORDER;

/**
 * DAO to insert a new order into the database.
 */
public class InsertOrderDAO extends AbstractDAO<Integer> {

    private final Order order;

    public InsertOrderDAO(Connection con, Order order) {
        super(con);
        this.order = order;
    }

    @Override
    protected void doAccess() throws Exception {
        boolean originalAutoCommit = con.getAutoCommit();
        try {
            con.setAutoCommit(false); 

            try (PreparedStatement stmt = con.prepareStatement(INSERT_ORDER)) {
                stmt.setDouble(1, order.getTotalPrice());
                stmt.setString(2, order.getPaymentMethod());
                stmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
                stmt.setString(4, order.getAddress());
                stmt.setString(5, order.getShipmentCode());
                stmt.setString(6, order.getStatus());

                try (var rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        int orderId = rs.getInt(1);
                        order.setOrderId(orderId);
                        this.outputParam = orderId;
                    } else {
                        throw new SQLException("Failed to insert order, no ID returned.");
                    }
                }
            }

            con.commit();
        } catch (SQLException e) {
            con.rollback();
            LOGGER.error("InsertOrderDAO - Transaction rolled back: {}", e.getMessage(), e);
            throw e;
        } finally {
            con.setAutoCommit(originalAutoCommit);
        }
    }
}
