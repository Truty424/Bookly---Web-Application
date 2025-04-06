package it.unipd.bookly.dao.user;

import it.unipd.bookly.Resource.Image;
import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;

import static it.unipd.bookly.dao.user.UserQueries.UPDATE_USER_IMAGE;

/**
 * DAO class to update the profile image of a user.
 */
public class UpdateUserImageDAO extends AbstractDAO<Boolean> {

    private final int userId;
    private final Image profileImage;

    /**
     * Constructor.
     *
     * @param con           the database connection
     * @param userId        the ID of the user
     * @param profileImage  the new profile image to set (must not be null)
     */
    public UpdateUserImageDAO(final Connection con, final int userId, final Image profileImage) {
        super(con);
        this.userId = userId;
        this.profileImage = profileImage;
    }

    @Override
    protected void doAccess() throws Exception {
        if (profileImage == null || profileImage.getPhoto() == null || profileImage.getPhotoMediaType() == null) {
            LOGGER.warn("Attempted to update user image with null data for user ID {}", userId);
            outputParam = false;
            return;
        }

        try (PreparedStatement stmt = con.prepareStatement(UPDATE_USER_IMAGE)) {
            stmt.setBytes(1, profileImage.getPhoto());
            stmt.setString(2, profileImage.getPhotoMediaType());
            stmt.setInt(3, userId);

            int rowsAffected = stmt.executeUpdate();
            outputParam = rowsAffected > 0;

            if (outputParam) {
                LOGGER.info("Profile image updated for user ID {}.", userId);
            } else {
                LOGGER.warn("No profile image was updated. User ID {} may not exist.", userId);
            }
        } catch (Exception ex) {
            LOGGER.error("Failed to update profile image for user ID {}: {}", userId, ex.getMessage());
            throw ex;
        }
    }
}
