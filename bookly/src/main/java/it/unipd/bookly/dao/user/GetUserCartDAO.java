package it.unipd.bookly.dao.cart;

import it.unipd.bookly.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetUserCartDAO {
    private static final String GET_USER_BY_CART =
        "SELECT u.user_id, u.username, u.first_name, u.last_name, u.email, u.phone, u.address, u.role " +
        "FROM booklySchema.users u " +
        "JOIN booklySchema.shoppingcart c ON u.user_id = c.user_id " +
        "WHERE c.cart_id = ?";

    public User getUserByCart(Connection connection, int cartId) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(GET_USER_BY_CART)) {
            ps.setInt(1, cartId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getString("role")
                    );
                }
            }
        }
        return null;
    }
}