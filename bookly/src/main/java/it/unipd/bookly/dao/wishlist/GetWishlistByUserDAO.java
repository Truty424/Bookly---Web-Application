package it.unipd.bookly.dao.wishlist;

import it.unipd.bookly.Resource.Wishlist;
import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static it.unipd.bookly.dao.wishlist.WishlistQueries.GET_WISHLIST_BY_USER;

/**
 * DAO class to retrieve all wishlists associated with a specific user.
 */
public class GetWishlistByUserDAO extends AbstractDAO<List<Wishlist>> {

    private final int userId;

    /**
     * Constructor.
     *
     * @param con    the database connection
     * @param userId the ID of the user whose wishlists should be retrieved
     */
    public GetWishlistByUserDAO(final Connection con, final int userId) {
        super(con);
        this.userId = userId;
    }

    @Override
    protected void doAccess() throws Exception {
        final List<Wishlist> wishlists = new ArrayList<>();

        try (PreparedStatement stmnt = con.prepareStatement(GET_WISHLIST_BY_USER)) {
            stmnt.setInt(1, userId);

            try (ResultSet rs = stmnt.executeQuery()) {
                while (rs.next()) {
                    int wishlistId = rs.getInt("wishlist_id");
                    int ownerId = rs.getInt("user_id");
                    Timestamp createdAt = rs.getTimestamp("created_at");

                    Wishlist wishlist = new Wishlist();
                    wishlist.setWishlistId(wishlistId);
                    wishlist.setUserId(ownerId);
                    wishlist.setCreatedAt(createdAt);

                    wishlists.add(wishlist);
                }
            }

            this.outputParam = wishlists;
            LOGGER.info("Retrieved {} wishlist(s) for user ID {}.", wishlists.size(), userId);

        } catch (Exception ex) {
            LOGGER.error("Failed to retrieve wishlists for user ID {}: {}", userId, ex.getMessage());
            throw ex;
        }
    }
}
