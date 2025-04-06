package it.unipd.bookly.dao.user;

import it.unipd.bookly.model.User;
import java.sql.*;

public class GetUserByIdDAO {

    private static final String SELECT_USER_BY_ID = "SELECT * FROM users WHERE user_id = ?";

    public User getUserById(String userId) throws SQLException {
        User user = null;
        
        // Establish database connection
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bookly", "username", "password");
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID)) {
             
            // Set the userId parameter in the query
            preparedStatement.setString(1, userId);

            // Execute the query
            ResultSet resultSet = preparedStatement.executeQuery();

            // If the user is found, create a User object and populate it with the data
            if (resultSet.next()) {
                user = new User();
                user.setUserId(resultSet.getString("user_id"));
                user.setUsername(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));
                user.setPhone(resultSet.getString("phone"));
                // Add other user fields as needed (e.g., name, address)
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error retrieving user by ID", e);
        }
        
        return user;
    }
}
