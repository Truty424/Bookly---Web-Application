package it.unipd.bookly.dao.user;

import it.unipd.bookly.Resource.Image;
import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;

import static it.unipd.bookly.dao.user.UserQueries.UPDATE_USER_IMAGE;

/**
 * DAO class to update the profile image of a user.
 */
public class UpdateUserImageDAO extends AbstractDAO<Void> {

    private final int userId;
    private final Image profileImage;

    /**
     * Constructor.
     *
     * @param con           the database connection
     * @param userId        the ID of the user
     * @param profileImage  the new profile image to set
     */
    public UpdateUserImageDAO(final Connection con, final int userId, final Image profileImage) {
        super(con);
        this.userId = userId;
        this.profileImage = profileImage;
    }

    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmnt = con.prepareStatement(UPDATE_USER_IMAGE)) {
            stmnt.setBytes(1, profileImage.getPhoto());
            stmnt.setString(2, profileImage.getPhotoMediaType());
            stmnt.setInt(3, userId);

            int rowsAffected = stmnt.executeUpdate();

            if (rowsAffected > 0) {
                LOGGER.info("Profile image updated for user ID {}.", userId);
            } else {
                LOGGER.warn("No profile image updated for user ID {}. Row may not exist.", userId);
            }

            this.outputParam = null;

        } catch (Exception ex) {
            LOGGER.error("Error updating profile image for user ID {}: {}", userId, ex.getMessage());
            throw ex;
        }
    }
}
