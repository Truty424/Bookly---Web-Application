package it.unipd.bookly.dao.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import it.unipd.bookly.Resource.Image;
import it.unipd.bookly.Resource.User;
import it.unipd.bookly.dao.AbstractDAO;

import static it.unipd.bookly.dao.user.UserQueries.LOGIN_USER;
import static it.unipd.bookly.dao.user.UserQueries.GET_USER_IMAGE;

/**
 * DAO to authenticate a user and retrieve full user data including profile image.
 */
public class LoginUserDAO extends AbstractDAO<User> {

    private final String username;
    private final String password;

    /**
     * Constructor.
     *
     * @param con       the database connection
     * @param username  the user's username
     * @param password  the user's password (in plain text, will be hashed by query)
     */
    public LoginUserDAO(Connection con, String username, String password) {
        super(con);
        this.username = username;
        this.password = password;
    }

    @Override
    protected void doAccess() throws Exception {
        try (
            PreparedStatement userStmt = con.prepareStatement(LOGIN_USER);
            PreparedStatement imageStmt = con.prepareStatement(GET_USER_IMAGE)
        ) {
            userStmt.setString(1, username);
            userStmt.setString(2, password); // will be hashed via md5(?) in SQL

            try (ResultSet userRs = userStmt.executeQuery()) {
                if (userRs.next()) {
                    int userId = userRs.getInt("user_id");

                    // Fetch user image if available
                    Image profileImage = null;
                    imageStmt.setInt(1, userId);
                    try (ResultSet imageRs = imageStmt.executeQuery()) {
                        if (imageRs.next()) {
                            profileImage = new Image(
                                imageRs.getBytes("image"),
                                imageRs.getString("image_type")
                            );
                        }
                    }

                    // Create and return the User object
                    this.outputParam = new User(
                        userRs.getString("username"),
                        userRs.getString("password"),
                        userRs.getString("first_name"),
                        userRs.getString("last_name"),
                        userRs.getString("email"),
                        userRs.getString("phone"),
                        userRs.getString("address"),
                        userRs.getString("role"),
                        profileImage
                    );

                    LOGGER.info("Login successful for user '{}'.", username);
                } else {
                    this.outputParam = null;
                    LOGGER.warn("Login failed: No user found for '{}'.", username);
                }
            }
        } catch (Exception e) {
            this.outputParam = null;  // Ensure it's always set
            LOGGER.error("LoginUserDAO error for '{}': {}", username, e.getMessage());
            throw e;
        }
    }
}
