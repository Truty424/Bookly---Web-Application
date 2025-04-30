package it.unipd.bookly.dao.cart;

import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;

import static it.unipd.bookly.dao.cart.CartQueries.CLEAR_CART;

/**
 * DAO class to clear all books from a shopping cart.
 */
public class ClearCartDAO extends AbstractDAO<Void> {

    private final int cartId;

    public ClearCartDAO(final Connection con, final int cartId) {
        super(con);
        this.cartId = cartId;
    }

    @Override
    protected void doAccess() throws Exception {
        boolean originalAutoCommit = con.getAutoCommit();
        con.setAutoCommit(false); // Start transaction

        try (PreparedStatement stmnt = con.prepareStatement(CLEAR_CART)) {
            stmnt.setInt(1, cartId);
            int rows = stmnt.executeUpdate();

            con.commit(); // Commit if successful
            LOGGER.info("Cart {} cleared — {} item(s) removed.", cartId, rows);

        } catch (Exception ex) {
            con.rollback(); // Rollback on error
            LOGGER.error("Error clearing cart {}: {}", cartId, ex.getMessage());
            throw ex;
        } finally {
            con.setAutoCommit(originalAutoCommit); // Restore auto-commit
        }
    }
}
