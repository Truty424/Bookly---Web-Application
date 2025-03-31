package it.unipd.bookly.dao.wishlist;

import it.unipd.bookly.dao.AbstractDAO;
import it.unipd.bookly.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static it.unipd.bookly.dao.wishlist.WishlistQueries.REMOVE_BOOK_FROM_WISHLIST;

/**
 * DAO to remove a specific book from a given wishlist.
 */
public class RemoveBookFromWishlistDAO extends AbstractDAO<Void> {

    private final int bookId;
    private final int wishlistId;

    /**
     * Constructor.
     *
     * @param con        the database connection
     * @param bookId     the book to remove
     * @param wishlistId the wishlist from which to remove the book
     */
    public RemoveBookFromWishlistDAO(final Connection con, final int bookId, final int wishlistId) {
        super(con);

        if (bookId <= 0 || wishlistId <= 0) {
            throw new IllegalArgumentException("Both bookId and wishlistId must be positive integers.");
        }

        this.bookId = bookId;
        this.wishlistId = wishlistId;
    }

    @Override
    protected void doAccess() throws Exception {
        boolean previousAutoCommit = con.getAutoCommit();
        try {
            con.setAutoCommit(false);

            try (PreparedStatement stmnt = con.prepareStatement(REMOVE_BOOK_FROM_WISHLIST)) {
                stmnt.setInt(1, wishlistId);
                stmnt.setInt(2, bookId);

                int affectedRows = stmnt.executeUpdate();
                if (affectedRows == 0) {
                    LOGGER.warn("No entry found: book ID {} was not in wishlist ID {}.", bookId, wishlistId);
                    throw new SQLException("No matching book found in the wishlist.");
                }

                con.commit();
                LOGGER.info("Book ID {} successfully removed from wishlist ID {}.", bookId, wishlistId);

            } catch (SQLException e) {
                con.rollback();
                LOGGER.error("Database error while removing book ID {} from wishlist ID {}: {}", bookId, wishlistId, e.getMessage());
                throw new DatabaseException("Failed to remove book from wishlist.", e);
            }

        } finally {
            con.setAutoCommit(previousAutoCommit);
        }
    }
}
