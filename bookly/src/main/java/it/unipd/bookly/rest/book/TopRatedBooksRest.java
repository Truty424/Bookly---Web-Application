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
 * Supported: GET /api/books/top-rated?minRating=4.5
 */
public class TopRatedBooksRest extends AbstractRestResource {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public TopRatedBooksRest(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super("top-rated-books", req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        res.setContentType("application/json;charset=UTF-8");  // Always respond as JSON

        if (!"GET".equalsIgnoreCase(req.getMethod())) {
            sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED,
                    "Method not allowed", "E405",
                    "Only GET is supported for retrieving top-rated books.");
            return;
        }

        handleGetTopRatedBooks();
    }

    private void handleGetTopRatedBooks() throws IOException {
        final String minRatingParam = req.getParameter("minRating");
        double minRating = 4.0;  // Default value if not provided (optional)

        if (minRatingParam != null && !minRatingParam.isBlank()) {
            try {
                minRating = Double.parseDouble(minRatingParam);
            } catch (NumberFormatException e) {
                sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid 'minRating' format", "E401",
                        "'minRating' must be a valid number.");
                return;
            }
        }

        try {
            List<Book> books = new GetTopRatedBooksDAO(con, minRating).access().getOutputParam();

            if (books == null || books.isEmpty()) {
                sendError(HttpServletResponse.SC_NOT_FOUND, "No top-rated books found", "E404",
                        "No books found with rating ≥ " + minRating);
                return;
            }

            res.setStatus(HttpServletResponse.SC_OK);
            MAPPER.writeValue(res.getOutputStream(), books);

            LOGGER.info("{} top-rated book(s) retrieved with minRating ≥ {}.", books.size(), minRating);

        } catch (Exception e) {
            LOGGER.error("Error fetching top-rated books (minRating={}): {}", minRating, e.getMessage(), e);
            sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server error", "E500",
                    "An error occurred while fetching top-rated books: " + e.getMessage());
        }
    }

    private void sendError(int status, String title, String code, String detail) throws IOException {
        res.setStatus(status);
        MAPPER.writeValue(res.getOutputStream(), new Message(title, code, detail));
    }
}
