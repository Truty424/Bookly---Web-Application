package it.unipd.bookly.dao.category;

import it.unipd.bookly.Resource.Category;
import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static it.unipd.bookly.dao.category.CategoryQueries.GET_ALL_CATEGORIES;

/**
 * DAO class to retrieve all categories from the database.
 */
public class GetAllCategoriesDAO extends AbstractDAO<List<Category>> {

    public GetAllCategoriesDAO(final Connection con) {
        super(con);
    }

    @Override
    protected void doAccess() throws Exception {
        List<Category> categories = new ArrayList<>();

        try (PreparedStatement stmt = con.prepareStatement(GET_ALL_CATEGORIES);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int categoryId = rs.getInt("category_id");
                String categoryName = rs.getString("category_name");
                String description = rs.getString("description");

                Category category = new Category(categoryId, categoryName, description);
                categories.add(category);

                LOGGER.debug("Loaded category: ID={} Name='{}'", categoryId, categoryName);
            }

            this.outputParam = categories;

            if (categories.isEmpty()) {
                LOGGER.warn("⚠️ No categories found in the database.");
            } else {
                LOGGER.info("✅ Retrieved {} categories from the database.", categories.size());
            }

        } catch (Exception ex) {
            LOGGER.error("❌ Error retrieving categories: {}", ex.getMessage(), ex);
            throw ex;
        }
    }
}
