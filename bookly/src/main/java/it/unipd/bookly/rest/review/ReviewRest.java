package it.unipd.bookly.rest.review;

import it.unipd.bookly.Resource.Message;
import it.unipd.bookly.Resource.Review;
import it.unipd.bookly.dao.review.InsertReviewDAO;
import it.unipd.bookly.dao.review.DeleteReviewDAO;
import it.unipd.bookly.dao.review.GetReviewByIdDAO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import it.unipd.bookly.rest.AbstractRestResource;

import java.io.IOException;
import java.sql.Connection;

public class ReviewRest extends AbstractRestResource {

    public ReviewRest(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super("review", req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        String method = req.getMethod();
        String path = req.getRequestURI();
        Message message = null;
        try {
            if ("POST".equals(method)) {
                Review review = Review.fromJSON(req.getInputStream());
                int reviewId = new InsertReviewDAO(con, review).access().getOutputParam();
                res.setStatus(HttpServletResponse.SC_CREATED);
                res.getWriter().write("{\"review_id\":" + reviewId + "}");
            } else if ("DELETE".equals(method) && path.matches(".*/review/\\d+")) {
                int reviewId = Integer.parseInt(path.substring(path.lastIndexOf("/") + 1));
                new DeleteReviewDAO(con, reviewId).access();
                res.setStatus(HttpServletResponse.SC_OK);
                message = new Message("Review deleted.", "200");
                message.toJSON(res.getOutputStream());
            } else if ("GET".equals(method) && path.matches(".*/review/\\d+")) {
                int reviewId = Integer.parseInt(path.substring(path.lastIndexOf("/") + 1));
                Review review = new GetReviewByIdDAO(con, reviewId).access().getOutputParam();
                if (review != null) {
                    res.setContentType("application/json");
                    review.toJSON(res.getOutputStream());
                } else {
                    res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    message = new Message("Review not found.", "404");
                    message.toJSON(res.getOutputStream());
                }
            } else {
                res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                message = new Message("Unsupported operation.", "405");
                message.toJSON(res.getOutputStream());
            }
        } catch (Exception e) {
            LOGGER.error("ReviewRest error", e);
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            new Message("Server error", "500", e.getMessage()).toJSON(res.getOutputStream());
        }
    }
}
