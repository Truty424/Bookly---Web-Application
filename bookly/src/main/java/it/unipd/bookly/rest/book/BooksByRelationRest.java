package it.unipd.bookly.rest.book;

import it.unipd.bookly.Resource.Book;
import it.unipd.bookly.Resource.Message;
import it.unipd.bookly.dao.book.GetBooksByAuthorIdDAO;
import it.unipd.bookly.dao.book.GetBooksByCategoryIdDAO;
import it.unipd.bookly.dao.book.GetBooksByPublisherIdDAO;
import it.unipd.bookly.rest.AbstractRestResource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

/**
 * Handles requests to get books by author, category, or publisher.
 * 
 * Supported endpoints:
 * - /api/books/author?id=1
 * - /api/books/category?id=2
 * - /api/books/publisher?id=3
 */
public class BooksByRelationRest extends AbstractRestResource {

    public BooksByRelationRest(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super("books-by-relation", req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        String path = req.getRequestURI();  // e.g. /api/books/author?id=1
        String idParam = req.getParameter("id");
        Message message = null;

        if (idParam == null || idParam.isBlank()) {
            message = new Message("Missing relation ID.", "E400", "Please provide an 'id' query parameter.");
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            message.toJSON(res.getOutputStream());
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idParam);
        } catch (NumberFormatException e) {
            message = new Message("Invalid ID format.", "E401", "The provided ID must be an integer.");
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            message.toJSON(res.getOutputStream());
            return;
        }

        try {
            List<Book> books = null;

            if (path.contains("/author")) {
                books = new GetBooksByAuthorIdDAO(con, id).access().getOutputParam();
            } else if (path.contains("/category")) {
                books = new GetBooksByCategoryIdDAO(con, id).access().getOutputParam();
            } else if (path.contains("/publisher")) {
                books = new GetBooksByPublisherIdDAO(con, id).access().getOutputParam();
            } else {
                message = new Message("Invalid path.", "E404", "Path must include /author, /category, or /publisher.");
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                message.toJSON(res.getOutputStream());
                return;
            }

            if (books == null || books.isEmpty()) {
                message = new Message("No books found for the given ID.", "E404");
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                message.toJSON(res.getOutputStream());
                return;
            }

            res.setContentType("application/json;charset=UTF-8");
            res.setStatus(HttpServletResponse.SC_OK);
            for (Book book : books) {
                book.toJSON(res.getOutputStream());
            }

        } catch (Exception e) {
            LOGGER.error("Error retrieving books by relation ID: {}", id, e);
            message = new Message("Internal server error", "E500", e.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            message.toJSON(res.getOutputStream());
        }
    }
}
