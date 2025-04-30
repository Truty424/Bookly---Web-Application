package it.unipd.bookly.dao.user;

import it.unipd.bookly.Resource.Image;
import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static it.unipd.bookly.dao.user.UserQueries.GET_USER_IMAGE;

/**
 * DAO to fetch a user's profile image from the database.
 */
public class GetUserImageDAO extends AbstractDAO<Image> {

    private final int userId;

    /**
     * Constructor to get image for a given user ID.
     *
     * @param con    database connection
     * @param userId user ID whose image is to be retrieved
     */
    public GetUserImageDAO(Connection con, int userId) {
        super(con);
        this.userId = userId;
    }

    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmt = con.prepareStatement(GET_USER_IMAGE)) {
            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    byte[] imageData = rs.getBytes("image");
                    String imageType = rs.getString("image_type");

                    if (imageData != null && imageType != null) {
                        this.outputParam = new Image(imageData, imageType);
                        LOGGER.info("Successfully fetched profile image for user ID {}", userId);
                    } else {
                        this.outputParam = null;
                        LOGGER.warn("User ID {} has no profile image stored.", userId);
                    }
                } else {
                    this.outputParam = null;
                    LOGGER.warn("No profile image found for user ID {}", userId);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error retrieving profile image for user ID {}: {}", userId, e.getMessage(), e);
            throw e;
        }
    }
}
