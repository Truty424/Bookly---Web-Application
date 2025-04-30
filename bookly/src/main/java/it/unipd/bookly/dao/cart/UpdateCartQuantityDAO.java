package it.unipd.bookly.dao.cart;

import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;

import static it.unipd.bookly.dao.cart.CartQueries.UPDATE_CART_QUANTITY;

/**
 * DAO class to update the total quantity of items in a shopping cart.
 */
public class UpdateCartQuantityDAO extends AbstractDAO<Void> {

    private final int cartId;
    private final int quantity;

    public UpdateCartQuantityDAO(final Connection con, final int cartId, final int quantity) {
        super(con);
        this.cartId = cartId;
        this.quantity = quantity;
    }

    @Override
    protected void doAccess() throws Exception {
        boolean originalAutoCommit = con.getAutoCommit();
        con.setAutoCommit(false); // Begin transaction

        try (PreparedStatement stmnt = con.prepareStatement(UPDATE_CART_QUANTITY)) {
            stmnt.setInt(1, quantity);
            stmnt.setInt(2, cartId);

            int rows = stmnt.executeUpdate();
            if (rows > 0) {
                LOGGER.info("Updated cart {} with new quantity: {}.", cartId, quantity);
            } else {
                LOGGER.warn("Cart with ID {} not found. No quantity updated.", cartId);
            }

            con.commit();
        } catch (Exception ex) {
            con.rollback();
            LOGGER.error("Error updating quantity for cart {}: {}", cartId, ex.getMessage(), ex);
            throw ex;
        } finally {
            con.setAutoCommit(originalAutoCommit); // Restore connection state
        }
    }
}
