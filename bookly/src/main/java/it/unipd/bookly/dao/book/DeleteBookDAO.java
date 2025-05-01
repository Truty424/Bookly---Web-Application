package it.unipd.bookly.dao.book;

import java.sql.Connection;
import java.sql.PreparedStatement;

import it.unipd.bookly.dao.AbstractDAO;

import static it.unipd.bookly.dao.book.BookQueries.DELETE_BOOK;

/**
 * DAO to delete a book from the database, including related data.
 */
public class DeleteBookDAO extends AbstractDAO<Boolean> {

    private final int book_id;

    public DeleteBookDAO(final Connection con, final int book_id) {
        super(con);
        this.book_id = book_id;
    }

    @Override
    protected void doAccess() throws Exception {
        boolean originalAutoCommit = con.getAutoCommit();
        con.setAutoCommit(false);

        try {
            // Step 1: Delete from category_belongs
            try (PreparedStatement stmt = con.prepareStatement("DELETE FROM booklySchema.category_belongs WHERE book_id = ?")) {
                stmt.setInt(1, book_id);
                stmt.executeUpdate();
            }

            // Step 2: Delete from writes
            try (PreparedStatement stmt = con.prepareStatement("DELETE FROM booklySchema.writes WHERE book_id = ?")) {
                stmt.setInt(1, book_id);
                stmt.executeUpdate();
            }

            // Step 3: Delete from published_by
            try (PreparedStatement stmt = con.prepareStatement("DELETE FROM booklySchema.published_by WHERE book_id = ?")) {
                stmt.setInt(1, book_id);
                stmt.executeUpdate();
            }

            // Step 4: Delete from reviews
            try (PreparedStatement stmt = con.prepareStatement("DELETE FROM booklySchema.reviews WHERE book_id = ?")) {
                stmt.setInt(1, book_id);
                stmt.executeUpdate();
            }

            // Step 5: Delete from contains
            try (PreparedStatement stmt = con.prepareStatement("DELETE FROM booklySchema.contains WHERE book_id = ?")) {
                stmt.setInt(1, book_id);
                stmt.executeUpdate();
            }

            // (Optional) Step 6: Delete from book_images if applicable
            try (PreparedStatement stmt = con.prepareStatement("DELETE FROM booklySchema.book_images WHERE book_id = ?")) {
                stmt.setInt(1, book_id);
                stmt.executeUpdate();
            }

            // Step 7: Finally, delete from books
            try (PreparedStatement stmt = con.prepareStatement(DELETE_BOOK)) {
                stmt.setInt(1, book_id);
                int affectedRows = stmt.executeUpdate();
                this.outputParam = affectedRows > 0;
            }

            if (this.outputParam) {
                LOGGER.info("Book ID {} deleted successfully (and all related data).", book_id);
            } else {
                LOGGER.warn("No book found with ID {}. Deletion skipped.", book_id);
            }

            con.commit(); 

        } catch (Exception e) {
            con.rollback();
            this.outputParam = false;
            LOGGER.error("Error deleting book {}: {}. Transaction rolled back.", book_id, e.getMessage());
            throw e;
        } finally {
            con.setAutoCommit(originalAutoCommit); // Always restore original autocommit state
        }
    }
}
