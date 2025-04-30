package it.unipd.bookly.dao.image;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import it.unipd.bookly.Resource.Image;
import it.unipd.bookly.dao.AbstractDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BookImageLoaderDAO extends AbstractDAO<Image> {

    private static final Logger LOGGER = LogManager.getLogger(BookImageLoaderDAO.class);
    private static final String STATEMENT_LOAD_BOOK_IMAGE =
        "SELECT * FROM booklySchema.book_image WHERE book_id = ?";

    private final int book_id;

    public BookImageLoaderDAO(final Connection con, int book_id) {
        super(con);
        this.book_id = book_id;
    }

    @Override
    protected void doAccess() throws SQLException {
        try (PreparedStatement pstmt = con.prepareStatement(STATEMENT_LOAD_BOOK_IMAGE)) {
            pstmt.setInt(1, book_id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    this.outputParam = new Image(rs.getBytes("image"), rs.getString("image_type"));
                    LOGGER.info("Loaded image for book ID {}", book_id);
                } else {
                    this.outputParam = null;
                    LOGGER.warn("No image found for book ID {}", book_id);
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Error loading image for book ID {}: {}", book_id, e.getMessage());
            throw e;
        }
    }
}
