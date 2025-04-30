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

    public LoginUserDAO(Connection con, String username, String password) {
        super(con);
        this.username = username;
        this.password = password;
    }

    @Override
    protected void doAccess() throws Exception {
        boolean autoCommitState = con.getAutoCommit();
        con.setAutoCommit(false);  // Start transaction

        PreparedStatement userStmt = null;
        PreparedStatement imageStmt = null;
        ResultSet userRs = null;
        ResultSet imageRs = null;

        try {
            userStmt = con.prepareStatement(LOGIN_USER);
            userStmt.setString(1, this.username);
            userStmt.setString(2, this.password);  // Will be hashed via md5(?) in SQL

            userRs = userStmt.executeQuery();

            if (userRs.next()) {
                int userId = userRs.getInt("user_id");

                imageStmt = con.prepareStatement(GET_USER_IMAGE);
                imageStmt.setInt(1, userId);
                imageRs = imageStmt.executeQuery();

                Image profileImage = null;
                if (imageRs.next()) {
                    profileImage = new Image(
                        imageRs.getBytes("image"),
                        imageRs.getString("image_type")
                    );
                }

                this.outputParam = new User(
                    userId,
                    userRs.getString("username"),
                    userRs.getString("password"),
                    userRs.getString("firstName"),
                    userRs.getString("lastName"),
                    userRs.getString("email"),
                    userRs.getString("phone"),
                    userRs.getString("address"),
                    userRs.getString("role"),
                    profileImage
                );

                LOGGER.info("Login successful for user '{}'.", this.username);
            } else {
                this.outputParam = null;
                LOGGER.warn("Login failed: No user found for '{}'.", this.username);
            }

            con.commit();  // Commit transaction

        } catch (Exception e) {
            con.rollback();  // Rollback if any error occurs
            this.outputParam = null;
            LOGGER.error("LoginUserDAO error for '{}': {}", this.username, e.getMessage(), e);
            throw e;

        } finally {
            if (imageRs != null) imageRs.close();
            if (userRs != null) userRs.close();
            if (imageStmt != null) imageStmt.close();
            if (userStmt != null) userStmt.close();
            con.setAutoCommit(autoCommitState);  // Restore previous autocommit state
        }
    }
}
