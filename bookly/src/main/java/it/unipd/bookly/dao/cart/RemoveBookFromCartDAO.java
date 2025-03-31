package it.unipd.bookly.dao.cart;

import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;

import static it.unipd.bookly.dao.cart.CartQueries.REMOVE_BOOK_FROM_CART;

/**
 * DAO class to remove a book from a shopping cart.
 */
public class RemoveBookFromCartDAO extends AbstractDAO<Void> {

    private final int bookId;
    private final int cartId;

    /**
     * Constructor to create DAO instance.
     *
     * @param con     the database connection.
     * @param bookId  the ID of the book to remove.
     * @param cartId  the ID of the cart to remove the book from.
     */
    public RemoveBookFromCartDAO(final Connection con, final int bookId, final int cartId) {
        super(con);
        this.bookId = bookId;
        this.cartId = cartId;
    }

    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmnt = con.prepareStatement(REMOVE_BOOK_FROM_CART)) {
            stmnt.setInt(1, bookId);
            stmnt.setInt(2, cartId);

            int rows = stmnt.executeUpdate();
            if (rows > 0) {
                LOGGER.info("Book {} removed from cart {}.", bookId, cartId);
            } else {
                LOGGER.warn("No book {} found in cart {} to remove.", bookId, cartId);
            }

        } catch (Exception ex) {
            LOGGER.error("Error removing book {} from cart {}: {}", bookId, cartId, ex.getMessage());
            throw ex;
        }
    }
}
