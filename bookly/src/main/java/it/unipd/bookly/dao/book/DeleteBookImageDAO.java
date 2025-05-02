package it.unipd.bookly.dao.book;

import java.sql.Connection;
import java.sql.PreparedStatement;

import it.unipd.bookly.dao.AbstractDAO;

import static it.unipd.bookly.dao.book.BookQueries.DELETE_BOOK_IMAGE;

/**
 * DAO to delete the image of a specific book from the database.
 */
public class DeleteBookImageDAO extends AbstractDAO<Boolean> {

    private final int bookId;

    /**
     * Constructs the DAO.
     *
     * @param con     The database connection.
     * @param bookId  The ID of the book whose image will be deleted.
     */
    public DeleteBookImageDAO(final Connection con, final int bookId) {
        super(con);
        this.bookId = bookId;
    }

    /**
     * Executes the delete operation for the book's image.
     *
     * @throws Exception if any SQL or DB error occurs.
     */
    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmt = con.prepareStatement(DELETE_BOOK_IMAGE)) {
            stmt.setInt(1, bookId);
            int affectedRows = stmt.executeUpdate();
            this.outputParam = affectedRows > 0;

            if (this.outputParam) {
                LOGGER.info("Image for book ID {} deleted successfully.", bookId);
            } else {
                LOGGER.warn("No image found for book ID {}. Nothing was deleted.", bookId);
            }
        } catch (Exception e) {
            LOGGER.error("Error deleting image for book ID {}: {}", bookId, e.getMessage(), e);
            throw e;
        }
    }
}
