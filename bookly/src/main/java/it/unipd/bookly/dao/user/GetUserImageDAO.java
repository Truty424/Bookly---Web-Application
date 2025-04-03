package it.unipd.bookly.dao.user;

import it.unipd.bookly.Resource.Image;
import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static it.unipd.bookly.dao.user.UserQueries.INSERT_USER_IMAGE;

/**
 * DAO class to insert a new profile image for a user.
 */
public class InsertUserImageDAO extends AbstractDAO<Boolean> {

    private final int userId;
    private final Image profileImage;

    /**
     * Constructs a DAO to insert a user's profile image.
     *
     * @param con           the database connection
     * @param userId        the user ID
     * @param profileImage  the image to insert
     */
    public InsertUserImageDAO(final Connection con, final int userId, final Image profileImage) {
        super(con);
        this.userId = userId;
        this.profileImage = profileImage;
    }

    @Override
    protected void doAccess() throws SQLException {
        try (PreparedStatement stmt = con.prepareStatement(INSERT_USER_IMAGE)) {
            stmt.setInt(1, userId);
            stmt.setBytes(2, profileImage.getPhoto());
            stmt.setString(3, profileImage.getPhotoMediaType());

            int rowsAffected = stmt.executeUpdate();
            this.outputParam = rowsAffected > 0;

        } catch (SQLException ex) {
            LOGGER.error("Error inserting profile image for user ID {}: {}", userId, ex.getMessage());
            throw ex;
        }
    }
}
