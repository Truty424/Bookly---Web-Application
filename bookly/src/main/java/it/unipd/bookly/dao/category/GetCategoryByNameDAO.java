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
                    Category category = new Category(
                            rs.getInt("category_id"),
                            rs.getString("category_name"),
                            rs.getString("description")
                    );
                    this.outputParam = category;
                    LOGGER.info("Category found by name '{}'.", categoryName);
                } else {
                    this.outputParam = null;
                    LOGGER.warn("No category found with name '{}'.", categoryName);
                }
            }

        } catch (Exception ex) {
            LOGGER.error("Error retrieving category with name '{}': {}", categoryName, ex.getMessage());
            throw ex;
        }
    }
}
