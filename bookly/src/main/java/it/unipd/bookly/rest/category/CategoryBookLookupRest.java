package it.unipd.bookly.rest.category;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.unipd.bookly.Resource.Book;
import it.unipd.bookly.Resource.Category;
import it.unipd.bookly.Resource.Message;
import it.unipd.bookly.dao.category.GetBooksByCategoryDAO;
import it.unipd.bookly.dao.category.GetCategoriesByBookDAO;
import it.unipd.bookly.rest.AbstractRestResource;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

/**
 * Handles:
 * - GET /api/category/{categoryId}/books
 * - GET /api/books/{bookId}/categories
 */
public class CategoryBookLookupRest extends AbstractRestResource {

    public CategoryBookLookupRest(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super("category-book-lookup", req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        final String method = req.getMethod();
        final String path = req.getRequestURI();

        try {
            switch (method) {
                case "GET" -> {
                    if (path.matches(".*/category/\\d+/books$")) {
                        handleGetBooksByCategory(path);
                    } else if (path.matches(".*/books/\\d+/categories$")) {
                        handleGetCategoriesByBook(path);
                    } else {
                        sendNotFound();
                    }
                }
                default -> sendMethodNotAllowed();
            }
        } catch (Exception e) {
            LOGGER.error("CategoryBookLookupRest error: ", e);
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            new Message("Internal server error", "E500", e.getMessage()).toJSON(res.getOutputStream());
        }
    }

    private void handleGetBooksByCategory(String path) throws Exception {
        int categoryId = extractId(path, "/category/", "/books");
        List<Book> books = new GetBooksByCategoryDAO(con, categoryId).access().getOutputParam();

        res.setContentType("application/json;charset=UTF-8");
        res.setStatus(HttpServletResponse.SC_OK);
        new ObjectMapper().writeValue(res.getOutputStream(), books);
    }

    private void handleGetCategoriesByBook(String path) throws Exception {
        int bookId = extractId(path, "/books/", "/categories");
        List<Category> categories = new GetCategoriesByBookDAO(con, bookId).access().getOutputParam();

        res.setContentType("application/json;charset=UTF-8");
        res.setStatus(HttpServletResponse.SC_OK);
        new ObjectMapper().writeValue(res.getOutputStream(), categories);
    }

    private int extractId(String path, String prefix, String suffix) {
        return Integer.parseInt(path.substring(
                path.indexOf(prefix) + prefix.length(),
                path.indexOf(suffix)
        ));
    }

    private void sendNotFound() throws IOException {
        res.setStatus(HttpServletResponse.SC_NOT_FOUND);
        new Message("Invalid lookup path.", "404",
                "Expected /category/{id}/books or /books/{id}/categories.")
                .toJSON(res.getOutputStream());
    }

    private void sendMethodNotAllowed() throws IOException {
        res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        new Message("Only GET method supported.", "405",
                "Use GET for retrieving books/categories.")
                .toJSON(res.getOutputStream());
    }
}
