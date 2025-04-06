package it.unipd.bookly.dao.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import it.unipd.bookly.Resource.Image;
import it.unipd.bookly.dao.AbstractDAO;

import static it.unipd.bookly.dao.user.UserQueries.INSERT_USER_IMAGE;

/**
 * DAO to insert or update a user's profile image.
 */
public class InsertUserImageDAO extends AbstractDAO<Boolean> {

    private final int userId;
    private final Image image;

    /**
     * Constructor.
     *
     * @param con     the database connection
     * @param userId  the ID of the user
     * @param image   the Image object containing photo data and type
     */
    public InsertUserImageDAO(Connection con, int userId, Image image) {
        super(con);
        this.userId = userId;
        this.image = image;
    }

    @Override
    protected void doAccess() throws SQLException {
        try (PreparedStatement stmt = con.prepareStatement(INSERT_USER_IMAGE)) {
            stmt.setInt(1, userId);
            stmt.setBytes(2, image.getPhoto());
            stmt.setString(3, image.getPhotoMediaType());

            int rows = stmt.executeUpdate();
            this.outputParam = rows > 0;

            if (outputParam) {
                LOGGER.info("Profile image inserted for user ID {}", userId);
            } else {
                LOGGER.warn("No profile image inserted for user ID {}", userId);
            }

        } catch (SQLException e) {
            LOGGER.error("Failed to insert image for user ID {}: {}", userId, e.getMessage());
            throw e;
        }
    }
}
