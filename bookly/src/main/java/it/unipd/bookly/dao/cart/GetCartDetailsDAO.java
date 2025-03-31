package it.unipd.bookly.dao.cart;

import it.unipd.bookly.Resource.CartItem;
import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static it.unipd.bookly.dao.cart.CartQueries.GET_CART_DETAILS;

/**
 * DAO class to retrieve detailed cart items for a given cart ID.
 */
public class GetCartDetailsDAO extends AbstractDAO<List<CartItem>> {

    private final int cartId;


    public GetCartDetailsDAO(final Connection con, final int cartId) {
        super(con);
        this.cartId = cartId;
    }

    @Override
    protected void doAccess() throws Exception {
        List<CartItem> items = new ArrayList<>();

        try (PreparedStatement stmnt = con.prepareStatement(GET_CART_DETAILS)) {
            stmnt.setInt(1, cartId);

            try (ResultSet rs = stmnt.executeQuery()) {
                while (rs.next()) {
                    CartItem item = new CartItem(
                            rs.getInt("book_id"),
                            rs.getString("title"),
                            rs.getInt("quantity"),
                            rs.getInt("price") // Assuming price is stored as int (e.g., cents)
                    );
                    items.add(item);
                }
            }

            this.outputParam = items;
            LOGGER.info("{} item(s) retrieved from cart {}.", items.size(), cartId);

        } catch (Exception ex) {
            LOGGER.error("Error retrieving cart details for cart {}: {}", cartId, ex.getMessage());
            throw ex;
        }
    }
}
