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
        try (PreparedStatement stmt = con.prepareStatement(INSERT_ORDER)) {
            stmt.setDouble(1, order.getTotalPrice());
            stmt.setString(2, order.getPaymentMethod());
            stmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            stmt.setString(4, order.getStatus());

            var rs = stmt.executeQuery();
            if (rs.next()) {
                this.outputParam = rs.getInt("order_id");
            } else {
                throw new SQLException("Failed to insert order, no ID obtained.");
            }
        }
    }
}