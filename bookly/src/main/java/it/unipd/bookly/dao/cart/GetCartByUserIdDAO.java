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

    /**
     * Constructs the DAO with the specified DB connection and user ID.
     *
     * @param con    the database connection
     * @param userId the ID of the user whose cart is to be retrieved
     */
    public GetCartByUserIdDAO(final Connection con, final int userId) {
        super(con);
        this.userId = userId;
    }

    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmt = con.prepareStatement(GET_CART_BY_USER_ID)) {
            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
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
                            Collections.emptyList() // Items can be populated in another DAO
                    );

                    this.outputParam = cart;
                    LOGGER.info(String.format("Cart retrieved successfully for user ID %d.", userId));
                } else {
                    this.outputParam = null;
                    LOGGER.warn(String.format("No cart found for user ID %d.", userId));
                }
            }

        } catch (Exception ex) {
            LOGGER.error(String.format("Failed to retrieve cart for user ID %d: %s", userId, ex.getMessage()), ex);
            throw ex;
        }
    }
}
