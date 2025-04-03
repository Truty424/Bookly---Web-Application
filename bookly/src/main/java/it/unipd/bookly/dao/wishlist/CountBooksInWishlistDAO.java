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

    private final Wishlist wishlist;

    /**
     * Constructor.
     *
     * @param con      the database connection
     * @param wishlist the wishlist to count books in
     */
    public CountBooksInWishlistDAO(final Connection con, final Wishlist wishlist) {
        super(con);
        this.wishlist = wishlist;
    }

    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmnt = con.prepareStatement(COUNT_BOOKS_IN_WISHLIST)) {
            stmnt.setInt(1, wishlist.getWishlistId());

            try (ResultSet rs = stmnt.executeQuery()) {
                if (rs.next()) {
                    this.outputParam = rs.getInt(1);
                    LOGGER.info("Wishlist ID {} contains {} book(s).", wishlist.getWishlistId(), outputParam);
                } else {
                    this.outputParam = 0;
                    LOGGER.warn("No records returned for wishlist ID {}.", wishlist.getWishlistId());
                }
            }

        } catch (Exception ex) {
            LOGGER.error("Error counting books in wishlist ID {}: {}", wishlist.getWishlistId(), ex.getMessage());
            throw ex;
        }
    }
}
