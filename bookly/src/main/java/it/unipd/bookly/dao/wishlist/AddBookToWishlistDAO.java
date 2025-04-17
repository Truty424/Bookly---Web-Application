package it.unipd.bookly.dao.wishlist;

import it.unipd.bookly.dao.AbstractDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;

import static it.unipd.bookly.dao.wishlist.WishlistQueries.ADD_BOOK_TO_WISHLIST;

/**
 * DAO to add a book to a specific wishlist.
 */
public class AddBookToWishlistDAO extends AbstractDAO<Void> {

    private final int wishlistId;
    private final int book_id;

    /**
     * Constructor.
     *
     * @param con        the database connection
     * @param wishlistId the wishlist ID
     * @param book_id     the book ID
     */
    public AddBookToWishlistDAO(final Connection con, final int wishlistId, final int book_id) {
        super(con);
        this.wishlistId = wishlistId;
        this.book_id = book_id;
    }

    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmt = con.prepareStatement(ADD_BOOK_TO_WISHLIST)) {
            stmt.setInt(1, wishlistId);
            stmt.setInt(2, book_id);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                LOGGER.info("Book ID {} added to wishlist ID {} successfully.", book_id, wishlistId);
            } else {
                LOGGER.warn("Book ID {} not added — maybe already in wishlist ID {}.", book_id, wishlistId);
            }

        } catch (Exception ex) {
            LOGGER.error("Failed to add book ID {} to wishlist ID {}: {}", book_id, wishlistId, ex.getMessage());
            throw ex;
        }
    }
}
