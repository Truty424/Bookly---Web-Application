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
                        handleUpdateLikes(path);
                    } else if (path.matches(".*/review/\\d+/dislikes$")) {
                        handleUpdateDislikes(path);
                    } else {
                        respondMethodNotAllowed("Invalid PUT path for review.");
                    }
                }
                default -> respondMethodNotAllowed("Only POST, GET, PUT supported.");
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
            new Message("Review created successfully", "201", "Review inserted.")
                    .toJSON(res.getOutputStream());
        } else {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            new Message("Failed to insert review", "400", "InsertReviewDAO returned false.")
                    .toJSON(res.getOutputStream());
        }
    }

    private void handleGetReviewById(String path) throws Exception {
        int reviewId = extractIdFromPath(path);
        Review review = new GetReviewByIdDAO(con, reviewId).access().getOutputParam();

        if (review != null) {
            res.setStatus(HttpServletResponse.SC_OK);
            res.setContentType("application/json;charset=UTF-8");
            mapper.writeValue(res.getOutputStream(), review);
        } else {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            new Message("Review not found", "404", "ID: " + reviewId)
                    .toJSON(res.getOutputStream());
        }
    }

    private void handleUpdateTextAndRating(String path) throws Exception {
        int reviewId = extractIdFromPath(path);
        Review updated = mapper.readValue(req.getInputStream(), Review.class);

        boolean success = new UpdateCommentAndRatingDAO(con, reviewId, updated.getReviewText(), updated.getRating())
                .access().getOutputParam();

        if (success) {
            res.setStatus(HttpServletResponse.SC_OK);
            new Message("Review updated", "200", "Text and rating updated.")
                    .toJSON(res.getOutputStream());
        } else {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            new Message("Update failed", "404", "No matching review to update.")
                    .toJSON(res.getOutputStream());
        }
    }

    private void handleUpdateLikes(String path) throws Exception {
        int reviewId = extractIdFromPath(path);
        Review update = mapper.readValue(req.getInputStream(), Review.class);

        boolean success = new UpdateReviewLikesDAO(con, reviewId, update.getNumberOfLikes())
                .access().getOutputParam();

        if (success) {
            res.setStatus(HttpServletResponse.SC_OK);
            new Message("Likes updated", "200", "Likes successfully updated.")
                    .toJSON(res.getOutputStream());
        } else {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            new Message("Review not found", "404", "No matching review to update likes.")
                    .toJSON(res.getOutputStream());
        }
    }

    private void handleUpdateDislikes(String path) throws Exception {
        int reviewId = extractIdFromPath(path);
        Review update = mapper.readValue(req.getInputStream(), Review.class);

        boolean success = new UpdateReviewDisLikesDAO(con, reviewId, update.getNumberOfDislikes())
                .access().getOutputParam();

        if (success) {
            res.setStatus(HttpServletResponse.SC_OK);
            new Message("Dislikes updated", "200", "Dislikes successfully updated.")
                    .toJSON(res.getOutputStream());
        } else {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            new Message("Review not found", "404", "No matching review to update dislikes.")
                    .toJSON(res.getOutputStream());
        }
    }

    private int extractIdFromPath(String path) {
        String[] parts = path.split("/");
        if (parts[parts.length - 2].equals("likes") || parts[parts.length - 2].equals("text") || parts[parts.length - 2].equals("dislikes")) {
            return Integer.parseInt(parts[parts.length - 3]);
        }
        return Integer.parseInt(parts[parts.length - 1]);
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
