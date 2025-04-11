package it.unipd.bookly.rest.book;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.unipd.bookly.Resource.Book;
import it.unipd.bookly.Resource.Message;
import it.unipd.bookly.dao.book.GetTopRatedBooksDAO;
import it.unipd.bookly.rest.AbstractRestResource;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;

/**
 * REST endpoint to retrieve top-rated books.
 * Supported: GET /api/book/top-rated?minRating=4.5
 */
public class TopRatedBooksRest extends AbstractRestResource {

    public TopRatedBooksRest(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super("top-rated-books", req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        final String method = req.getMethod();

        switch (method) {
            case "GET" -> handleGetTopRatedBooks();
            default -> sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED,
                    "Method not allowed", "E405", "Only GET is supported for top-rated books.");
        }
    }

    private void handleGetTopRatedBooks() throws IOException {
        final String minRatingParam = req.getParameter("minRating");

        if (minRatingParam == null || minRatingParam.isBlank()) {
            sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing 'minRating'", "E400",
                    "You must provide a 'minRating' query parameter.");
            return;
        }

        double minRating;
        try {
            minRating = Double.parseDouble(minRatingParam);
        } catch (NumberFormatException e) {
            sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid rating format", "E401",
                    "'minRating' must be a valid number.");
            return;
        }

        try {
            List<Book> books = new GetTopRatedBooksDAO(con, minRating).access().getOutputParam();

            if (books == null || books.isEmpty()) {
                sendError(HttpServletResponse.SC_NOT_FOUND, "No top-rated books", "E404",
                        "No books found with rating ≥ " + minRating);
                return;
            }

            res.setContentType("application/json;charset=UTF-8");
            res.setStatus(HttpServletResponse.SC_OK);
            new ObjectMapper().writeValue(res.getOutputStream(), books);

        } catch (Exception e) {
            LOGGER.error("Error fetching top-rated books (minRating={}): {}", minRating, e.getMessage());
            sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server error", "E500", e.getMessage());
        }
    }

    private void sendError(int status, String title, String code, String detail) throws IOException {
        res.setStatus(status);
        new Message(title, code, detail).toJSON(res.getOutputStream());
    }
}
