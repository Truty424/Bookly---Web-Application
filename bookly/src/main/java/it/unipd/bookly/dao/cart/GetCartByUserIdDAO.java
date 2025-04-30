package it.unipd.bookly.dao.cart;

import it.unipd.bookly.Resource.Cart;
import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
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
                    Timestamp createdDate = rs.getTimestamp("created_date");
                    Cart cart = new Cart(
                        rs.getInt("cart_id"),
                        rs.getInt("user_id"),
                        rs.getInt("quantity"),
                        rs.getString("shipment_method"),
                        (Integer) rs.getObject("discount_id"),
                        (Integer) rs.getObject("order_id"),
                        createdDate != null ? createdDate : new Timestamp(System.currentTimeMillis()),
                        rs.getDouble("total_price"), // Use getDouble for decimal
                        Collections.emptyList() // Items can be populated in another DAO
                    );

                    this.outputParam = cart;
                    LOGGER.info("Cart retrieved successfully for user ID {}.", userId);
                } else {
                    this.outputParam = null;
                    LOGGER.warn("No cart found for user ID {}.", userId);
                }
            }

        } catch (Exception ex) {
            LOGGER.error("Failed to retrieve cart for user ID {}: {}", userId, ex.getMessage(), ex);
            throw ex;
        }
    }
}
