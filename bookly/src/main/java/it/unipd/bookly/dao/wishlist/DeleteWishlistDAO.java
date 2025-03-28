package it.unipd.bookly.dao.wishlist;

import it.unipd.bookly.dao.AbstractDAO;
import it.unipd.bookly.model.Wishlist;

import java.sql.Connection;
import java.sql.PreparedStatement;

import static it.unipd.bookly.dao.wishlist.WishlistQueries.DELETE_WISHLIST;

/**
 * DAO class to delete a wishlist.
 */
public class DeleteWishlistDAO extends AbstractDAO<Wishlist> {

    private final Wishlist wishlist;

    /**
     * Constructs a DAO to delete a wishlist.
     *
     * @param con       the database connection.
     * @param wishlist  the Wishlist object to be deleted.
     */
    public DeleteWishlistDAO(final Connection con, final Wishlist wishlist) {
        super(con);
        this.wishlist = wishlist;
    }

    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmnt = con.prepareStatement(DELETE_WISHLIST)) {
            stmnt.setInt(1, wishlist.getWishlistId());
            stmnt.execute();
            LOGGER.info("Wishlist ID {} deleted.", wishlist.getWishlistId());
        } catch (Exception ex) {
            LOGGER.error("Error deleting wishlist ID {}: {}", wishlist.getWishlistId(), ex.getMessage());
        }
    }
}