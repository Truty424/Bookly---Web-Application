package it.unipd.bookly.rest.review;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.unipd.bookly.Resource.Message;
import it.unipd.bookly.Resource.Review;
import it.unipd.bookly.dao.review.DeleteReviewDAO;
import it.unipd.bookly.dao.review.GetReviewByIdDAO;
import it.unipd.bookly.dao.review.InsertReviewDAO;
import it.unipd.bookly.rest.AbstractRestResource;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;


/**
 * Handles: - POST /api/review → insert a review - GET /api/review/{id} → get
 * review by ID - DELETE /api/review/{id} → delete review by ID
 */
public class ReviewRest extends AbstractRestResource {

    private final ObjectMapper mapper = new ObjectMapper();

    public ReviewRest(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super("review", req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        final String method = req.getMethod();
        final String path = req.getRequestURI();

        try {
            switch (method) {
                case "POST" ->
                    handleInsertReview();
                case "GET" -> {
                    if (path.matches(".*/review/\\d+$")) {
                        handleGetReviewById(path);
                    } else {
                        respondMethodNotAllowed("Invalid GET path for review.");
                    }
                }
                case "DELETE" -> {
                    if (path.matches(".*/review/\\d+$")) {
                        handleDeleteReview(path);
                    } else {
                        respondMethodNotAllowed("Invalid DELETE path for review.");
                    }
                }
                default ->
                    respondMethodNotAllowed("Only POST, GET, DELETE supported for /review endpoint.");
            }
        } catch (Exception e) {
            LOGGER.error("ReviewRest error", e);
            respondServerError("Server error: " + e.getMessage());
        }
    }

    private void handleInsertReview() throws Exception {
        Review review = mapper.readValue(req.getInputStream(), Review.class);
        boolean inserted = new InsertReviewDAO(con, review).access().getOutputParam();
        res.setStatus(HttpServletResponse.SC_CREATED);
        mapper.writeValue(res.getOutputStream(), inserted);
    }

    private void handleGetReviewById(String path) throws Exception {
        int reviewId = Integer.parseInt(path.substring(path.lastIndexOf("/") + 1));
        Review review = new GetReviewByIdDAO(con, reviewId).access().getOutputParam();

        if (review == null) {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            new Message("Review not found.", "404", "No review with ID " + reviewId).toJSON(res.getOutputStream());
        } else {
            res.setStatus(HttpServletResponse.SC_OK);
            res.setContentType("application/json;charset=UTF-8");
            mapper.writeValue(res.getOutputStream(), review);
        }
    }

    private void handleDeleteReview(String path) throws Exception {
        int reviewId = extractIdFromPath(path);
        new DeleteReviewDAO(con, reviewId).access();

        res.setStatus(HttpServletResponse.SC_OK);
        new Message("Review deleted", "200", "ID: " + reviewId).toJSON(res.getOutputStream());
    }

    private int extractIdFromPath(String path) {
        return Integer.parseInt(path.substring(path.lastIndexOf("/") + 1));
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
