package it.unipd.bookly.dao.wishlist;

import it.unipd.bookly.Resource.Wishlist;
import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;

import static it.unipd.bookly.dao.wishlist.WishlistQueries.CLEAR_WISHLIST;

/**
 * DAO to remove all books from a given wishlist.
 */
public class ClearWishlistDAO extends AbstractDAO<Wishlist> {

    private final Wishlist wishlist;

    /**
     * Creates a new DAO instance to clear all books from a wishlist.
     *
     * @param con      the DB connection to use.
     * @param wishlist the wishlist to be cleared.
     */
    public ClearWishlistDAO(final Connection con, final Wishlist wishlist) {
        super(con);
        this.wishlist = wishlist;
    }

    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmt = con.prepareStatement(CLEAR_WISHLIST)) {
            stmt.setInt(1, wishlist.getWishlistId());
            int rows = stmt.executeUpdate();

            if (rows > 0) {
                LOGGER.info("Wishlist ID {} successfully cleared. {} book(s) removed.", wishlist.getWishlistId(), rows);
                this.outputParam = wishlist;
            } else {
                LOGGER.warn("Wishlist ID {} was already empty or does not exist.", wishlist.getWishlistId());
                this.outputParam = null;
            }

        } catch (Exception ex) {
            LOGGER.error("Error clearing wishlist ID {}: {}", wishlist.getWishlistId(), ex.getMessage());
            throw ex;
        }
    }
}
