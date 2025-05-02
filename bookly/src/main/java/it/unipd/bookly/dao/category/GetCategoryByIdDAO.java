package it.unipd.bookly.dao.category;

import it.unipd.bookly.Resource.Category;
import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static it.unipd.bookly.dao.category.CategoryQueries.GET_CATEGORY_BY_ID;

/**
 * DAO class to retrieve a category by its ID.
 */
public class GetCategoryByIdDAO extends AbstractDAO<Category> {

    private final int category_id;

    /**
     * Constructor to initialize the DAO with the given category ID.
     *
     * @param con          the database connection.
     * @param category_id  the ID of the category to retrieve.
     */
    public GetCategoryByIdDAO(final Connection con, final int category_id) {
        super(con);
        this.category_id = category_id;
    }

    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmt = con.prepareStatement(GET_CATEGORY_BY_ID)) {
            stmt.setInt(1, category_id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Category category = new Category(
                            rs.getInt("category_id"),
                            rs.getString("category_name") != null ? rs.getString("category_name") : "",
                            rs.getString("description") != null ? rs.getString("description") : ""
                            // Add additional fields here if your query returns more columns, e.g., images.
                    );

                    this.outputParam = category;
                    LOGGER.info("Category with ID {} retrieved successfully.", category_id);
                } else {
                    this.outputParam = null;
                    LOGGER.warn("No category found for ID {}.", category_id);
                }
            }
        } catch (Exception ex) {
            LOGGER.error("Failed to retrieve category with ID {}: {}", category_id, ex.getMessage(), ex);
            throw ex;
        }
    }
}
