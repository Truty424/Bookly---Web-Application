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

        try (PreparedStatement stmnt = con.prepareStatement(GET_ALL_CATEGORIES);
             ResultSet rs = stmnt.executeQuery()) {

            while (rs.next()) {
                Category category = new Category(
                        rs.getInt("category_id"),
                        rs.getString("category_name"),
                        rs.getString("description")
                );
                categories.add(category);
            }

            this.outputParam = categories;
            LOGGER.info("Retrieved {} categories from the database.", categories.size());

        } catch (Exception ex) {
            LOGGER.error("Error retrieving categories: {}", ex.getMessage());
            throw ex;
        }
    }
}
