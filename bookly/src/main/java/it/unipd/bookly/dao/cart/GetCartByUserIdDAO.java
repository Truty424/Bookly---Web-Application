package it.unipd.bookly.dao.cart;

import it.unipd.bookly.Resource.Cart;
import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collections;

import static it.unipd.bookly.dao.cart.CartQueries.GET_CART_BY_USER_ID;

/**
 * DAO class to retrieve a cart for a specific user.
 */
public class GetCartByUserIdDAO extends AbstractDAO<Cart> {

    private final int userId;


    public GetCartByUserIdDAO(final Connection con, final int userId) {
        super(con);
        this.userId = userId;
    }

    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmnt = con.prepareStatement(GET_CART_BY_USER_ID)) {
            stmnt.setInt(1, userId);

            try (ResultSet rs = stmnt.executeQuery()) {
                if (rs.next()) {
                    Cart cart = new Cart(
                            rs.getInt("cart_id"),
                            rs.getInt("user_id"),
                            rs.getInt("quantity"),
                            rs.getString("shipment_method"),
                            (Integer) rs.getObject("discount_id"),
                            (Integer) rs.getObject("order_id"),
                            rs.getTimestamp("created_date"),
                            rs.getInt("total_price"),
                            Collections.emptyList() // not loading items in this DAO
                    );
                    this.outputParam = cart;
                    LOGGER.info("Cart retrieved for user ID {}.", userId);
                } else {
                    this.outputParam = null;
                    LOGGER.warn("No cart found for user ID {}.", userId);
                }
            }

        } catch (Exception ex) {
            LOGGER.error("Error retrieving cart for user ID {}: {}", userId, ex.getMessage());
            throw ex;
        }
    }
}
