package it.unipd.bookly.rest.category;

import it.unipd.bookly.Resource.Message;
import it.unipd.bookly.dao.category.AddCategoryToBookDAO;
import it.unipd.bookly.dao.category.RemoveCategoryFromBookDAO;
import it.unipd.bookly.rest.AbstractRestResource;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;

/**
 * Handles:
 * - POST /api/books/{bookId}/category/{categoryId}
 * - DELETE /api/books/{bookId}/category/{categoryId}
 */
public class CategoryAssignmentRest extends AbstractRestResource {

    public CategoryAssignmentRest(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super("category-assignment", req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        final String method = req.getMethod();
        final String path = req.getRequestURI();

        try {
            switch (method) {
                case "POST" -> {
                    if (path.matches(".*/books/\\d+/category/\\d+$")) {
                        handleAddCategory(path);
                    } else {
                        sendUnsupportedPath();
                    }
                }
                case "DELETE" -> {
                    if (path.matches(".*/books/\\d+/category/\\d+$")) {
                        handleRemoveCategory(path);
                    } else {
                        sendUnsupportedPath();
                    }
                }
                default -> sendUnsupportedMethod();
            }
        } catch (Exception e) {
            LOGGER.error("CategoryAssignmentRest error: ", e);
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            new Message("Internal server error", "E500", e.getMessage()).toJSON(res.getOutputStream());
        }
    }

    private void handleAddCategory(String path) throws Exception {
        int[] ids = extractIds(path);
        new AddCategoryToBookDAO(con, ids[0], ids[1]).access();

        res.setStatus(HttpServletResponse.SC_CREATED);
        new Message("Category assigned to book successfully.", "201", 
                    "Category ID " + ids[1] + " -> Book ID " + ids[0])
            .toJSON(res.getOutputStream());
    }

    private void handleRemoveCategory(String path) throws Exception {
        int[] ids = extractIds(path);
        new RemoveCategoryFromBookDAO(con, ids[0], ids[1]).access();

        res.setStatus(HttpServletResponse.SC_OK);
        new Message("Category removed from book.", "200", 
                    "Category ID " + ids[1] + " removed from Book ID " + ids[0])
            .toJSON(res.getOutputStream());
    }

    private int[] extractIds(String path) {
        String[] parts = path.split("/");
        int bookId = Integer.parseInt(parts[parts.length - 3]);
        int categoryId = Integer.parseInt(parts[parts.length - 1]);
        return new int[]{bookId, categoryId};
    }

    private void sendUnsupportedMethod() throws IOException {
        res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        new Message("Unsupported HTTP method.", "405", "Only POST and DELETE are supported.")
            .toJSON(res.getOutputStream());
    }

    private void sendUnsupportedPath() throws IOException {
        res.setStatus(HttpServletResponse.SC_NOT_FOUND);
        new Message("Invalid path format.", "404", "Expected /api/books/{bookId}/category/{categoryId}.")
            .toJSON(res.getOutputStream());
    }
}
