package it.unipd.bookly.dao.wishlist;

import it.unipd.bookly.Resource.Wishlist;
import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;

import static it.unipd.bookly.dao.wishlist.WishlistQueries.DELETE_WISHLIST;

/**
 * DAO class to delete a wishlist by its ID.
 */
public class DeleteWishlistDAO extends AbstractDAO<Wishlist> {

    private final Wishlist wishlist;

    /**
     * Constructor.
     *
     * @param con      the database connection
     * @param wishlist the wishlist object to be deleted
     */
    public DeleteWishlistDAO(final Connection con, final Wishlist wishlist) {
        super(con);
        this.wishlist = wishlist;
    }

    @Override
    protected void doAccess() throws Exception {
        final int wishlistId = wishlist.getWishlistId();

        try (PreparedStatement stmnt = con.prepareStatement(DELETE_WISHLIST)) {
            stmnt.setInt(1, wishlistId);

            int rowsAffected = stmnt.executeUpdate();

            if (rowsAffected > 0) {
                LOGGER.info("Wishlist with ID {} deleted successfully.", wishlistId);
                this.outputParam = wishlist;
            } else {
                LOGGER.warn("No wishlist found with ID {} to delete.", wishlistId);
                this.outputParam = null;
            }

        } catch (Exception ex) {
            LOGGER.error("Failed to delete wishlist with ID {}: {}", wishlistId, ex.getMessage());
            throw ex;
        }
    }
}
