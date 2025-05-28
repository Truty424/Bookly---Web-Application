package it.unipd.bookly.dao.wishlist;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import it.unipd.bookly.dao.AbstractDAO;

import static it.unipd.bookly.dao.wishlist.WishlistQueries.IS_BOOK_IN_WISHLIST;

public class IsBookInWishlistDAO extends AbstractDAO<Boolean> {

    private final int wishlistId;
    private final int bookId;

    public IsBookInWishlistDAO(Connection con, int wishlistId, int bookId) {
        super(con);
        this.wishlistId = wishlistId;
        this.bookId = bookId;
    }

    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmnt = con.prepareStatement(IS_BOOK_IN_WISHLIST)) {
            stmnt.setInt(1, wishlistId);
            stmnt.setInt(2, bookId);

            try (ResultSet rs = stmnt.executeQuery()) {
                this.outputParam = rs.next();
                LOGGER.info("Book ID {} is {}in wishlist ID {}.",
                        bookId, (outputParam ? "" : "not "), wishlistId);
            }

        } catch (Exception ex) {
            LOGGER.error("Failed to check if book ID {} is in wishlist ID {}: {}", bookId, wishlistId, ex.getMessage());
            throw ex;
        }
    }
}
