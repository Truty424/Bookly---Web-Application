package it.unipd.bookly.dao.wishlist;

import it.unipd.bookly.Resource.Wishlist;
import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import static it.unipd.bookly.dao.wishlist.WishlistQueries.CREATE_WISHLIST;

/**
 * DAO class to create a new wishlist for a specific user.
 */
public class CreateWishlistDAO extends AbstractDAO<Wishlist> {

    private final int userId;

    /**
     * Constructor.
     *
     * @param con    the database connection
     * @param userId the ID of the user for whom the wishlist is created
     */
    public CreateWishlistDAO(final Connection con, final int userId) {
        super(con);
        this.userId = userId;
    }

    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmnt = con.prepareStatement(CREATE_WISHLIST)) {
            stmnt.setInt(1, userId);

            try (ResultSet rs = stmnt.executeQuery()) {
                if (rs.next()) {
                    final int wishlistId = rs.getInt("wishlist_id");
                    final Timestamp createdAt = rs.getTimestamp("created_at");

                    this.outputParam = new Wishlist(wishlistId, userId, createdAt);
                    LOGGER.info("Created new wishlist (ID: {}) for user ID {}.", wishlistId, userId);
                } else {
                    this.outputParam = null;
                    LOGGER.warn("Wishlist creation returned no result for user ID {}.", userId);
                }
            }

        } catch (Exception ex) {
            LOGGER.error("Failed to create wishlist for user ID {}: {}", userId, ex.getMessage());
            throw ex;
        }
    }
}
