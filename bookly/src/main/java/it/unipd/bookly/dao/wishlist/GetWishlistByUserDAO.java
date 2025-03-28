package it.unipd.bookly.dao.wishlist;

import it.unipd.bookly.dao.AbstractDAO;
import it.unipd.bookly.model.Wishlist;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static it.unipd.bookly.dao.wishlist.WishlistQueries.GET_WISHLIST_BY_USER;

/**
 * DAO class to retrieve a user's wishlist object.
 */
public class GetWishlistByUserDAO extends AbstractDAO<List<Wishlist>> {

    private final int userId;

    /**
     * Constructs a DAO to retrieve wishlists for a given user.
     *
     * @param con    the database connection.
     * @param userId the user ID.
     */
    public GetWishlistByUserDAO(final Connection con, final int userId) {
        super(con);
        this.userId = userId;
    }

    @Override
    protected void doAccess() throws Exception {
        List<Wishlist> wishlists = new ArrayList<>();
        try (PreparedStatement stmnt = con.prepareStatement(GET_WISHLIST_BY_USER)) {
            stmnt.setInt(1, userId);
            try (ResultSet rs = stmnt.executeQuery()) {
                while (rs.next()) {
                    Wishlist wishlist = new Wishlist(
                            rs.getInt("wishlist_id"),
                            rs.getInt("user_id"),
                            rs.getTimestamp("created_at")
                    );
                    wishlists.add(wishlist);
                }
            }
            this.outputParam = wishlists;
            LOGGER.info("{} wishlist(s) retrieved for user ID {}.", wishlists.size(), userId);
        } catch (Exception ex) {
            LOGGER.error("Error retrieving wishlist for user {}: {}", userId, ex.getMessage());
        }
    }
}
