package it.unipd.bookly.dao.wishlist;

import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;

import static it.unipd.bookly.dao.wishlist.WishlistQueries.CLEAR_WISHLIST;

/**
 * DAO class to clear all books from a given wishlist.
 */
public class ClearWishlistDAO extends AbstractDAO<Boolean> {

    private final int wishlistId;

    /**
     * Constructs a DAO to clear a wishlist.
     *
     * @param con        the database connection.
     * @param wishlistId the wishlist ID to clear.
     */
    public ClearWishlistDAO(final Connection con, final int wishlistId) {
        super(con);
        this.wishlistId = wishlistId;
    }

    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmnt = con.prepareStatement(CLEAR_WISHLIST)) {
            stmnt.setInt(1, wishlistId);
            int rows = stmnt.executeUpdate();
            this.outputParam = rows > 0;
            stmnt.execute();
            LOGGER.info("Wishlist ID {} cleared. {} book(s) removed.", wishlistId, rows);
        } catch (Exception ex) {
            LOGGER.error("Error clearing wishlist {}: {}", wishlistId, ex.getMessage());
        }
    }
}
