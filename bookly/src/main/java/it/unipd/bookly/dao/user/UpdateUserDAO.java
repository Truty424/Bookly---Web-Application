package it.unipd.bookly.dao.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import it.unipd.bookly.dao.AbstractDAO;

import static it.unipd.bookly.dao.user.UserQueries.UPDATE_USER;

/**
 * DAO class to update user profile information (excluding password/image).
 */
public class UpdateUserDAO extends AbstractDAO<Boolean> {

    private final int userId;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String phone;
    private final String address;
    private final String role;

    /**
     * Constructs the DAO to update user profile data.
     *
     * @param con       Database connection
     * @param userId    User ID to update
     * @param firstName First name
     * @param lastName  Last name
     * @param email     Email address
     * @param phone     Phone number
     * @param address   Address
     * @param role      Role (e.g., USER, ADMIN)
     */
    public UpdateUserDAO(Connection con, int userId, String firstName, String lastName,
                         String email, String phone, String address, String role) {
        super(con);
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.role = role;
    }

    @Override
    protected void doAccess() throws SQLException {
        try (PreparedStatement stmt = con.prepareStatement(UPDATE_USER)) {
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setString(3, email);
            stmt.setString(4, phone);
            stmt.setString(5, address);
            stmt.setString(6, role);
            stmt.setInt(7, userId);

            int affectedRows = stmt.executeUpdate();
            this.outputParam = affectedRows > 0;

            if (outputParam) {
                LOGGER.info("User ID {} updated successfully.", userId);
            } else {
                LOGGER.warn("No user found with ID {}. Update skipped.", userId);
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to update user ID {}: {}", userId, e.getMessage());
            throw e;
        }
    }
}
