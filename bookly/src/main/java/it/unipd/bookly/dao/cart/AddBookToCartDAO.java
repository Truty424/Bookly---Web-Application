package it.unipd.bookly.dao.cart;

import java.sql.Connection;
import java.sql.PreparedStatement;

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
        try (PreparedStatement stmnt = con.prepareStatement(ADD_BOOK_TO_CART)) {
            stmnt.setInt(1, book_id);
            stmnt.setInt(2, cartId);

            stmnt.executeUpdate();
            LOGGER.info("Book {} successfully added to cart {}.", book_id, cartId);
        } catch (Exception ex) {
            LOGGER.error("Error adding book {} to cart {}: {}", book_id, cartId, ex.getMessage());
            throw ex;
        }
    }
}
