package it.unipd.bookly.dao.user;

import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static it.unipd.bookly.dao.user.UserQueries.CHANGE_USER_PASSWORD;

/**
 * DAO to change the password for a specific user.
 */
public class ChangeUserPasswordDAO extends AbstractDAO<Boolean> {

    private final int userId;
    private final String newHashedPassword;

    /**
     * Constructs a DAO to change the user's password.
     *
     * @param con               the database connection
     * @param userId            the user ID
     * @param newHashedPassword the new password (must already be hashed)
     */
    public ChangeUserPasswordDAO(Connection con, int userId, String newHashedPassword) {
        super(con);
        this.userId = userId;
        this.newHashedPassword = newHashedPassword;
    }

    @Override
    protected void doAccess() throws SQLException {
        try (PreparedStatement stmt = con.prepareStatement(CHANGE_USER_PASSWORD)) {
            stmt.setString(1, newHashedPassword);
            stmt.setInt(2, userId);

            int rowsUpdated = stmt.executeUpdate();
            this.outputParam = rowsUpdated > 0;

            if (this.outputParam) {
                LOGGER.info("Password successfully updated for user ID {}", userId);
            } else {
                LOGGER.warn("Password update failed. No user found with ID {}", userId);
            }
        } catch (SQLException ex) {
            LOGGER.error("SQL error while updating password for user ID {}: {}", userId, ex.getMessage());
            throw ex;
        }
    }
}
