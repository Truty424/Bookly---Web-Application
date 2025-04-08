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
 * REST endpoint for retrieving books by author, category, or publisher.
 *
 * Supported endpoints: - /api/books/author?id=1 - /api/books/category?id=2 -
 * /api/books/publisher?id=3
 */
public class BooksByRelationRest extends AbstractRestResource {

    public BooksByRelationRest(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super("books-by-relation", req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        String path = req.getRequestURI();  // e.g., /api/books/author?id=1
        String idParam = req.getParameter("id");
        Message message;

        if (idParam == null || idParam.isBlank()) {
            message = new Message("Missing 'id' parameter.", "E400", "Query parameter 'id' is required.");
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            message.toJSON(res.getOutputStream());
            return;
        }

        try {
            int id = Integer.parseInt(idParam);
            List<Book> books = null;

            if (path.contains("/author")) {
                books = new GetBooksByAuthorIdDAO(con, id).access().getOutputParam();
            } else if (path.contains("/category")) {
                books = new GetBooksByCategoryIdDAO(con, id).access().getOutputParam();
            } else if (path.contains("/publisher")) {
                books = new GetBooksByPublisherIdDAO(con, id).access().getOutputParam();
            } else {
                message = new Message("Invalid endpoint.", "E404", "Path must contain /author, /category, or /publisher.");
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                message.toJSON(res.getOutputStream());
                return;
            }

            if (books == null || books.isEmpty()) {
                message = new Message("No books found", "E404", "No books found for the given ID: " + id);
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                message.toJSON(res.getOutputStream());
                return;
            }

            // Respond with book list in JSON
            res.setContentType("application/json;charset=UTF-8");
            res.setStatus(HttpServletResponse.SC_OK);
            new ObjectMapper().writeValue(res.getOutputStream(), books);

        } catch (NumberFormatException e) {
            message = new Message("Invalid 'id' format.", "E401", "'id' must be a valid integer.");
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            message.toJSON(res.getOutputStream());
        } catch (Exception e) {
            LOGGER.error("Error fetching books by relation", e);
            message = new Message("Server error while retrieving books.", "E500", e.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            message.toJSON(res.getOutputStream());
        }
    }
}
