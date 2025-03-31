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
                    Category category = new Category(
                            rs.getInt("category_id"),
                            rs.getString("category_name"),
                            rs.getString("description")
                    );
                    this.outputParam = category;
                    LOGGER.info("Category retrieved for ID {}.", categoryId);
                } else {
                    this.outputParam = null;
                    LOGGER.warn("No category found for ID {}.", categoryId);
                }
            }

        } catch (Exception ex) {
            LOGGER.error("Error retrieving category with ID {}: {}", categoryId, ex.getMessage());
            throw ex;
        }
    }
}
