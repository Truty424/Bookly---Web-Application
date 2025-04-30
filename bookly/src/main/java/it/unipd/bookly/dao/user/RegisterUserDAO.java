package it.unipd.bookly.dao.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import it.unipd.bookly.Resource.Image;
import it.unipd.bookly.Resource.User;
import it.unipd.bookly.dao.AbstractDAO;

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
        boolean originalAutoCommit = con.getAutoCommit();
        con.setAutoCommit(false); // Begin transaction

        try (PreparedStatement userStmt = con.prepareStatement(REGISTER_USER, PreparedStatement.RETURN_GENERATED_KEYS)) {
            userStmt.setString(1, user.getUsername());
            userStmt.setString(2, user.getPassword()); // Already hashed
            userStmt.setString(3, user.getFirstName());
            userStmt.setString(4, user.getLastName());
            userStmt.setString(5, user.getEmail());
            userStmt.setString(6, user.getPhone());
            userStmt.setString(7, user.getAddress());
            userStmt.setString(8, user.getRole() != null ? user.getRole() : "user");

            userStmt.executeUpdate();

            try (ResultSet rs = userStmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int userId = rs.getInt(1);
                    user.setUserId(userId);

                    Image profileImage = user.getProfileImage();
                    if (profileImage != null && profileImage.getPhoto() != null) {
                        // Use an existing DAO for image update
                        boolean updated = new UpdateUserImageIfExistsDAO(con, userId, profileImage).access().getOutputParam();
                        if (!updated) {
                            LOGGER.warn("Image for user ID {} was not inserted or updated.", userId);
                        }
                    }

                    this.outputParam = user;
                    LOGGER.info("User '{}' registered successfully with ID {}", user.getUsername(), userId);
                } else {
                    throw new Exception("User registration failed: no ID returned.");
                }
            }

            con.commit(); // Commit if everything succeeded
        } catch (Exception e) {
            con.rollback();
            LOGGER.error("Registration failed for user '{}': {}", user.getUsername(), e.getMessage(), e);
            throw e;
        } finally {
            con.setAutoCommit(originalAutoCommit); // Restore default state
        }
    }
}
