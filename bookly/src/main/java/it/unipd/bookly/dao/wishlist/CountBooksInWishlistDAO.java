package it.unipd.bookly.dao.wishlist;

import it.unipd.bookly.Resource.Wishlist;
import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static it.unipd.bookly.dao.wishlist.WishlistQueries.COUNT_BOOKS_IN_WISHLIST;

/**
 * DAO to count how many books exist in a specific wishlist.
 */
public class CountBooksInWishlistDAO extends AbstractDAO<Integer> {

    private final int wishlist_id;

    /**
     * Constructor.
     *
     * @param con      the database connection
     * @param wishlist the wishlist to count books in
     */
    public CountBooksInWishlistDAO(final Connection con, final int wishlist_id) {
        super(con);
        this.wishlist_id = wishlist_id;
    }

    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmnt = con.prepareStatement(COUNT_BOOKS_IN_WISHLIST)) {
            stmnt.setInt(1, wishlist_id);

            try (ResultSet rs = stmnt.executeQuery()) {
                if (rs.next()) {
                    this.outputParam = rs.getInt(1);
                    LOGGER.info("Wishlist ID {} contains {} book(s).", wishlist_id, outputParam);
                } else {
                    this.outputParam = 0;
                    LOGGER.warn("No records returned for wishlist ID {}.", wishlist_id);
                }
            }

        } catch (Exception ex) {
            LOGGER.error("Error counting books in wishlist ID {}: {}", wishlist_id, ex.getMessage());
            throw ex;
        }
    }
}
