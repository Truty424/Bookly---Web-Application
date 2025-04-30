package it.unipd.bookly.dao.cart;

import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;

import static it.unipd.bookly.dao.cart.CartQueries.LINK_ORDER_TO_CART;

/**
 * DAO class to link an order to a shopping cart.
 */
public class LinkOrderToCartDAO extends AbstractDAO<Void> {

    private final int cartId;
    private final int orderId;

    public LinkOrderToCartDAO(final Connection con, final int cartId, final int orderId) {
        super(con);
        this.cartId = cartId;
        this.orderId = orderId;
    }

    @Override
    protected void doAccess() throws Exception {
        boolean originalAutoCommit = con.getAutoCommit();
        con.setAutoCommit(false); // Begin transaction

        try (PreparedStatement stmnt = con.prepareStatement(LINK_ORDER_TO_CART)) {
            stmnt.setInt(1, orderId);
            stmnt.setInt(2, cartId);

            int rows = stmnt.executeUpdate();
            if (rows > 0) {
                LOGGER.info("Order {} successfully linked to cart {}.", orderId, cartId);
            } else {
                LOGGER.warn("No cart found with ID {} to link order {}.", cartId, orderId);
            }

            con.commit(); // Commit transaction

        } catch (Exception ex) {
            con.rollback(); // Rollback on failure
            LOGGER.error("Failed to link order {} to cart {}: {}", orderId, cartId, ex.getMessage());
            throw ex;
        } finally {
            con.setAutoCommit(originalAutoCommit); // Restore auto-commit
        }
    }
}
