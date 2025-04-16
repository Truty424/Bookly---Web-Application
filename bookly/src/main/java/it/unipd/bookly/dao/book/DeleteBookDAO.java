package it.unipd.bookly.dao.book;

import java.sql.Connection;
import java.sql.PreparedStatement;

import it.unipd.bookly.dao.AbstractDAO;
import static it.unipd.bookly.dao.book.BookQueries.DELETE_BOOK;

/**
 * DAO to delete a book from the database.
 * This class provides functionality to remove a book record
 * from the database using the book's unique ID.
 */
public class DeleteBookDAO extends AbstractDAO<Boolean> {

    /**
     * The ID of the book to be deleted.
     */
    private final int book_id;

    /**
     * Constructs a DAO to delete a book from the database.
     *
     * @param con     The database connection to use.
     * @param book_id The ID of the book to be deleted.
     */
    public DeleteBookDAO(final Connection con, final int book_id) {
        super(con);
        this.book_id = book_id;
    }

    /**
     * Executes the query to delete a book from the database.
     * Populates the {@link #outputParam} with {@code true} if the deletion was successful, {@code false} otherwise.
     *
     * @throws Exception If an error occurs during the database operation.
     */
    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement ps = con.prepareStatement(DELETE_BOOK)) {
            ps.setInt(1, book_id);
            int affectedRows = ps.executeUpdate();
            this.outputParam = affectedRows > 0;
        } catch (Exception e) {
            LOGGER.error("Error deleting book {}: {}", book_id, e.getMessage());
        }
    }
}