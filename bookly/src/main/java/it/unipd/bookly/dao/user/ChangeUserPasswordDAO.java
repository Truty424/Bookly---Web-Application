package it.unipd.bookly.dao.user;

import java.sql.Connection;
import java.sql.PreparedStatement;

import it.unipd.bookly.dao.AbstractDAO;

import static it.unipd.bookly.dao.user.UserQueries.CHANGE_USER_PASSWORD;

/**
 * DAO to change a user's password by ID.
 */
public class ChangeUserPasswordDAO extends AbstractDAO<Boolean> {

    private final int userId;
    private final String password;

    public ChangeUserPasswordDAO(Connection con, int userId, String password) {
        super(con);
        this.userId = userId;
        this.password = password;
    }

    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmt = con.prepareStatement(CHANGE_USER_PASSWORD)) {
            stmt.setString(1, password);
            stmt.setInt(2, userId);
            int affectedRows = stmt.executeUpdate();

            this.outputParam = affectedRows > 0;
            if (outputParam) {
                LOGGER.info("Password updated for user_id {}", userId);
            } else {
                LOGGER.warn("No user found with user_id {}. Password not changed.", userId);
            }
        } catch (Exception e) {
            this.outputParam = false; 
            LOGGER.error("Failed to update password for user_id {}: {}", userId, e.getMessage());
            throw e;
        }
    }
}
