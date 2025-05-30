package it.unipd.bookly.dao.image;

import it.unipd.bookly.Resource.Image;
import it.unipd.bookly.dao.AbstractDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookImageLoaderDAO extends AbstractDAO<Image> {

    private static final Logger LOGGER = LogManager.getLogger(BookImageLoaderDAO.class);

    private static final String LOAD_BOOK_IMAGE_SQL =
        "SELECT image, image_type FROM booklySchema.book_image WHERE book_id = ?";

    private final int bookId;

    public BookImageLoaderDAO(Connection con, int bookId) {
        super(con);
        this.bookId = bookId;
    }

    @Override
    protected void doAccess() throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(LOAD_BOOK_IMAGE_SQL)) {
            ps.setInt(1, bookId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    byte[] imageBytes = rs.getBytes("image");
                    String imageType = rs.getString("image_type");

                    if (imageBytes != null && imageType != null) {
                        this.outputParam = new Image(imageBytes, imageType);
                        LOGGER.info("Book image loaded for ID {}", bookId);
                    } else {
                        this.outputParam = null;
                        LOGGER.warn("Image or type is null for book ID {}", bookId);
                    }
                } else {
                    this.outputParam = null;
                    LOGGER.warn("No image found for book ID {}", bookId);
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to load image for book ID {}: {}", bookId, e.getMessage());
            throw e;
        }
    }
}
