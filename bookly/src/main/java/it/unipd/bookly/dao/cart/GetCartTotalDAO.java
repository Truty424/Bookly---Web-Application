package it.unipd.bookly.dao.cart;

import it.unipd.bookly.dao.AbstractDAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static it.unipd.bookly.dao.cart.CartQueries.GET_CART_TOTAL;

public class GetCartTotalDAO extends AbstractDAO<Double> {
    private final int cartId;

    public GetCartTotalDAO(java.sql.Connection con, int cartId) {
        super(con);
        this.cartId = cartId;
    }

    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmt = con.prepareStatement(GET_CART_TOTAL)) {
            stmt.setInt(1, cartId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    this.outputParam = rs.getDouble("total_price");
                } else {
                    throw new SQLException("No total_price found for cart ID: " + cartId);
                }
            }
        }
    }
}
