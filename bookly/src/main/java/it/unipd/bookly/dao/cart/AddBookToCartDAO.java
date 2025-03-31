package it.unipd.bookly.dao.cart;

import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;

import static it.unipd.bookly.dao.cart.CartQueries.ADD_BOOK_TO_CART;

/**
 * DAO class to add a book to a shopping cart.
 */
public class AddBookToCartDAO extends AbstractDAO<Void> {

    private final int bookId;
    private final int cartId;


    public AddBookToCartDAO(final Connection con, final int bookId, final int cartId) {
        super(con);
        this.bookId = bookId;
        this.cartId = cartId;
    }

    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmnt = con.prepareStatement(ADD_BOOK_TO_CART)) {
            stmnt.setInt(1, bookId);
            stmnt.setInt(2, cartId);

            stmnt.executeUpdate();
            LOGGER.info("Book {} successfully added to cart {}.", bookId, cartId);
        } catch (Exception ex) {
            LOGGER.error("Error adding book {} to cart {}: {}", bookId, cartId, ex.getMessage());
            throw ex;
        }
    }
}
