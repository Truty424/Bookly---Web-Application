package it.unipd.bookly.rest.review;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.unipd.bookly.Resource.Message;
import it.unipd.bookly.Resource.Review;
import it.unipd.bookly.dao.review.GetAllReviewsDAO;
import it.unipd.bookly.dao.review.GetReviewsByBookDAO;
import it.unipd.bookly.dao.review.GetReviewsByUserDAO;
import it.unipd.bookly.dao.review.GetTopReviewsForBookDAO;
import it.unipd.bookly.rest.AbstractRestResource;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

/**
 * Handles: - GET /api/review/book/{bookId} - GET /api/review/user/{userId} -
 * GET /api/review/top/book/{bookId} - GET /api/reviews
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
            switch (method) {
                case "GET" ->
                    handleGet(path);
                default ->
                    respondMethodNotAllowed("Only GET is supported for review queries.");
            }
        } catch (Exception e) {
            LOGGER.error("ReviewQueryRest error", e);
            respondServerError("Internal error while querying reviews: " + e.getMessage());
        }
    }

    private void handleGet(String path) throws Exception {
        if (path.matches(".*/review/book/\\d+$")) {
            handleGetReviewsByBook(path);
        } else if (path.matches(".*/review/user/\\d+$")) {
            handleGetReviewsByUser(path);
        } else if (path.matches(".*/review/top/book/\\d+$")) {
            handleGetTopReviewsForBook(path);
        } else if (path.endsWith("/reviews")) {
            handleGetAllReviews();
        } else {
            respondNotFound("Invalid review query path.");
        }
    }

    private void handleGetReviewsByBook(String path) throws Exception {
        int bookId = extractIdFromPath(path);
        List<Review> reviews = new GetReviewsByBookDAO(con, bookId).access().getOutputParam();
        writeJsonResponse(reviews);
    }

    private void handleGetReviewsByUser(String path) throws Exception {
        int userId = extractIdFromPath(path);
        List<Review> reviews = new GetReviewsByUserDAO(con, userId).access().getOutputParam();
        writeJsonResponse(reviews);
    }

    private void handleGetTopReviewsForBook(String path) throws Exception {
        int bookId = extractIdFromPath(path);
        int limit = 5;
        List<Review> reviews = new GetTopReviewsForBookDAO(con, bookId, limit).access().getOutputParam();
        writeJsonResponse(reviews);
    }

    private void handleGetAllReviews() throws Exception {
        List<Review> reviews = new GetAllReviewsDAO(con).access().getOutputParam();
        writeJsonResponse(reviews);
    }

    private int extractIdFromPath(String path) {
        return Integer.parseInt(path.substring(path.lastIndexOf("/") + 1));
    }

    private void writeJsonResponse(List<Review> reviews) throws IOException {
        res.setStatus(HttpServletResponse.SC_OK);
        res.setContentType("application/json;charset=UTF-8");
        mapper.writeValue(res.getOutputStream(), reviews);
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
