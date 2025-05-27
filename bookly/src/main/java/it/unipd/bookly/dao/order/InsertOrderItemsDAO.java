package it.unipd.bookly.dao.order;

import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;

import static it.unipd.bookly.dao.order.OrderQueries.INSERT_ORDER_ITEMS;

/**
 * DAO to insert books from a user's cart into the order_items table after order placement.
 */
public class InsertOrderItemsDAO extends AbstractDAO<Boolean> {

    private final int cartId;
    private final int orderId;

    public InsertOrderItemsDAO(Connection con, int cartId, int orderId) {
        super(con);
        this.cartId = cartId;
        this.orderId = orderId;
    }


    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmnt = con.prepareStatement(INSERT_ORDER_ITEMS)) {
            stmnt.setInt(1, orderId);
            stmnt.setInt(2, cartId);

            int rowsInserted = stmnt.executeUpdate();
            this.outputParam = rowsInserted > 0;

            LOGGER.info("Inserted {} book(s) into order_items for order ID {}", rowsInserted, orderId);
        } catch (Exception ex) {
            this.outputParam = false;
            LOGGER.error("Error inserting books into order_items for order ID {}: {}", orderId, ex.getMessage(), ex);
            throw ex;
        }
    }

}
