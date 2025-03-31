package it.unipd.bookly.rest;

import it.unipd.bookly.dao.book.GetBookByIdDAO;
import it.unipd.bookly.Resource.Book;
import it.unipd.bookly.Resource.Message;
import it.unipd.bookly.rest.AbstractRestResource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;

/**
 * Handles GET requests to retrieve book details by ID.
 * Endpoint: /api/book?id=123
 */
public class GetBookRest extends AbstractRestResource {

    public GetBookRest(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super("get-book", req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        // Parse book ID from query parameter
        final String bookIdParam = req.getParameter("id");

        if (bookIdParam == null || bookIdParam.isBlank()) {
            Message m = new Message("Missing book ID.", "E400", "You must provide a book ID as a query parameter.");
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            m.toJSON(res.getOutputStream());
            return;
        }

        int bookId;
        try {
            bookId = Integer.parseInt(bookIdParam);
        } catch (NumberFormatException ex) {
            Message m = new Message("Invalid book ID format.", "E401", "Book ID must be an integer.");
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            m.toJSON(res.getOutputStream());
            return;
        }

        try {
            Book book = new GetBookByIdDAO(con, bookId).access().getOutputParam();

            if (book == null) {
                Message m = new Message("Book not found.", "E404", "No book found with the specified ID.");
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                m.toJSON(res.getOutputStream());
                return;
            }

            // Set content type and write book JSON
            res.setContentType("application/json;charset=UTF-8");
            book.toJSON(res.getOutputStream());

        } catch (Exception e) {
            LOGGER.error("Error while retrieving book with ID: {}", bookId, e);
            Message m = new Message("Internal server error while retrieving book.", "E500", e.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        }
    }
}
