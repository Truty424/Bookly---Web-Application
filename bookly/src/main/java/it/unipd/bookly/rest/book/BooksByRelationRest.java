package it.unipd.bookly.rest.book;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.unipd.bookly.Resource.Book;
import it.unipd.bookly.Resource.Message;
import it.unipd.bookly.dao.book.GetBooksByAuthorIdDAO;
import it.unipd.bookly.dao.book.GetBooksByCategoryIdDAO;
import it.unipd.bookly.dao.book.GetBooksByPublisherIdDAO;
import it.unipd.bookly.rest.AbstractRestResource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Handles book retrieval by relationship:
 * 
 * - GET /api/books/author?id={authorId}     → Books by a specific author
 * - GET /api/books/category?id={categoryId} → Books in a specific category
 * - GET /api/books/publisher?id={publisherId} → Books by a specific publisher
 */
public class BooksByRelationRest extends AbstractRestResource {

    public BooksByRelationRest(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super("books-by-relation", req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        final String path = req.getRequestURI(); // e.g. /api/books/author?id=1
        final String idParam = req.getParameter("id");

        if (idParam == null || idParam.isBlank()) {
            sendMessage(HttpServletResponse.SC_BAD_REQUEST, "Missing 'id' parameter.", "E400", "Query parameter 'id' is required.");
            return;
        }

        try {
            int id = Integer.parseInt(idParam);
            String relationType = extractRelationType(path);

            List<Book> books;
            switch (relationType) {
                case "author" ->
                    books = new GetBooksByAuthorIdDAO(con, id).access().getOutputParam();
                case "category" ->
                    books = new GetBooksByCategoryIdDAO(con, id).access().getOutputParam();
                case "publisher" ->
                    books = new GetBooksByPublisherIdDAO(con, id).access().getOutputParam();
                default -> {
                    sendMessage(HttpServletResponse.SC_NOT_FOUND, "Invalid endpoint.", "E404",
                            "Path must contain /author, /category, or /publisher.");
                    return;
                }
            }

            if (books == null || books.isEmpty()) {
                sendMessage(HttpServletResponse.SC_NOT_FOUND, "No books found", "E404",
                        "No books found for the given ID: " + id);
                return;
            }

            res.setContentType("application/json;charset=UTF-8");
            res.setStatus(HttpServletResponse.SC_OK);
            new ObjectMapper().writeValue(res.getOutputStream(), books);

        } catch (NumberFormatException e) {
            sendMessage(HttpServletResponse.SC_BAD_REQUEST, "Invalid 'id' format.", "E401", "'id' must be a valid integer.");
        } catch (Exception e) {
            LOGGER.error("BooksByRelationRest error", e);
            sendMessage(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server error while retrieving books.", "E500", e.getMessage());
        }
    }

    private String extractRelationType(String path) {
        if (path.contains("/author")) {
            return "author";
        }
        if (path.contains("/category")) {
            return "category";
        }
        if (path.contains("/publisher")) {
            return "publisher";
        }
        return "unknown";
    }

    private void sendMessage(int status, String title, String code, String detail) throws IOException {
        res.setStatus(status);
        new Message(title, code, detail).toJSON(res.getOutputStream());
    }
}
