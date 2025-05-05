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

/**
 * Handles:
 * - POST /api/review → insert a review
 * - GET /api/review/{id} → get review by ID
 * - DELETE /api/review/{id} → delete review by ID
 * - PUT /api/review/{id}/text → update review text and rating
 * - PUT /api/review/{id}/likes → update likes and dislikes
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
                case "POST" -> handleInsertReview();
                case "GET" -> {
                    if (path.matches(".*/review/\\d+$")) {
                        handleGetReviewById(path);
                    } else {
                        respondMethodNotAllowed("Invalid GET path for review.");
                    }
                }
                case "PUT" -> {
                    if (path.matches(".*/review/\\d+/text$")) {
                        handleUpdateTextAndRating(path);
                    } else if (path.matches(".*/review/\\d+/likes$")) {
                        handleUpdateLikesDislikes(path);
                    } else {
                        respondMethodNotAllowed("Invalid PUT path for review.");
                    }
                }
                default -> respondMethodNotAllowed("Only POST, GET, PUT, DELETE supported.");
            }
        } catch (Exception e) {
            LOGGER.error("ReviewRest error", e);
            respondServerError("Server error: " + e.getMessage());
        }
    }

    private void handleInsertReview() throws Exception {
        Review review = mapper.readValue(req.getInputStream(), Review.class);
        boolean inserted = new InsertReviewDAO(con, review).access().getOutputParam();

        if (inserted) {
            res.setStatus(HttpServletResponse.SC_CREATED);
            new Message("Review created successfully.", "201", "Review added.").toJSON(res.getOutputStream());
        } else {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            new Message("Failed to create review.", "400", "Insertion returned false.").toJSON(res.getOutputStream());
        }
    }

    private void handleGetReviewById(String path) throws Exception {
        int reviewId = extractIdFromPath(path);
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


    private void handleUpdateTextAndRating(String path) throws Exception {
        int reviewId = extractIdFromPath(path);
        Review updated = mapper.readValue(req.getInputStream(), Review.class);

        boolean result = new UpdateCommentAndRatingDAO(con, reviewId, updated.getReviewText(), updated.getRating())
                .access().getOutputParam();

        if (result) {
            res.setStatus(HttpServletResponse.SC_OK);
            new Message("Review updated", "200", "Text and rating updated for ID " + reviewId)
                    .toJSON(res.getOutputStream());
        } else {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            new Message("Update failed", "404", "No review found to update.")
                    .toJSON(res.getOutputStream());
        }
    }

    private void handleUpdateLikesDislikes(String path) throws Exception {
        int reviewId = extractIdFromPath(path);
        Review update = mapper.readValue(req.getInputStream(), Review.class);

        boolean result = new UpdateReviewLikesDislikesDAO(con, reviewId, update.getNumberOfLikes(), update.getNumberOfDislikes())
                .access().getOutputParam();

        if (result) {
            res.setStatus(HttpServletResponse.SC_OK);
            new Message("Review engagement updated", "200", "Likes/dislikes updated.").toJSON(res.getOutputStream());
        } else {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            new Message("Review not found", "404", "Update failed - ID not found.").toJSON(res.getOutputStream());
        }
    }

    private int extractIdFromPath(String path) {
        String[] parts = path.split("/");
        return Integer.parseInt(parts[parts.length - 2].equals("likes") || parts[parts.length - 2].equals("text")
                ? parts[parts.length - 3]
                : parts[parts.length - 1]);
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
