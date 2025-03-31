package it.unipd.bookly.dao.cart;

import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static it.unipd.bookly.dao.cart.CartQueries.CREATE_CART_FOR_USER;

/**
 * DAO class to create a new shopping cart for a user.
 */
public class CreateCartForUserDAO extends AbstractDAO<Integer> {

    private final int userId;
    private final String shipmentMethod;


    public CreateCartForUserDAO(final Connection con, final int userId, final String shipmentMethod) {
        super(con);
        this.userId = userId;
        this.shipmentMethod = shipmentMethod;
    }

    @Override
    protected void doAccess() throws Exception {
        // Modify query here to use RETURNING
        final String query = CREATE_CART_FOR_USER + " RETURNING cart_id";

        try (PreparedStatement stmnt = con.prepareStatement(query)) {
            stmnt.setInt(1, userId);
            stmnt.setString(2, shipmentMethod);

            try (ResultSet rs = stmnt.executeQuery()) {
                if (rs.next()) {
                    int cartId = rs.getInt("cart_id");
                    this.outputParam = cartId;
                    LOGGER.info("New cart created for user {} with cart ID {}.", userId, cartId);
                } else {
                    throw new Exception("Cart creation failed — no cart ID returned.");
                }
            }

        } catch (Exception ex) {
            LOGGER.error("Error creating cart for user {}: {}", userId, ex.getMessage());
            throw ex;
        }
    }
}
