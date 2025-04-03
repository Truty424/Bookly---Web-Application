
package it.unipd.bookly.dao.user;

import it.unipd.bookly.Resource.Image;
import it.unipd.bookly.Resource.User;
import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static it.unipd.bookly.dao.user.UserQueries.GET_USERS_BY_USERNAME;

/**
 * DAO class to retrieve users whose usernames match a given string.
 */
public class GetUserByUsernameDAO extends AbstractDAO<List<User>> {

    private final String usernameLike;

    /**
     * Constructor.
     *
     * @param con           the database connection
     * @param usernameLike  the username or partial match (e.g., "john%")
     */
    public GetUserByUsernameDAO(final Connection con, final String usernameLike) {
        super(con);
        this.usernameLike = usernameLike;
    }

    @Override
    protected void doAccess() throws Exception {
        List<User> users = new ArrayList<>();

        try (PreparedStatement stmt = con.prepareStatement(GET_USERS_BY_USERNAME)) {
            stmt.setString(1, usernameLike);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Image image = null;
                    byte[] imgData = rs.getBytes("image");
                    String imgType = rs.getString("image_type");
                    if (imgData != null && imgType != null) {
                        image = new Image(imgData, imgType);
                    }

                    User user = new User(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getString("role"),
                        image
                    );
                    users.add(user);
                }
            }

            this.outputParam = users;

            if (!users.isEmpty()) {
                LOGGER.info("{} user(s) found matching '{}'.", users.size(), usernameLike);
            } else {
                LOGGER.info("No users found matching '{}'.", usernameLike);
            }

        } catch (Exception ex) {
            LOGGER.error("Error retrieving users by username '{}': {}", usernameLike, ex.getMessage());
            throw ex;
        }
    }
}
