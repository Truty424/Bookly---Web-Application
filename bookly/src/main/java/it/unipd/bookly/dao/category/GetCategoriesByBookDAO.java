package it.unipd.bookly.dao.category;

import it.unipd.bookly.Resource.Category;
import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static it.unipd.bookly.dao.category.CategoryQueries.GET_CATEGORIES_BY_BOOK;

/**
 * DAO class to retrieve all categories associated with a specific book.
 */
public class GetCategoriesByBookDAO extends AbstractDAO<List<Category>> {

    private final int bookId;

    /**
     * Constructor to create the DAO instance.
     *
     * @param con    the database connection.
     * @param bookId the ID of the book.
     */
    public GetCategoriesByBookDAO(final Connection con, final int bookId) {
        super(con);
        this.bookId = bookId;
    }

    @Override
    protected void doAccess() throws Exception {
        List<Category> categories = new ArrayList<>();

        try (PreparedStatement stmnt = con.prepareStatement(GET_CATEGORIES_BY_BOOK)) {
            stmnt.setInt(1, bookId);

            try (ResultSet rs = stmnt.executeQuery()) {
                while (rs.next()) {
                    int categoryId = rs.getInt("category_id");
                    String categoryName = rs.getString("category_name");
                    String description = rs.getString("description");

                    Category category = new Category(
                            categoryId,
                            categoryName != null ? categoryName : "",
                            description != null ? description : ""
                    );

                    categories.add(category);
                }
            }

            this.outputParam = categories;
            LOGGER.info("Retrieved {} category(ies) for book ID {}.", categories.size(), bookId);

        } catch (Exception ex) {
            LOGGER.error("Failed to retrieve categories for book ID {}: {}", bookId, ex.getMessage());
            throw ex;
        }
    }
}
