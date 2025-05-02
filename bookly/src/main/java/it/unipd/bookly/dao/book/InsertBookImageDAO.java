package it.unipd.bookly.dao.book;

import it.unipd.bookly.Resource.Image;
import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;

import static it.unipd.bookly.dao.book.BookQueries.INSERT_BOOK_IMAGE;

/**
 * DAO to insert an image for a specific book.
 */
public class InsertBookImageDAO extends AbstractDAO<Boolean> {

    private final int bookId;
    private final Image image;

    /**
     * Constructor.
     *
     * @param con Database connection
     * @param bookId The ID of the book to associate the image with
     * @param image The {@link Image} object containing image data and type
     */
    public InsertBookImageDAO(final Connection con, final int bookId, final Image image) {
        super(con);
        this.bookId = bookId;
        this.image = image;
    }

    @Override
    protected void doAccess() throws Exception {
        boolean success = false;

        try (PreparedStatement stmt = con.prepareStatement(INSERT_BOOK_IMAGE)) {
            stmt.setInt(1, bookId);
            stmt.setBytes(2, image.getPhoto());
            stmt.setString(3, image.getPhotoMediaType());

            int rowsAffected = stmt.executeUpdate();
            success = rowsAffected > 0;

            if (success) {
                LOGGER.info("Inserted image for book ID {} ({} row(s) affected).", bookId, rowsAffected);
            } else {
                LOGGER.warn("No rows affected while inserting image for book ID {}.", bookId);
            }

        } catch (Exception e) {
            LOGGER.error("Error inserting image for book ID {}: {}", bookId, e.getMessage(), e);
            throw e;
        }

        this.outputParam = success;
    }
}
