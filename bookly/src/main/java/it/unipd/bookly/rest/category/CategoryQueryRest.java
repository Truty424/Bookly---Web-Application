package it.unipd.bookly.rest.category;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.unipd.bookly.Resource.Category;
import it.unipd.bookly.Resource.Message;
import it.unipd.bookly.dao.category.GetAllCategoriesDAO;
import it.unipd.bookly.dao.category.GetCategoryByIdDAO;
import it.unipd.bookly.dao.category.GetCategoryByNameDAO;
import it.unipd.bookly.rest.AbstractRestResource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Handles category information retrieval:
 * - GET /api/category
 * - GET /api/category/{id}
 * - GET /api/category/name/{name}
 */
public class CategoryQueryRest extends AbstractRestResource {

    public CategoryQueryRest(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super("category-query", req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        String method = req.getMethod();
        String path = req.getRequestURI();

        try {
            if (!"GET".equals(method)) {
                res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                new Message("Only GET method supported for this endpoint.", "405", "Use GET for category queries.")
                    .toJSON(res.getOutputStream());
                return;
            }

            if (path.matches(".*/category/\\d+$")) {
                handleGetById(path);
            } else if (path.matches(".*/category/name/.+$")) {
                handleGetByName(path);
            } else if (path.endsWith("/category")) {
                handleGetAll();
            } else {
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                new Message("Invalid category query path.", "404", "Check supported routes.")
                    .toJSON(res.getOutputStream());
            }

        } catch (Exception e) {
            LOGGER.error("CategoryQueryRest error: ", e);
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            new Message("Internal server error.", "E500", e.getMessage()).toJSON(res.getOutputStream());
        }
    }

    private void handleGetAll() throws Exception {
        List<Category> categories = new GetAllCategoriesDAO(con).access().getOutputParam();
        res.setContentType("application/json;charset=UTF-8");
        res.setStatus(HttpServletResponse.SC_OK);
        new ObjectMapper().writeValue(res.getOutputStream(), categories);
    }

    private void handleGetById(String path) throws Exception {
        int id = Integer.parseInt(path.substring(path.lastIndexOf("/") + 1));
        Category category = new GetCategoryByIdDAO(con, id).access().getOutputParam();

        if (category == null) {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            new Message("Category not found.", "404", "No category with ID " + id).toJSON(res.getOutputStream());
        } else {
            res.setContentType("application/json;charset=UTF-8");
            res.setStatus(HttpServletResponse.SC_OK);
            new ObjectMapper().writeValue(res.getOutputStream(), category);
        }
    }

    private void handleGetByName(String path) throws Exception {
        String name = path.substring(path.lastIndexOf("/") + 1);
        Category category = new GetCategoryByNameDAO(con, name).access().getOutputParam();

        if (category == null) {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            new Message("Category not found.", "404", "No category named '" + name + "'")
                .toJSON(res.getOutputStream());
        } else {
            res.setContentType("application/json;charset=UTF-8");
            res.setStatus(HttpServletResponse.SC_OK);
            new ObjectMapper().writeValue(res.getOutputStream(), category);
        }
    }
}
