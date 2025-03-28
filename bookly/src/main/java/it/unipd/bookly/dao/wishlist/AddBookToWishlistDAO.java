package it.unipd.bookly.dao.wishlist;

import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;

import static it.unipd.bookly.dao.wishlist.WishlistQueries.ADD_BOOK_TO_WISHLIST;

/**
 * DAO class to add a book to a specific wishlist.
 */
public class AddBookToWishlistDAO extends AbstractDAO<Boolean> {

    private final int wishlistId;
    private final int bookId;

    /**
     * Constructs a DAO to insert a book into a wishlist.
     *
     * @param con        the database connection.
     * @param wishlistId the wishlist ID.
     * @param bookId     the book ID to add.
     */
    public AddBookToWishlistDAO(final Connection con, final int wishlistId, final int bookId) {
        super(con);
        this.wishlistId = wishlistId;
        this.bookId = bookId;
    }

    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmnt = con.prepareStatement(ADD_BOOK_TO_WISHLIST)) {
            stmnt.setInt(1, wishlistId);
            stmnt.setInt(2, bookId);
            int rowsAffected = stmnt.executeUpdate();
            this.outputParam = rowsAffected > 0;
            stmnt.execute();
            LOGGER.info("Book with ID {} added to wishlist ID {}.", bookId, wishlistId);
        } catch (Exception ex) {
            LOGGER.error("Error adding book {} to wishlist {}: {}", bookId, wishlistId, ex.getMessage());
        }
    }
}
