package it.unipd.bookly.dao.cart;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import it.unipd.bookly.dao.AbstractDAO;
import static it.unipd.bookly.dao.cart.CartQueries.ADD_BOOK_TO_CART;

/**
 * DAO class to add a book to a shopping cart.
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
        String checkQuery = "SELECT 1 FROM booklySchema.contains WHERE book_id = ? AND cart_id = ?";
        try (PreparedStatement checkStmt = con.prepareStatement(checkQuery)) {
            checkStmt.setInt(1, book_id);
            checkStmt.setInt(2, cartId);

            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next()) {
                    LOGGER.warn("Book {} is already in cart {}", book_id, cartId);
                    return; // Skip inserting, already present
                }
            }
        }

        try (PreparedStatement stmt = con.prepareStatement(ADD_BOOK_TO_CART)) {
            stmt.setInt(1, book_id);
            stmt.setInt(2, cartId);
            stmt.executeUpdate();
            LOGGER.info("Book {} added to cart {}", book_id, cartId);
        }
    }
}
