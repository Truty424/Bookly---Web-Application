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
     * @param con  the DB connection.
     * @param user the user to register.
     */
    public RegisterUserDAO(final Connection con, final User user) throws Exception {
        super(con);
        con.setAutoCommit(false);
        this.user = user;
    }

    @Override
    protected void doAccess() throws Exception {
        try (
            PreparedStatement userStmnt = con.prepareStatement(REGISTER_USER, PreparedStatement.RETURN_GENERATED_KEYS);
            PreparedStatement imageStmnt = con.prepareStatement(INSERT_USER_IMAGE)
        ) {
            userStmnt.setString(1, user.getUsername());
            userStmnt.setString(2, user.getPassword()); // Should be hashed by DB or before this point
            userStmnt.setString(3, user.getFirstName());
            userStmnt.setString(4, user.getLastName());
            userStmnt.setString(5, user.getEmail());
            userStmnt.setString(6, user.getPhone());
            userStmnt.setString(7, user.getAddress());
            userStmnt.setString(8, user.getRole() != null ? user.getRole() : "user");

            userStmnt.executeUpdate();

            try (ResultSet rs = userStmnt.getGeneratedKeys()) {
                if (rs.next()) {
                    final int userId = rs.getInt(1);
                    user.setUserId(userId);

                    // Insert profile image if available
                    if (user.getProfileImage() != null) {
                        Image img = user.getProfileImage();
                        imageStmnt.setInt(1, userId);
                        imageStmnt.setBytes(2, img.getPhoto());
                        imageStmnt.setString(3, img.getPhotoMediaType());
                        imageStmnt.executeUpdate();
                    }

                    this.outputParam = user;
                    LOGGER.info("User '{}' registered successfully with ID {}.", user.getUsername(), userId);
                } else {
                    throw new Exception("User registered but no ID returned.");
                }
            }

            con.commit();

        } catch (Exception ex) {
            con.rollback();
            LOGGER.error("Error during user registration for '{}': {}", user.getUsername(), ex.getMessage());
            throw ex;
        } finally {
            con.setAutoCommit(true);
        }
    }
}
