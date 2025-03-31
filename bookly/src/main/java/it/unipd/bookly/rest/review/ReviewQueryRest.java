package it.unipd.bookly.rest.review;

import it.unipd.bookly.Resource.Message;
import it.unipd.bookly.Resource.Review;
import it.unipd.bookly.dao.review.GetAllReviewsDAO;
import it.unipd.bookly.dao.review.GetReviewsByBookDAO;
import it.unipd.bookly.dao.review.GetReviewsByUserDAO;
import it.unipd.bookly.dao.review.GetTopReviewsForBookDAO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import it.unipd.bookly.rest.AbstractRestResource;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

public class ReviewQueryRest extends AbstractRestResource {

    public ReviewQueryRest(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super("review-query", req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        String path = req.getRequestURI();

        try {
            List<Review> reviews = null;

            if (path.matches(".*/review/book/\\d+")) {
                int bookId = Integer.parseInt(path.substring(path.lastIndexOf("/") + 1));
                reviews = new GetReviewsByBookDAO(con, bookId).access().getOutputParam();
            } else if (path.matches(".*/review/user/\\d+")) {
                int userId = Integer.parseInt(path.substring(path.lastIndexOf("/") + 1));
                reviews = new GetReviewsByUserDAO(con, userId).access().getOutputParam();
            } else if (path.matches(".*/review/top/book/\\d+")) {
                int bookId = Integer.parseInt(path.substring(path.lastIndexOf("/") + 1));
                reviews = new GetTopReviewsForBookDAO(con, bookId).access().getOutputParam();
            } else if (path.endsWith("/reviews")) {
                reviews = new GetAllReviewsDAO(con).access().getOutputParam();
            } else {
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                new Message("Review query path not found", "404").toJSON(res.getOutputStream());
                return;
            }

            res.setContentType("application/json");
            for (Review review : reviews) {
                review.toJSON(res.getOutputStream()); // or serialize the list together if you have a helper
            }

        } catch (Exception e) {
            LOGGER.error("Error querying reviews", e);
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            new Message("Internal error while querying reviews", "500", e.getMessage()).toJSON(res.getOutputStream());
        }
    }
}
