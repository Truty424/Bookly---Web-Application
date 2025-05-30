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

    private final int book_id;
    private final Image image;

    /**
     * Constructor.
     *
     * @param con Database connection
     * @param book_id The ID of the book to associate the image with
     * @param image The {@link Image} object containing image data and type
     */
    public InsertBookImageDAO(final Connection con, final int book_id, final Image image) {
        super(con);
        this.book_id = book_id;
        this.image = image;
    }

    @Override
    protected void doAccess() throws Exception {
        boolean success = false;

        try (PreparedStatement stmt = con.prepareStatement(INSERT_BOOK_IMAGE)) {
            stmt.setInt(1, book_id);
            stmt.setBytes(2, image.getPhoto());
            stmt.setString(3, image.getPhotoMediaType());

            int rowsAffected = stmt.executeUpdate();
            
            success = rowsAffected > 0;

            if (success) {
                LOGGER.info("Inserted image for book ID {} ({} row(s) affected).", book_id, rowsAffected);
            } else {
                LOGGER.warn("No rows affected while inserting image for book ID {}.", book_id);
            }

        } catch (Exception e) {
            LOGGER.error("Error inserting image for book ID {}: {}", book_id, e.getMessage(), e);
            throw e;
        }

        this.outputParam = success;
    }
}
