package it.unipd.bookly.dao.cart;

import java.sql.Connection;
import java.sql.PreparedStatement;

import it.unipd.bookly.dao.AbstractDAO;

import static it.unipd.bookly.dao.cart.CartQueries.REMOVE_BOOK_FROM_CART;

/**
 * DAO class to remove a book from a shopping cart.
 */
public class RemoveBookFromCartDAO extends AbstractDAO<Void> {

    private final int book_id;
    private final int cartId;

    /**
     * Constructor to create DAO instance.
     *
     * @param con     the database connection.
     * @param book_id  the ID of the book to remove.
     * @param cartId  the ID of the cart to remove the book from.
     */
    public RemoveBookFromCartDAO(final Connection con, final int book_id, final int cartId) {
        super(con);
        this.book_id = book_id;
        this.cartId = cartId;
    }

    @Override
    protected void doAccess() throws Exception {
        boolean originalAutoCommit = con.getAutoCommit();
        con.setAutoCommit(false); // Begin transaction

        try (PreparedStatement stmnt = con.prepareStatement(REMOVE_BOOK_FROM_CART)) {
            stmnt.setInt(1, book_id);
            stmnt.setInt(2, cartId);

            int rows = stmnt.executeUpdate();
            if (rows > 0) {
                LOGGER.info("Book {} removed from cart {}.", book_id, cartId);
            } else {
                LOGGER.warn("No book {} found in cart {} to remove.", book_id, cartId);
            }

            con.commit(); // Commit the transaction
        } catch (Exception ex) {
            con.rollback(); // Rollback on error
            LOGGER.error("Error removing book {} from cart {}: {}", book_id, cartId, ex.getMessage());
            throw ex;
        } finally {
            con.setAutoCommit(originalAutoCommit); // Restore connection state
        }
    }
}
