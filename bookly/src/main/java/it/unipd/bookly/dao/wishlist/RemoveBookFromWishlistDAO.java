package it.unipd.bookly.dao.wishlist;

import it.unipd.bookly.dao.AbstractDAO;
import it.unipd.bookly.exceptions.DatabaseException;
import java.sql.*;

import static it.unipd.bookly.dao.wishlist.WishlistQueries.REMOVE_BOOK_FROM_WISHLIST;

public class RemoveBookFromWishlistDAO extends AbstractDAO<Void> {
    private final int bookId;
    private final int wishlistId;

    public RemoveBookFromWishlistDAO(Connection con, int bookId, int wishlistId) {
        super(con);
        if (bookId <= 0 || wishlistId <= 0) {
            throw new IllegalArgumentException("IDs must be positive");
        }
        this.bookId = bookId;
        this.wishlistId = wishlistId;
    }

    @Override
    protected void doAccess() throws DatabaseException, SQLException {
        try {
            con.setAutoCommit(false);
            try (PreparedStatement stmnt = con.prepareStatement(REMOVE_BOOK_FROM_WISHLIST)) {
                stmnt.setInt(1, wishlistId);
                stmnt.setInt(2, bookId);
                
                int affectedRows = stmnt.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Book not found in wishlist");
                }
                con.commit();
                LOGGER.info("Book {} removed from wishlist {}", bookId, wishlistId);
            } catch (SQLException e) {
                con.rollback();
                LOGGER.error("DB error while removing book {} from wishlist {}", bookId, wishlistId, e);
                throw new DatabaseException("Wishlist update failed", e);
            }
        } finally {
            con.setAutoCommit(true);
        }
    }
}