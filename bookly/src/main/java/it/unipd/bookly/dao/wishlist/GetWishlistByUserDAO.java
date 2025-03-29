package it.unipd.bookly.dao.wishlist;

import it.unipd.bookly.Resource.Wishlist;
import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static it.unipd.bookly.dao.wishlist.WishlistQueries.GET_WISHLIST_BY_USER;

/**
 * DAO class to retrieve all wishlists belonging to a specific user.
 */
public class GetWishlistByUserDAO extends AbstractDAO<List<Wishlist>> {

    private final int userId;

    /**
     * Constructor to create DAO instance.
     *
     * @param con    the database connection.
     * @param userId the user ID whose wishlists are requested.
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
            LOGGER.error("Error retrieving wishlists for user {}: {}", userId, ex.getMessage());
        }
    }
}
