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
 * Handles GET requests to retrieve top-rated books.
 * Endpoint: /api/book/top-rated?minRating=4.5
 */
public class TopRatedBooksRest extends AbstractRestResource {

    public TopRatedBooksRest(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super("top-rated-books", req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        final String minRatingParam = req.getParameter("minRating");
        Message message = null;
        double minRating;

        if (minRatingParam == null || minRatingParam.isBlank()) {
            message = new Message("Missing 'minRating' parameter.", "E400", "You must provide a minimum rating.");
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            message.toJSON(res.getOutputStream());
            return;
        }

        try {
            minRating = Double.parseDouble(minRatingParam);
        } catch (NumberFormatException e) {
            message = new Message("Invalid rating format.", "E401", "'minRating' must be a valid number.");
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            message.toJSON(res.getOutputStream());
            return;
        }

        try {
            List<Book> books = new GetTopRatedBooksDAO(con, minRating).access().getOutputParam();

            if (books == null || books.isEmpty()) {
                message = new Message("No top-rated books found.", "E404", "Try lowering the rating threshold.");
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                message.toJSON(res.getOutputStream());
                return;
            }

            res.setContentType("application/json;charset=UTF-8");
            res.setStatus(HttpServletResponse.SC_OK);

            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(res.getOutputStream(), books);

        } catch (Exception e) {
            LOGGER.error("Error while retrieving top-rated books (minRating={}): {}", minRating, e.getMessage());
            message = new Message("Internal server error while retrieving top-rated books.", "E500", e.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            message.toJSON(res.getOutputStream());
        }
    }
}
