package it.unipd.bookly.dao.user;

import it.unipd.bookly.Resource.Image;
import it.unipd.bookly.Resource.User;
import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static it.unipd.bookly.dao.user.UserQueries.GET_USERS_BY_USERNAME;

/**
 * DAO to retrieve users whose usernames match a given pattern.
 */
public class GetUserByUsernameDAO extends AbstractDAO<User> {

    private final String username;

    /**
     * Constructs the DAO with the connection and username pattern.
     *
     * @param con the DB connection
     * @param usernameLike the username filter (e.g., "john%")
     */
    public GetUserByUsernameDAO(Connection con, String username) {
        super(con);
        this.username = username;
    }

    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmt = con.prepareStatement(GET_USERS_BY_USERNAME)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Image profileImage = null;
                    byte[] imageBytes = rs.getBytes("image");
                    String imageType = rs.getString("image_type");

                    if (imageBytes != null && imageType != null) {
                        profileImage = new Image(imageBytes, imageType);
                    }

                    User user = new User(
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("email"),
                            rs.getString("phone"),
                            rs.getString("address"),
                            rs.getString("role"),
                            profileImage
                    );

                    this.outputParam = user;
                    LOGGER.info("Found user matching username '{}'.", username);
                } else {
                    this.outputParam = null;
                    LOGGER.info("No user found matching username '{}'.", username);
                }
            }

        } catch (Exception e) {
            LOGGER.error("Failed to retrieve user by username '{}': {}", username, e.getMessage());
            throw e;
        }
    }
}
