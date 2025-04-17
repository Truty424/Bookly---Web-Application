package it.unipd.bookly.dao.wishlist;

import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;

import static it.unipd.bookly.dao.wishlist.WishlistQueries.CLEAR_WISHLIST;

/**
 * DAO to remove all books from a specific wishlist.
 */
public class ClearWishlistDAO extends AbstractDAO<Boolean> {

    private final int wishlistId;

    /**
     * Constructor.
     *
     * @param con        the database connection
     * @param wishlistId the ID of the wishlist to be cleared
     */
    public ClearWishlistDAO(final Connection con, final int wishlistId) {
        super(con);
        this.wishlistId = wishlistId;
    }

    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmnt = con.prepareStatement(CLEAR_WISHLIST)) {
            stmnt.setInt(1, wishlistId);
            int rowsAffected = stmnt.executeUpdate();

            this.outputParam = rowsAffected > 0;

            if (outputParam) {
                LOGGER.info("Cleared wishlist ID {} — {} book(s) removed.", wishlistId, rowsAffected);
            } else {
                LOGGER.warn("Wishlist ID {} is already empty or not found.", wishlistId);
            }

        } catch (Exception ex) {
            LOGGER.error("Failed to clear wishlist ID {}: {}", wishlistId, ex.getMessage());
            throw ex;
        }
    }
}
