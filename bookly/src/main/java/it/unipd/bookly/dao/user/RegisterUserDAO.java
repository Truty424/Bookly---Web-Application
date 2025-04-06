package it.unipd.bookly.dao.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import it.unipd.bookly.Resource.Image;
import it.unipd.bookly.Resource.User;
import it.unipd.bookly.dao.AbstractDAO;

import static it.unipd.bookly.dao.user.UserQueries.INSERT_USER_IMAGE;
import static it.unipd.bookly.dao.user.UserQueries.REGISTER_USER;

/**
 * DAO class to register a new user and optionally store their profile image.
 */
public class RegisterUserDAO extends AbstractDAO<User> {

    private final User user;

    /**
     * Constructor.
     *
     * @param con  the DB connection
     * @param user the user to register
     */
    public RegisterUserDAO(final Connection con, final User user) {
        super(con);
        this.user = user;
    }

    @Override
    protected void doAccess() throws Exception {
        boolean autoCommitState = con.getAutoCommit();
        con.setAutoCommit(false); // Begin transaction

        try (
            PreparedStatement userStmt = con.prepareStatement(REGISTER_USER, PreparedStatement.RETURN_GENERATED_KEYS);
            PreparedStatement imageStmt = con.prepareStatement(INSERT_USER_IMAGE)
        ) {
            // Set user fields
            userStmt.setString(1, user.getUsername());
            userStmt.setString(2, user.getPassword()); // Assume already hashed
            userStmt.setString(3, user.getFirstName());
            userStmt.setString(4, user.getLastName());
            userStmt.setString(5, user.getEmail());
            userStmt.setString(6, user.getPhone());
            userStmt.setString(7, user.getAddress());
            userStmt.setString(8, user.getRole() != null ? user.getRole() : "user");

            userStmt.executeUpdate();

            // Get the generated user ID
            try (ResultSet rs = userStmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int userId = rs.getInt(1);
                    user.setUserId(userId);

                    // If a profile image exists, insert it
                    Image profileImage = user.getProfileImage();
                    if (profileImage != null) {
                        imageStmt.setInt(1, userId);
                        imageStmt.setBytes(2, profileImage.getPhoto());
                        imageStmt.setString(3, profileImage.getPhotoMediaType());
                        imageStmt.executeUpdate();
                    }

                    outputParam = user;
                    LOGGER.info("User '{}' registered successfully with ID {}", user.getUsername(), userId);
                } else {
                    throw new Exception("User registration failed: no ID returned.");
                }
            }

            con.commit(); // Commit transaction
        } catch (Exception ex) {
            con.rollback(); // Rollback on error
            LOGGER.error("Registration failed for user '{}': {}", user.getUsername(), ex.getMessage());
            throw ex;
        } finally {
            con.setAutoCommit(autoCommitState); // Restore original state
        }
    }
}
