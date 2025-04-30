package it.unipd.bookly.dao.cart;

import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static it.unipd.bookly.dao.cart.CartQueries.ADD_BOOK_TO_CART;

/**
 * DAO class to add a book to a shopping cart with transactional safety.
 */
public class AddBookToCartDAO extends AbstractDAO<Void> {

    private final int book_id;
    private final int cartId;

    public AddBookToCartDAO(final Connection con, final int book_id, final int cartId) {
        super(con);
        this.book_id = book_id;
        this.cartId = cartId;
    }

    @Override
    protected void doAccess() throws Exception {
        final String CHECK_BOOK_IN_CART = """
            SELECT 1 FROM booklySchema.contains 
            WHERE book_id = ? AND cart_id = ?
        """;

        boolean originalAutoCommit = con.getAutoCommit();
        con.setAutoCommit(false);  // Begin transaction

        try (
            PreparedStatement checkStmt = con.prepareStatement(CHECK_BOOK_IN_CART);
            PreparedStatement insertStmt = con.prepareStatement(ADD_BOOK_TO_CART)
        ) {
            checkStmt.setInt(1, book_id);
            checkStmt.setInt(2, cartId);

            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next()) {
                    LOGGER.warn("Book ID {} already in cart ID {}. Skipping insert.", book_id, cartId);
                    con.rollback();  // Explicit rollback since no change needed
                    return;
                }
            }

            insertStmt.setInt(1, book_id);
            insertStmt.setInt(2, cartId);
            insertStmt.executeUpdate();

            con.commit();
            LOGGER.info("Book ID {} added to cart ID {}.", book_id, cartId);

        } catch (Exception e) {
            con.rollback();  // Rollback on any failure
            LOGGER.error("Failed to add book ID {} to cart ID {}: {}", book_id, cartId, e.getMessage(), e);
            throw e;
        } finally {
            con.setAutoCommit(originalAutoCommit);  // Restore original state
        }
    }
}
