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
     * @param con    the database connection.
     * @param book_id the ID of the book.
     */
    public GetCategoriesByBookDAO(final Connection con, final int book_id) {
        super(con);
        this.book_id = book_id;
    }

    @Override
    protected void doAccess() throws Exception {
        List<Category> categories = new ArrayList<>();

        try (PreparedStatement stmt = con.prepareStatement(GET_CATEGORIES_BY_BOOK)) {
            stmt.setInt(1, book_id);

            try (ResultSet rs = stmt.executeQuery()) {
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
                    LOGGER.debug("Loaded category: ID={} Name='{}'", categoryId, categoryName);
                }
            }

            if (categories.isEmpty()) {
                LOGGER.warn("No categories found for book ID {}", book_id);
            } else {
                LOGGER.info("✅ Retrieved {} category(ies) for book ID {}.", categories.size(), book_id);
            }

            this.outputParam = categories;

        } catch (Exception ex) {
            LOGGER.error("❌ Failed to retrieve categories for book ID {}: {}", book_id, ex.getMessage(), ex);
            throw ex;
        }
    }
}
