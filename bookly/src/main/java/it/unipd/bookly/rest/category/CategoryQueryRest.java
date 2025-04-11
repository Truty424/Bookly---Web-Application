package it.unipd.bookly.rest.category;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.unipd.bookly.Resource.Category;
import it.unipd.bookly.Resource.Message;
import it.unipd.bookly.dao.category.GetAllCategoriesDAO;
import it.unipd.bookly.dao.category.GetCategoryByIdDAO;
import it.unipd.bookly.dao.category.GetCategoryByNameDAO;
import it.unipd.bookly.rest.AbstractRestResource;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;



public class CategoryQueryRest extends AbstractRestResource {

    public CategoryQueryRest(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super("category-query", req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        final String method = req.getMethod();
        final String path = req.getRequestURI();

        try {
            switch (method) {
                case "GET" -> {
                    if (path.matches(".*/category/\\d+$")) {
                        handleGetById(path);
                    } else if (path.matches(".*/category/name/.+$")) {
                        handleGetByName(path);
                    } else if (path.endsWith("/category")) {
                        handleGetAll();
                    } else {
                        sendNotFound();
                    }
                }
                default -> sendMethodNotAllowed();
            }
        } catch (Exception e) {
            LOGGER.error("CategoryQueryRest error: ", e);
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            new Message("Internal server error.", "E500", e.getMessage()).toJSON(res.getOutputStream());
        }
    }

    private void handleGetAll() throws Exception {
        List<Category> categories = new GetAllCategoriesDAO(con).access().getOutputParam();
        sendJson(categories, HttpServletResponse.SC_OK);
    }

    private void handleGetById(String path) throws Exception {
        int id = Integer.parseInt(path.substring(path.lastIndexOf("/") + 1));
        Category category = new GetCategoryByIdDAO(con, id).access().getOutputParam();

        if (category == null) {
            sendMessage("Category not found.", "404", "No category with ID " + id, HttpServletResponse.SC_NOT_FOUND);
        } else {
            sendJson(category, HttpServletResponse.SC_OK);
        }
    }

    private void handleGetByName(String path) throws Exception {
        String name = path.substring(path.lastIndexOf("/") + 1);
        Category category = new GetCategoryByNameDAO(con, name).access().getOutputParam();

        if (category == null) {
            sendMessage("Category not found.", "404", "No category named '" + name + "'", HttpServletResponse.SC_NOT_FOUND);
        } else {
            sendJson(category, HttpServletResponse.SC_OK);
        }
    }

    private void sendJson(Object data, int statusCode) throws IOException {
        res.setContentType("application/json;charset=UTF-8");
        res.setStatus(statusCode);
        new ObjectMapper().writeValue(res.getOutputStream(), data);
    }

    private void sendMessage(String title, String code, String detail, int status) throws IOException {
        res.setStatus(status);
        new Message(title, code, detail).toJSON(res.getOutputStream());
    }

    private void sendNotFound() throws IOException {
        sendMessage("Invalid category query path.", "404", "Check supported routes.", HttpServletResponse.SC_NOT_FOUND);
    }

    private void sendMethodNotAllowed() throws IOException {
        sendMessage("Only GET method supported for this endpoint.", "405", "Use GET for category queries.", HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }
}
