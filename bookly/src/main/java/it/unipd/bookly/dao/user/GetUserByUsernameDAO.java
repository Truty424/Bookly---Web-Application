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
 * DAO to retrieve users whose usernames match a given pattern.
 */
public class GetUserByUsernameDAO extends AbstractDAO<List<User>> {

    private final String usernameLike;

    /**
     * Constructs the DAO with the connection and username pattern.
     *
     * @param con           the DB connection
     * @param usernameLike  the username filter (e.g., "john%")
     */
    public GetUserByUsernameDAO(Connection con, String usernameLike) {
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
                    users.add(user);
                }
            }

            this.outputParam = users;

            LOGGER.info("Found {} user(s) matching username '{}'.", users.size(), usernameLike);

        } catch (Exception e) {
            LOGGER.error("Failed to retrieve users by username '{}': {}", usernameLike, e.getMessage());
            throw e;
        }
    }
}
