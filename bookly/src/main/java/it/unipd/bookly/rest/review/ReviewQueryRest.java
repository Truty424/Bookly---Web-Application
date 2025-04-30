package it.unipd.bookly.rest.review;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.unipd.bookly.Resource.Message;
import it.unipd.bookly.Resource.Review;
import it.unipd.bookly.dao.review.*;
import it.unipd.bookly.rest.AbstractRestResource;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handles review querying:
 * - GET /api/review/book/{bookId}
 * - GET /api/review/user/{userId}
 * - GET /api/review/stats/book/{bookId}
 */
public class ReviewQueryRest extends AbstractRestResource {

    private final ObjectMapper mapper = new ObjectMapper();

    public ReviewQueryRest(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super("review-query", req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        final String method = req.getMethod();
        final String path = req.getRequestURI();

        try {
            if ("GET".equals(method)) {
                handleGet(path);
            } else {
                respondMethodNotAllowed("Only GET is supported for review queries.");
            }
        } catch (Exception e) {
            LOGGER.error("ReviewQueryRest error", e);
            respondServerError("Internal error while querying reviews: " + e.getMessage());
        }
    }

    private void handleGet(String path) throws Exception {
        if (path.matches(".*/review/book/\\d+$")) {
            int bookId = extractIdFromPath(path);
            List<Review> reviews = new GetReviewsByBookDAO(con, bookId).access().getOutputParam();
            respondJson(reviews);
        } else if (path.matches(".*/review/user/\\d+$")) {
            int userId = extractIdFromPath(path);
            List<Review> reviews = new GetReviewsByUserDAO(con, userId).access().getOutputParam();
            respondJson(reviews);
        } else if (path.matches(".*/review/stats/book/\\d+$")) {
            int bookId = extractIdFromPath(path);
            handleGetReviewStats(bookId);
        } else {
            respondNotFound("Invalid review query path.");
        }
    }

    private void handleGetReviewStats(int bookId) throws Exception {
        double averageRating = new GetAvgRatingForBookDAO(con, bookId).access().getOutputParam();
        int reviewCount = new CountReviewsForBookDAO(con, bookId).access().getOutputParam();

        Map<String, Object> stats = new HashMap<>();
        stats.put("bookId", bookId);
        stats.put("averageRating", averageRating);
        stats.put("reviewCount", reviewCount);

        respondJson(stats);
    }

    private int extractIdFromPath(String path) {
        return Integer.parseInt(path.substring(path.lastIndexOf("/") + 1));
    }

    private void respondJson(Object obj) throws IOException {
        res.setStatus(HttpServletResponse.SC_OK);
        res.setContentType("application/json;charset=UTF-8");
        mapper.writeValue(res.getOutputStream(), obj);
    }

    private void respondNotFound(String detail) throws IOException {
        res.setStatus(HttpServletResponse.SC_NOT_FOUND);
        new Message("Not Found", "404", detail).toJSON(res.getOutputStream());
    }

    private void respondMethodNotAllowed(String detail) throws IOException {
        res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        new Message("Method Not Allowed", "405", detail).toJSON(res.getOutputStream());
    }

    private void respondServerError(String detail) throws IOException {
        res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        new Message("Server Error", "500", detail).toJSON(res.getOutputStream());
    }
}
