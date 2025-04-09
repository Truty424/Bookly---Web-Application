package it.unipd.bookly.dao.category;

import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;

import static it.unipd.bookly.dao.category.CategoryQueries.ADD_CATEGORY_TO_BOOK;

/**
 * DAO class to assign a category to a book.
 */
public class AddCategoryToBookDAO extends AbstractDAO<Void> {

    private final int bookId;
    private final int category_id;


    public AddCategoryToBookDAO(final Connection con, final int bookId, final int category_id) {
        super(con);
        this.bookId = bookId;
        this.category_id = category_id;
    }

    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmnt = con.prepareStatement(ADD_CATEGORY_TO_BOOK)) {
            stmnt.setInt(1, bookId);
            stmnt.setInt(2, category_id);

            stmnt.executeUpdate();
            LOGGER.info("Category {} successfully assigned to book {}.", category_id, bookId);

        } catch (Exception ex) {
            LOGGER.error("Error assigning category {} to book {}: {}", category_id, bookId, ex.getMessage());
            throw ex;
        }
    }
}
