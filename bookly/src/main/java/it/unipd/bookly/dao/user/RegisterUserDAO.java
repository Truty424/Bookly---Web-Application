package it.unipd.bookly.user;

import it.unipd.bookly.dao.AbstractDAO;
import it.unipd.bookly.resource.Image;
import it.unipd.bookly.resource.User;

import java.sql.Connection;
import java.sql.PreparedStatement;

import static it.unipd.bookly.dao.user.UserQueries.REGISTER_USER;
import static it.unipd.bookly.dao.user.UserQueries.INSERT_USER_IMAGE;

/**
 * Registers a new user in the system.
 */
public class RegisterUserDAO extends AbstractDAO<User> {

    private final User user;

    public RegisterUserDAO(final Connection con, final User user) throws Exception {
        super(con);
        con.setAutoCommit(false);
        this.user = user;
    }

    @Override
    protected void doAccess() throws Exception {
        try (
            PreparedStatement stmnt = con.prepareStatement(REGISTER_USER);
            PreparedStatement imgStmnt = con.prepareStatement(INSERT_USER_IMAGE)
        ) {
            stmnt.setString(1, user.getUsername());
            stmnt.setString(2, user.getPassword());  // This will be hashed with md5(?) in the query
            stmnt.setString(3, user.getFirstName());
            stmnt.setString(4, user.getLastName());
            stmnt.setString(5, user.getEmail());
            stmnt.setString(6, user.getPhone());
            stmnt.setString(7, user.getAddress());
            stmnt.setString(8, user.getRole() != null ? user.getRole() : "user");

            stmnt.executeUpdate();

            // Insert profile image if provided
            if (user.getProfileImage() != null) {
                Image img = user.getProfileImage();
                imgStmnt.setInt(1, user.getUserId()); // Should be retrieved post-insert or assigned
                imgStmnt.setBytes(2, img.getPhoto());
                imgStmnt.setString(3, img.getPhotoMediaType());
                imgStmnt.executeUpdate();
            }

            con.commit();
            this.outputParam = user;
            LOGGER.info("User registered successfully: {}", user.getUsername());

        } catch (Exception ex) {
            con.rollback();
            LOGGER.error("Error registering user {}: {}", user.getUsername(), ex.getMessage());
            throw ex;
        } finally {
            con.setAutoCommit(true);
        }
    }
}
