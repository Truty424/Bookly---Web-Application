package it.unipd.bookly.dao.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import it.unipd.bookly.Resource.Image;
import it.unipd.bookly.Resource.User;
import it.unipd.bookly.dao.AbstractDAO;
import static it.unipd.bookly.dao.user.UserQueries.GET_USER_IMAGE;
import static it.unipd.bookly.dao.user.UserQueries.LOGIN_USER;

/**
 * DAO to authenticate a user. Returns a full {@code User} object if credentials match.
 */
public class LoginUserDAO extends AbstractDAO<User> {

    private final String usernameOrEmail;
    private final String password;

    /**
     * Constructor.
     *
     * @param con              the database connection
     * @param usernameOrEmail  the user's username or email
     * @param password         the user's password
     */
    public LoginUserDAO(final Connection con, final String usernameOrEmail, final String password) {
        super(con);
        this.usernameOrEmail = usernameOrEmail;
        this.password = password;
    }

    @Override
    protected void doAccess() throws Exception {
        try (
            PreparedStatement userStmnt = con.prepareStatement(LOGIN_USER);
            PreparedStatement imageStmnt = con.prepareStatement(GET_USER_IMAGE)
        ) {
            userStmnt.setString(1, usernameOrEmail);
            userStmnt.setString(2, password);

            try (ResultSet userRs = userStmnt.executeQuery()) {
                if (userRs.next()) {
                    final int userId = userRs.getInt("user_id");

                    Image userImage = null;
                    imageStmnt.setInt(1, userId);

                    try (ResultSet imageRs = imageStmnt.executeQuery()) {
                        if (imageRs.next()) {
                            userImage = new Image(
                                imageRs.getBytes("image"),
                                imageRs.getString("image_type")
                            );
                        }
                    }

                this.outputParam = new User(
                    userId,
                    userRs.getString("username"),
                    userRs.getString("password"),
                    userRs.getString("first_name"),
                    userRs.getString("last_name"),
                    userRs.getString("email"),
                    userRs.getString("phone"),
                    userRs.getString("address"),
                    userRs.getString("role"),
                    userImage
                );
                    LOGGER.info("Login successful for user '{}'.", this.outputParam.getUsername());
                } else {
                    this.outputParam = null;
                    LOGGER.warn("Login failed: No user found with the provided credentials.");
                }
            }

        } catch (Exception ex) {
            LOGGER.error("Login failed due to unexpected error: {}", ex.getMessage());
            throw ex;
        }
    }
}
