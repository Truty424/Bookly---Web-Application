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

    private final String categoryName;

    /**
     * Constructor.
     *
     * @param con          the database connection.
     * @param categoryName the name of the category to retrieve.
     */
    public GetCategoryByNameDAO(final Connection con, final String categoryName) {
        super(con);
        this.categoryName = categoryName;
    }

    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmnt = con.prepareStatement(GET_CATEGORY_BY_NAME)) {
            stmnt.setString(1, categoryName);

            try (ResultSet rs = stmnt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("category_id");
                    String name = rs.getString("category_name");
                    String description = rs.getString("description");

                    Category category = new Category(
                            id,
                            name != null ? name : "",
                            description != null ? description : ""
                    );

                    this.outputParam = category;
                    LOGGER.info("Category '{}' retrieved successfully by name.", categoryName);
                } else {
                    this.outputParam = null;
                    LOGGER.warn("No category found with name '{}'.", categoryName);
                }
            }

        } catch (Exception ex) {
            LOGGER.error("Failed to retrieve category with name '{}': {}", categoryName, ex.getMessage());
            throw ex;
        }
    }
}
