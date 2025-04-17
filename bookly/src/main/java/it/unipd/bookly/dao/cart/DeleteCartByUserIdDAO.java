package it.unipd.bookly.dao.cart;

import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * DAO class to delete a cart associated with a given user.
 */
public class DeleteCartByUserIdDAO extends AbstractDAO<Void> {

    private final int userId;

    public DeleteCartByUserIdDAO(final Connection con, final int userId) {
        super(con);
        this.userId = userId;
    }

    @Override
    protected void doAccess() throws Exception {
        try {
            // Step 1: Delete entries from `contains` where the cart_id matches the user's cart
            String deleteFromContains = """
                DELETE FROM booklySchema.contains 
                WHERE cart_id = (
                    SELECT cart_id FROM booklySchema.shoppingcart WHERE user_id = ?
                )
            """;

            try (PreparedStatement stmt1 = con.prepareStatement(deleteFromContains)) {
                stmt1.setInt(1, userId);
                int containsRows = stmt1.executeUpdate();
                LOGGER.info("Deleted {} row(s) from 'contains' for user ID {}.", containsRows, userId);
            }

            // Step 2: Delete the cart
            String deleteCart = "DELETE FROM booklySchema.shoppingcart WHERE user_id = ?";
            try (PreparedStatement stmt2 = con.prepareStatement(deleteCart)) {
                stmt2.setInt(1, userId);
                int cartRows = stmt2.executeUpdate();
                LOGGER.info("Cart(s) deleted for user ID {} ({} row(s) affected).", userId, cartRows);
            }

        } catch (Exception ex) {
            LOGGER.error("Error deleting cart for user ID {}: {}", userId, ex.getMessage());
            throw ex;
        }
    }
}
