package it.unipd.bookly.dao.wishlist;

import it.unipd.bookly.Resource.Wishlist;
import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static it.unipd.bookly.dao.wishlist.WishlistQueries.COUNT_BOOKS_IN_WISHLIST;

/**
 * DAO to count the number of books in a specific wishlist.
 */
public class CountBooksInWishlistDAO extends AbstractDAO<Integer> {

    private final Wishlist wishlist;

    /**
     * Constructs a DAO to count books in the given wishlist.
     *
     * @param con      the database connection.
     * @param wishlist the wishlist to count books from.
     */
    public CountBooksInWishlistDAO(Connection con, Wishlist wishlist) {
        super(con);
        this.wishlist = wishlist;
    }

    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmt = con.prepareStatement(COUNT_BOOKS_IN_WISHLIST)) {
            stmt.setInt(1, wishlist.getWishlistId());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    this.outputParam = rs.getInt(1);
                    LOGGER.info("Wishlist ID {} contains {} book(s).", wishlist.getWishlistId(), outputParam);
                } else {
                    this.outputParam = 0;
                    LOGGER.warn("Wishlist ID {} not found or empty.", wishlist.getWishlistId());
                }
            }

        } catch (Exception ex) {
            LOGGER.error("Failed to count books in wishlist ID {}: {}", wishlist.getWishlistId(), ex.getMessage());
            throw ex;
        }
    }
}
