package it.unipd.bookly.dao.wishlist;

import it.unipd.bookly.Resource.Book;
import it.unipd.bookly.Resource.Wishlist;
import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;

import static it.unipd.bookly.dao.wishlist.WishlistQueries.ADD_BOOK_TO_WISHLIST;

/**
 * DAO to add a book to a specific wishlist in the system.
 */
public class AddBookToWishlistDAO extends AbstractDAO<Wishlist> {
    private final Wishlist wishlist;
    private final Book book;

    /**
     * Constructs a DAO for adding a book to a wishlist.
     *
     * @param con      the DB connection to be used.
     * @param wishlist the wishlist resource to add the book to.
     * @param book     the book resource to be added.
     */
    public AddBookToWishlistDAO(final Connection con, final Wishlist wishlist, final Book book) {
        super(con);
        this.wishlist = wishlist;
        this.book = book;
    }

    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmt = con.prepareStatement(ADD_BOOK_TO_WISHLIST)) {
            stmt.setInt(1, wishlist.getWishlistId());
            stmt.setInt(2, book.getBook_id());

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                LOGGER.info("Book with ID {} added to wishlist ID {}.", book.getBook_id(), wishlist.getWishlistId());
                this.outputParam = wishlist;
            } else {
                LOGGER.warn("Book with ID {} was NOT added to wishlist ID {}.", book.getBook_id(), wishlist.getWishlistId());
                this.outputParam = null;
            }

        } catch (Exception ex) {
            LOGGER.error("Failed to add book ID {} to wishlist ID {}: {}", book.getBook_id(), wishlist.getWishlistId(), ex.getMessage());
            throw ex;
        }
    }
}
