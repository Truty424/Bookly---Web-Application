package it.unipd.bookly.dao.user;

import it.unipd.bookly.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {
    private static final String GET_USER_BY_ID = 
        "SELECT user_id, username, first_name, last_name, email, phone, address, role " +
        "FROM booklySchema.users WHERE user_id = ?";

    public User getUserById(Connection connection, int userId) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(GET_USER_BY_ID)) {
            ps.setInt(1, userId);
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