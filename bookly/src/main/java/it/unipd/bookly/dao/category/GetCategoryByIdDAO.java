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

    private final int categoryId;

    /**
     * Constructor to initialize the DAO with the given book ID.
     *
     * @param con        the database connection.
     * @param categoryId the ID of the category to retrieve.
     */
    public GetCategoryByIdDAO(final Connection con, final int categoryId) {
        super(con);
        this.categoryId = categoryId;
    }

    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmnt = con.prepareStatement(GET_CATEGORY_BY_ID)) {
            stmnt.setInt(1, categoryId);

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
                    LOGGER.info("Category with ID {} retrieved successfully.", categoryId);
                } else {
                    this.outputParam = null;
                    LOGGER.warn("No category found for ID {}.", categoryId);
                }
            }

        } catch (Exception ex) {
            LOGGER.error("Failed to retrieve category with ID {}: {}", categoryId, ex.getMessage());
            throw ex;
        }
    }
}
