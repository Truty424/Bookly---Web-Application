package it.unipd.bookly.dao.wishlist;

import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static it.unipd.bookly.dao.wishlist.WishlistQueries.CREATE_WISHLIST;

/**
 * DAO class to create a new wishlist for a user.
 */
public class CreateWishlistDAO extends AbstractDAO<Integer> {

    private final int userId;

    /**
     * Constructs a DAO to create a new wishlist for the given user.
     *
     * @param con    the database connection.
     * @param userId the ID of the user who will own the wishlist.
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
                    this.outputParam = rs.getInt("wishlist_id");
                    LOGGER.info("Created wishlist ID {} for user ID {}.", outputParam, userId);
                }
            }
        } catch (Exception ex) {
            LOGGER.error("Error creating wishlist for user {}: {}", userId, ex.getMessage());
        }
    }
}
