package it.unipd.bookly.dao.user;

import java.sql.Connection;
import java.sql.PreparedStatement;

import it.unipd.bookly.Resource.User;
import it.unipd.bookly.dao.AbstractDAO;

import static it.unipd.bookly.dao.user.UserQueries.UPDATE_USER;

/**
 * DAO class to update user profile information (excluding password/image).
 */
public class UpdateUserDAO extends AbstractDAO<User> {

    private final User user;

    /**
     * Constructor.
     *
     * @param con  the DB connection
     * @param user the user object with updated fields (must include userId)
     */
    public UpdateUserDAO(final Connection con, final User user) {
        super(con);
        this.user = user;
    }

    @Override
    protected void doAccess() throws Exception {
        boolean autoCommit = con.getAutoCommit();
        con.setAutoCommit(false); // Begin transaction

        try (PreparedStatement stmt = con.prepareStatement(UPDATE_USER)) {
            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getLastName());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPhone());
            stmt.setString(5, user.getAddress());
            stmt.setString(6, user.getRole());
            stmt.setInt(7, user.getUserId());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                outputParam = user;
                LOGGER.info("User ID {} updated successfully.", user.getUserId());
            } else {
                LOGGER.warn("User ID {} not found for update.", user.getUserId());
                outputParam = null;
            }

            con.commit();
        } catch (Exception ex) {
            con.rollback();
            LOGGER.error("Error updating user ID {}: {}", user.getUserId(), ex.getMessage());
            throw ex;
        } finally {
            con.setAutoCommit(autoCommit); // Restore original setting
        }
    }
}
