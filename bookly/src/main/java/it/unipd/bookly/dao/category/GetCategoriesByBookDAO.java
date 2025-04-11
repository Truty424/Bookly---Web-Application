package it.unipd.bookly.dao.category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import it.unipd.bookly.Resource.Category;
import it.unipd.bookly.dao.AbstractDAO;
import static it.unipd.bookly.dao.category.CategoryQueries.GET_CATEGORIES_BY_BOOK;

/**
 * DAO class to retrieve all categories associated with a specific book.
 */
public class GetCategoriesByBookDAO extends AbstractDAO<List<Category>> {

    private final int book_id;

    /**
     * Constructor to create the DAO instance.
     *
     * @param con the database connection.
     * @param book_id the ID of the book.
     */
    public GetCategoriesByBookDAO(final Connection con, final int book_id) {
        super(con);
        this.book_id = book_id;
    }

    @Override
    protected void doAccess() throws Exception {
        List<Category> categories = new ArrayList<>();

        try (PreparedStatement stmnt = con.prepareStatement(GET_CATEGORIES_BY_BOOK)) {
            stmnt.setInt(1, book_id);

            try (ResultSet rs = stmnt.executeQuery()) {
                while (rs.next()) {
                    int category_id = rs.getInt("category_id");
                    String category_name = rs.getString("category_name");
                    String description = rs.getString("description");

                    Category category = new Category(
                            category_id,
                            category_name != null ? category_name : "",
                            description != null ? description : ""
                    );

                    categories.add(category);
                }
            }

            this.outputParam = categories;
            LOGGER.info("Retrieved {} category(ies) for book ID {}.", categories.size(), book_id);

        } catch (Exception ex) {
            LOGGER.error("Failed to retrieve categories for book ID {}: {}", book_id, ex.getMessage());
            throw ex;
        }
    }
}
