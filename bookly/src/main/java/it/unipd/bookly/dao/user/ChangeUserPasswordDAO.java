package it.unipd.bookly.dao.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import it.unipd.bookly.dao.AbstractDAO;
import static it.unipd.bookly.dao.user.UserQueries.CHANGE_USER_PASSWORD;

/**
 * DAO class to update a user's password in the database.
 */
public class ChangeUserPasswordDAO extends AbstractDAO<Boolean> {

    private final int userId;
    private final String newPassword;

    /**
     * Constructor.
     *
     * @param con          the DB connection
     * @param userId       the ID of the user
     * @param newPassword  the new password (already hashed, if applicable)
     */
    public ChangeUserPasswordDAO(final Connection con, final int userId, final String newPassword) {
        super(con);
        this.userId = userId;
        this.newPassword = newPassword;
    }

    @Override
    protected void doAccess() throws SQLException {
        try (PreparedStatement stmt = con.prepareStatement(CHANGE_USER_PASSWORD)) {
            stmt.setString(1, newPassword);
            stmt.setInt(2, userId);

            int rowsAffected = stmt.executeUpdate();
            this.outputParam = rowsAffected > 0;

            if (outputParam) {
                LOGGER.info("Password updated successfully for user ID {}.", userId);
            } else {
                LOGGER.warn("Password not updated. User ID {} may not exist.", userId);
            }

        } catch (SQLException ex) {
            LOGGER.error("Error updating password for user ID {}: {}", userId, ex.getMessage());
            throw ex;
        }
    }
}