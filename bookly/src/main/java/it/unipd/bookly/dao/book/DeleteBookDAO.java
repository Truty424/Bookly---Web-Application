package it.unipd.bookly.dao.book;

import java.sql.Connection;
import java.sql.PreparedStatement;

import it.unipd.bookly.dao.AbstractDAO;

import static it.unipd.bookly.dao.book.BookQueries.DELETE_BOOK;
import static it.unipd.bookly.dao.book.BookQueries.DELETE_BOOK_IMAGE;

/**
 * DAO to delete a book from the database, including related data (categories, authors, publishers, reviews, images).
 */
public class DeleteBookDAO extends AbstractDAO<Boolean> {

    private final int bookId;

    public DeleteBookDAO(final Connection con, final int bookId) {
        super(con);
        this.bookId = bookId;
    }

    @Override
    protected void doAccess() throws Exception {
        boolean originalAutoCommit = con.getAutoCommit();
        con.setAutoCommit(false);

        try {
            // Step 1: Delete from category_belongs
            try (PreparedStatement stmt = con.prepareStatement(
                    "DELETE FROM booklySchema.category_belongs WHERE book_id = ?")) {
                stmt.setInt(1, bookId);
                stmt.executeUpdate();
            }

            // Step 2: Delete from writes
            try (PreparedStatement stmt = con.prepareStatement(
                    "DELETE FROM booklySchema.writes WHERE book_id = ?")) {
                stmt.setInt(1, bookId);
                stmt.executeUpdate();
            }

            // Step 3: Delete from published_by
            try (PreparedStatement stmt = con.prepareStatement(
                    "DELETE FROM booklySchema.published_by WHERE book_id = ?")) {
                stmt.setInt(1, bookId);
                stmt.executeUpdate();
            }

            // Step 4: Delete from reviews
            try (PreparedStatement stmt = con.prepareStatement(
                    "DELETE FROM booklySchema.reviews WHERE book_id = ?")) {
                stmt.setInt(1, bookId);
                stmt.executeUpdate();
            }

            // Step 5: Delete from contains (if you have an orders system or similar)
            try (PreparedStatement stmt = con.prepareStatement(
                    "DELETE FROM booklySchema.contains WHERE book_id = ?")) {
                stmt.setInt(1, bookId);
                stmt.executeUpdate();
            }

            // Step 6: Delete image (optional but recommended)
            try (PreparedStatement stmt = con.prepareStatement(DELETE_BOOK_IMAGE)) {
                stmt.setInt(1, bookId);
                stmt.executeUpdate();
            }

            // Step 7: Finally delete from books
            try (PreparedStatement stmt = con.prepareStatement(DELETE_BOOK)) {
                stmt.setInt(1, bookId);
                int affectedRows = stmt.executeUpdate();
                this.outputParam = affectedRows > 0;
            }

            if (this.outputParam) {
                LOGGER.info("Book ID {} and all related data deleted successfully.", bookId);
            } else {
                LOGGER.warn("No book found with ID {}. Nothing was deleted.", bookId);
            }

            con.commit();

        } catch (Exception e) {
            con.rollback();
            this.outputParam = false;
            LOGGER.error("Error deleting book ID {}: {}. Transaction rolled back.", bookId, e.getMessage(), e);
            throw e;
        } finally {
            // Always restore autocommit
            try {
                con.setAutoCommit(originalAutoCommit);
            } catch (Exception e) {
                LOGGER.warn("Failed to restore autocommit state after deleting book ID {}.", bookId, e);
            }
        }
    }
}
