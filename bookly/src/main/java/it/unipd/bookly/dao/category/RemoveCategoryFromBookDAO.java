package it.unipd.bookly.dao.category;

import java.sql.Connection;
import java.sql.PreparedStatement;

import it.unipd.bookly.dao.AbstractDAO;
import static it.unipd.bookly.dao.category.CategoryQueries.REMOVE_CATEGORY_FROM_BOOK;

/**
 * DAO class to remove a category assignment from a specific book.
 */
public class RemoveCategoryFromBookDAO extends AbstractDAO<Void> {

    private final int book_id;
    private final int category_id;

    /**
     * Constructs the DAO for removing a category from a book.
     *
     * @param con the database connection
     * @param book_id the ID of the book
     * @param category_id the ID of the category to remove
     */
    public RemoveCategoryFromBookDAO(final Connection con, final int book_id, final int category_id) {
        super(con);
        this.book_id = book_id;
        this.category_id = category_id;
    }

    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmnt = con.prepareStatement(REMOVE_CATEGORY_FROM_BOOK)) {
            stmnt.setInt(1, book_id);
            stmnt.setInt(2, category_id);

            int rowsAffected = stmnt.executeUpdate();

            if (rowsAffected > 0) {
                LOGGER.info("Removed category {} from book {}.", category_id, book_id);
            } else {
                LOGGER.warn("No category-book link found to remove (book ID: {}, category ID: {}).", book_id, category_id);
            }

        } catch (Exception ex) {
            LOGGER.error("Failed to remove category {} from book {}: {}", category_id, book_id, ex.getMessage());
            throw ex;
        }
    }
}
