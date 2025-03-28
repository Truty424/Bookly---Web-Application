package it.unipd.bookly.dao.wishlist;

import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static it.unipd.bookly.dao.wishlist.WishlistQueries.COUNT_BOOKS_IN_WISHLIST;

public class CountBooksInWishlistDAO extends AbstractDAO<Integer> {

    private final int wishlistId;

    /**
     * Creates a new DAO to count books in a specific wishlist.
     *
     * @param con         the connection to be used for accessing the database.
     * @param wishlistId  the ID of the wishlist to count books from.
     */
    public CountBooksInWishlistDAO(Connection con, int wishlistId) {
        super(con);
        this.wishlistId = wishlistId;
    }

    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmnt = con.prepareStatement(COUNT_BOOKS_IN_WISHLIST)) {
            stmnt.setInt(1, wishlistId);
            try (ResultSet rs = stmnt.executeQuery()) {
                if (rs.next()) {
                    this.outputParam = rs.getInt(1);
                    LOGGER.info("{} book(s) found in wishlist ID {}.", outputParam, wishlistId);
                } else {
                    this.outputParam = 0;
                    LOGGER.warn("No results returned while counting books in wishlist ID {}.", wishlistId);
                }
            }
        } catch (Exception ex) {
            LOGGER.error("Error counting books in wishlist ID {}: {}", wishlistId, ex.getMessage());
        }
    }
}
