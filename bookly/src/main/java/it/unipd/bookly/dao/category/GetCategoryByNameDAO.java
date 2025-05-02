package it.unipd.bookly.dao.category;

import it.unipd.bookly.Resource.Category;
import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static it.unipd.bookly.dao.category.CategoryQueries.GET_CATEGORY_BY_NAME;

/**
 * DAO class to retrieve a category by its name (case-insensitive).
 */
public class GetCategoryByNameDAO extends AbstractDAO<Category> {

    private final String category_name;

    /**
     * Constructor.
     *
     * @param con           the database connection.
     * @param category_name the name of the category to retrieve.
     */
    public GetCategoryByNameDAO(final Connection con, final String category_name) {
        super(con);
        this.category_name = category_name;
    }

    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmt = con.prepareStatement(GET_CATEGORY_BY_NAME)) {
            stmt.setString(1, category_name);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Category category = new Category(
                            rs.getInt("category_id"),
                            rs.getString("category_name") != null ? rs.getString("category_name") : "",
                            rs.getString("description") != null ? rs.getString("description") : ""
                            // If your query includes more fields (like an image), you can add them here.
                    );

                    this.outputParam = category;
                    LOGGER.info("Category '{}' retrieved successfully by name.", category_name);
                } else {
                    this.outputParam = null;
                    LOGGER.warn("No category found with name '{}'.", category_name);
                }
            }

        } catch (Exception ex) {
            LOGGER.error("Failed to retrieve category with name '{}': {}", category_name, ex.getMessage(), ex);
            throw ex;
        }
    }
}
