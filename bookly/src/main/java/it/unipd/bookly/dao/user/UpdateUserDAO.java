package it.unipd.bookly.dao.user;

import java.sql.Connection;
import java.sql.PreparedStatement;

import it.unipd.bookly.Resource.User;
import it.unipd.bookly.dao.AbstractDAO;
import static it.unipd.bookly.dao.user.UserQueries.UPDATE_USER;

/**
 * DAO class to update user profile information (and optionally the profile
 * image).
 */
public class UpdateUserDAO extends AbstractDAO<User> {

    private final User user;

    /**
     * Constructor.
     *
     * @param con the DB connection
     * @param user the user object with updated fields (userId must be set)
     */
    public UpdateUserDAO(final Connection con, final User user) throws Exception {
        super(con);
        con.setAutoCommit(false);
        this.user = user;
    }

    @Override
    protected void doAccess() throws Exception {
        try (
                PreparedStatement userStmnt = con.prepareStatement(UPDATE_USER);) {
            userStmnt.setString(1, user.getFirstName());
            userStmnt.setString(2, user.getLastName());
            userStmnt.setString(3, user.getEmail());
            userStmnt.setString(4, user.getPhone());
            userStmnt.setString(5, user.getAddress());
            userStmnt.setString(6, user.getRole());
            userStmnt.setInt(7, user.getUserId());

            userStmnt.executeUpdate();

            con.commit();
            this.outputParam = user;
            LOGGER.info("User ID {} updated successfully.", user.getUserId());

        } catch (Exception ex) {
            con.rollback();
            LOGGER.error("Failed to update user ID {}: {}", user.getUserId(), ex.getMessage());
            throw ex;
        } finally {
            con.setAutoCommit(true);
        }
    }
}
