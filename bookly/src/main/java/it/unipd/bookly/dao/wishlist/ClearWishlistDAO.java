package it.unipd.bookly.dao.wishlist;

import it.unipd.bookly.Resource.Wishlist;
import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;

import static it.unipd.bookly.dao.wishlist.WishlistQueries.CLEAR_WISHLIST;

/**
 * DAO to remove all books from a specific wishlist.
 */
public class ClearWishlistDAO extends AbstractDAO<Wishlist> {

    private final Wishlist wishlist;

    /**
     * Constructor.
     *
     * @param con      the database connection
     * @param wishlist the wishlist to be cleared
     */
    public ClearWishlistDAO(final Connection con, final Wishlist wishlist) {
        super(con);
        this.wishlist = wishlist;
    }

    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmnt = con.prepareStatement(CLEAR_WISHLIST)) {
            stmnt.setInt(1, wishlist.getWishlistId());

            int rowsAffected = stmnt.executeUpdate();

            if (rowsAffected > 0) {
                LOGGER.info("Cleared wishlist ID {} — {} book(s) removed.", wishlist.getWishlistId(), rowsAffected);
                this.outputParam = wishlist;
            } else {
                LOGGER.warn("Wishlist ID {} is already empty or not found.", wishlist.getWishlistId());
                this.outputParam = null;
            }

        } catch (Exception ex) {
            LOGGER.error("Failed to clear wishlist ID {}: {}", wishlist.getWishlistId(), ex.getMessage());
            throw ex;
        }
    }
}
