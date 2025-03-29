package it.unipd.bookly.user;

import it.unipd.bookly.dao.AbstractDAO;
import it.unipd.bookly.resource.User;
import it.unipd.bookly.resource.Image;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static it.unipd.bookly.dao.user.UserQueries.LOGIN_USER;
import static it.unipd.bookly.dao.user.UserQueries.GET_USER_IMAGE;

/**
 * Logs in a user. Returns a complete User object if credentials are correct.
 */
public class LoginUserDAO extends AbstractDAO<User> {

    private final String usernameOrEmail;
    private final String password;

    public LoginUserDAO(Connection con, String usernameOrEmail, String password) {
        super(con);
        this.usernameOrEmail = usernameOrEmail;
        this.password = password;
    }

    @Override
    protected void doAccess() throws Exception {
        ResultSet rs = null;
        ResultSet imageRs = null;
        Image image = null;

        try (PreparedStatement stmt = con.prepareStatement(LOGIN_USER);
             PreparedStatement imgStmt = con.prepareStatement(GET_USER_IMAGE)) {

            stmt.setString(1, usernameOrEmail);
            stmt.setString(2, password);
            rs = stmt.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("user_id");

                imgStmt.setInt(1, userId);
                imageRs = imgStmt.executeQuery();

                if (imageRs.next()) {
                    image = new Image(imageRs.getBytes("image"), imageRs.getString("image_type"));
                }

                this.outputParam = new User(
                        userId,
                        rs.getString("username"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getString("role"),
                        image
                );

                LOGGER.info("User '{}' logged in successfully.", this.outputParam.getUsername());
            } else {
                LOGGER.warn("Login failed: Invalid username/email or password.");
            }

        } catch (Exception e) {
            LOGGER.error("Login exception: {}", e.getMessage());
            throw e;
        }
    }
}
