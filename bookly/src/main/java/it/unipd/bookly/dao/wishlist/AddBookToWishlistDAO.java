package it.unipd.bookly.dao.wishlist;

import it.unipd.bookly.Resource.Book;
import it.unipd.bookly.Resource.Wishlist;
import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;

import static it.unipd.bookly.dao.wishlist.WishlistQueries.ADD_BOOK_TO_WISHLIST;

/**
 * DAO to add a book to a specific wishlist.
 */
public class AddBookToWishlistDAO extends AbstractDAO<Wishlist> {

    private final Wishlist wishlist;
    private final Book book;

    /**
     * Constructor.
     *
     * @param con      the database connection
     * @param wishlist the wishlist to which the book will be added
     * @param book     the book to be added to the wishlist
     */
    public AddBookToWishlistDAO(final Connection con, final Wishlist wishlist, final Book book) {
        super(con);
        this.wishlist = wishlist;
        this.book = book;
    }

    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmnt = con.prepareStatement(ADD_BOOK_TO_WISHLIST)) {
            stmnt.setInt(1, wishlist.getWishlistId());
            stmnt.setInt(2, book.getBookId());

            int rowsAffected = stmnt.executeUpdate();

            if (rowsAffected > 0) {
                LOGGER.info("Added book ID {} to wishlist ID {}.", book.getBookId(), wishlist.getWishlistId());
                this.outputParam = wishlist;
            } else {
                LOGGER.warn("No book was added — book ID {} and wishlist ID {} may already be linked.", book.getBookId(), wishlist.getWishlistId());
                this.outputParam = null;
            }

        } catch (Exception ex) {
            LOGGER.error("Error adding book ID {} to wishlist ID {}: {}", book.getBookId(), wishlist.getWishlistId(), ex.getMessage());
            throw ex;
        }
    }
}
