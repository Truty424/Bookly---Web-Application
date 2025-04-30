package it.unipd.bookly.dao.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import it.unipd.bookly.Resource.Image;
import it.unipd.bookly.dao.AbstractDAO;

import static it.unipd.bookly.dao.user.UserQueries.UPDATE_USER_IMAGE_IF_EXISTS;

/**
 * DAO to insert or update a user's profile image using UPSERT logic.
 */
public class UpdateUserImageIfExistsDAO extends AbstractDAO<Boolean> {

    private final int userId;
    private final Image image;

    /**
     * Constructs the DAO with connection, user ID, and image data.
     *
     * @param con     Database connection
     * @param userId  User ID whose image is being updated
     * @param image   Image object containing binary data and MIME type
     */
    public UpdateUserImageIfExistsDAO(Connection con, int userId, Image image) {
        super(con);
        this.userId = userId;
        this.image = image;
    }

    /**
     * Executes the UPSERT operation for the user's profile image.
     *
     * @throws SQLException if any SQL error occurs
     */
    @Override
    protected void doAccess() throws SQLException {
        try (PreparedStatement stmt = con.prepareStatement(UPDATE_USER_IMAGE_IF_EXISTS)) {
            stmt.setInt(1, userId);
            stmt.setBytes(2, image.getPhoto());
            stmt.setString(3, image.getPhotoMediaType());

            int rows = stmt.executeUpdate();
            this.outputParam = rows > 0;

            if (outputParam) {
                LOGGER.info("Profile image inserted or updated for user ID {}", userId);
            } else {
                LOGGER.warn("No profile image inserted/updated for user ID {}", userId);
            }

        } catch (SQLException e) {
            LOGGER.error("Failed to insert/update image for user ID {}: {}", userId, e.getMessage(), e);
            throw e;
        }
    }
}
