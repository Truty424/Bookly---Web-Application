package it.unipd.bookly.dao.cart;

import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static it.unipd.bookly.dao.cart.CartQueries.DELETE_CART_BY_USER_ID;

/**
 * DAO class to delete a cart associated with a given user.
 * This includes deleting all book entries in the cart and the cart itself.
 */
public class DeleteCartByUserIdDAO extends AbstractDAO<Void> {

    private final int userId;

    public DeleteCartByUserIdDAO(final Connection con, final int userId) {
        super(con);
        this.userId = userId;
    }

    @Override
    protected void doAccess() throws Exception {
        boolean originalAutoCommit = con.getAutoCommit();
        con.setAutoCommit(false); 

        try {
            // Step 1: Delete books from the 'contains' table for the user's cart
            String deleteFromContains = """
                DELETE FROM booklySchema.contains 
                WHERE cart_id = (
                    SELECT cart_id FROM booklySchema.shoppingcart WHERE user_id = ?
                )
            """;

            try (PreparedStatement stmt1 = con.prepareStatement(deleteFromContains)) {
                stmt1.setInt(1, userId);
                int rows1 = stmt1.executeUpdate();
                LOGGER.info("Deleted {} row(s) from 'contains' for user ID {}.", rows1, userId);
            }

            // Step 2: Delete the cart using predefined query constant
            try (PreparedStatement stmt2 = con.prepareStatement(DELETE_CART_BY_USER_ID)) {
                stmt2.setInt(1, userId);
                int rows2 = stmt2.executeUpdate();
                LOGGER.info("Deleted {} shopping cart(s) for user ID {}.", rows2, userId);
            }

            con.commit(); 
        } catch (SQLException ex) {
            con.rollback(); 
            LOGGER.error("Failed to delete cart for user ID {}: {}", userId, ex.getMessage());
            throw ex;
        } finally {
            con.setAutoCommit(originalAutoCommit);
        }
    }
}
