package it.unipd.bookly.dao.category;

import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;

import static it.unipd.bookly.dao.category.CategoryQueries.REMOVE_CATEGORY_FROM_BOOK;

/**
 * DAO class to remove a category from a book.
 */
public class RemoveCategoryFromBookDAO extends AbstractDAO<Void> {

    private final int bookId;
    private final int categoryId;


    public RemoveCategoryFromBookDAO(final Connection con, final int bookId, final int categoryId) {
        super(con);
        this.bookId = bookId;
        this.categoryId = categoryId;
    }

    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmnt = con.prepareStatement(REMOVE_CATEGORY_FROM_BOOK)) {
            stmnt.setInt(1, bookId);
            stmnt.setInt(2, categoryId);

            int rowsAffected = stmnt.executeUpdate();

            if (rowsAffected > 0) {
                LOGGER.info("Category {} removed from book {}.", categoryId, bookId);
            } else {
                LOGGER.warn("No relationship found between book {} and category {} to remove.", bookId, categoryId);
            }

        } catch (Exception ex) {
            LOGGER.error("Error removing category {} from book {}: {}", categoryId, bookId, ex.getMessage());
            throw ex;
        }
    }
}
