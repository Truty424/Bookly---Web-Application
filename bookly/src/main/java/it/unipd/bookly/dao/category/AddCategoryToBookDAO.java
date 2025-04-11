package it.unipd.bookly.dao.category;

import java.sql.Connection;
import java.sql.PreparedStatement;

import it.unipd.bookly.dao.AbstractDAO;
import static it.unipd.bookly.dao.category.CategoryQueries.ADD_CATEGORY_TO_BOOK;

/**
 * DAO class to assign a category to a book.
 */
public class AddCategoryToBookDAO extends AbstractDAO<Void> {

    private final int book_id;
    private final int category_id;

    public AddCategoryToBookDAO(final Connection con, final int book_id, final int category_id) {
        super(con);
        this.book_id = book_id;
        this.category_id = category_id;
    }

    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmnt = con.prepareStatement(ADD_CATEGORY_TO_BOOK)) {
            stmnt.setInt(1, book_id);
            stmnt.setInt(2, category_id);

            stmnt.executeUpdate();
            LOGGER.info("Category {} successfully assigned to book {}.", category_id, book_id);

        } catch (Exception ex) {
            LOGGER.error("Error assigning category {} to book {}: {}", category_id, book_id, ex.getMessage());
            throw ex;
        }
    }
}
