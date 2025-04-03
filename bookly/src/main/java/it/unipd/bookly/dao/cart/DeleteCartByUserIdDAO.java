package it.unipd.bookly.dao.cart;

import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;

import static it.unipd.bookly.dao.cart.CartQueries.DELETE_CART_BY_USER_ID;

/**
 * DAO class to delete a cart associated with a given user.
 */
public class DeleteCartByUserIdDAO extends AbstractDAO<Void> {

    private final int userId;

    /**
     * Constructor to create DAO instance.
     *
     * @param con     the database connection.
     * @param userId  the ID of the user whose cart will be deleted.
     */
    public DeleteCartByUserIdDAO(final Connection con, final int userId) {
        super(con);
        this.userId = userId;
    }

    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmnt = con.prepareStatement(DELETE_CART_BY_USER_ID)) {
            stmnt.setInt(1, userId);

            int rows = stmnt.executeUpdate();
            LOGGER.info("Cart(s) deleted for user ID {} ({} row(s) affected).", userId, rows);

        } catch (Exception ex) {
            LOGGER.error("Error deleting cart for user ID {}: {}", userId, ex.getMessage());
            throw ex;
        }
    }
}
