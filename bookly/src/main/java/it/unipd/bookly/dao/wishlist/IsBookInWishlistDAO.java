package it.unipd.bookly.dao.wishlist;

import it.unipd.bookly.Resource.Book;
import it.unipd.bookly.Resource.Wishlist;
import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static it.unipd.bookly.dao.wishlist.WishlistQueries.IS_BOOK_IN_WISHLIST;

/**
 * DAO class to check if a book already exists in a wishlist.
 */
public class IsBookInWishlistDAO extends AbstractDAO<Boolean> {

    private final int wishlistId;
    private final int bookId;

    /**
     * Constructs a DAO to verify if a book is in the wishlist.
     *
     * @param con      the database connection.
     * @param wishlist the wishlist to check.
     * @param book     the book to verify.
     */
    public IsBookInWishlistDAO(final Connection con, final Wishlist wishlist, final Book book) {
        super(con);
        this.wishlistId = wishlist.getWishlist_id();
        this.bookId = book.getBook_id();
    }

    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmnt = con.prepareStatement(IS_BOOK_IN_WISHLIST)) {
            stmnt.setInt(1, wishlistId);
            stmnt.setInt(2, bookId);

            try (ResultSet rs = stmnt.executeQuery()) {
                this.outputParam = rs.next();
                LOGGER.info("Book ID {} is {}in wishlist ID {}.",
                        bookId, this.outputParam ? "" : "not ", wishlistId);
            }
        } catch (Exception ex) {
            LOGGER.error("Error checking if book ID {} is in wishlist ID {}: {}",
                    bookId, wishlistId, ex.getMessage());
        }
    }
}
