package it.unipd.bookly.dao.wishlist;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import it.unipd.bookly.dao.AbstractDAO;
import static it.unipd.bookly.dao.wishlist.WishlistQueries.REMOVE_BOOK_FROM_WISHLIST;
import it.unipd.bookly.exceptions.DatabaseException;

/**
 * DAO to remove a specific book from a given wishlist.
 */
public class RemoveBookFromWishlistDAO extends AbstractDAO<Void> {

    private final int book_id;
    private final int wishlistId;

    /**
     * Constructor.
     *
     * @param con the database connection
     * @param book_id the book to remove
     * @param wishlistId the wishlist from which to remove the book
     */
    public RemoveBookFromWishlistDAO(final Connection con, final int book_id, final int wishlistId) {
        super(con);

        if (book_id <= 0 || wishlistId <= 0) {
            throw new IllegalArgumentException("Both book_id and wishlistId must be positive integers.");
        }

        this.book_id = book_id;
        this.wishlistId = wishlistId;
    }

    @Override
    protected void doAccess() throws Exception {
        boolean previousAutoCommit = con.getAutoCommit();
        try {
            con.setAutoCommit(false);
            try (PreparedStatement stmnt = con.prepareStatement(REMOVE_BOOK_FROM_WISHLIST)) {
                stmnt.setInt(1, wishlistId);
                stmnt.setInt(2, book_id);

                int affectedRows = stmnt.executeUpdate();
                if (affectedRows == 0) {
                    LOGGER.warn("No entry found: book ID {} was not in wishlist ID {}.", book_id, wishlistId);
                    throw new SQLException("No matching book found in the wishlist.");
                }

                con.commit();
                LOGGER.info("Book ID {} successfully removed from wishlist ID {}.", book_id, wishlistId);

            } catch (SQLException e) {
                con.rollback();
                LOGGER.error("Database error while removing book ID {} from wishlist ID {}: {}", book_id, wishlistId, e.getMessage());
                throw new DatabaseException("Failed to remove book from wishlist.", e);
            }

        } finally {
            con.setAutoCommit(previousAutoCommit);
        }
    }
}
