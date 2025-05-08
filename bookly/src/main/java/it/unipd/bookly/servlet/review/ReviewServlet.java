package it.unipd.bookly.servlet.review;

import it.unipd.bookly.LogContext;
import it.unipd.bookly.Resource.Review;
import it.unipd.bookly.Resource.User;
import it.unipd.bookly.dao.review.InsertReviewDAO;
import it.unipd.bookly.dao.review.UpdateReviewLikesDAO;
import it.unipd.bookly.dao.review.UpdateReviewDisLikesDAO;
import it.unipd.bookly.servlet.AbstractDatabaseServlet;
import it.unipd.bookly.utilities.ServletUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Timestamp;
import java.time.Instant;

@WebServlet(name = "ReviewServlet", value = "/review/*")
@MultipartConfig
public class ReviewServlet extends AbstractDatabaseServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setResource(req.getRequestURI());
        LogContext.setAction("ReviewServlet");

        try {
            HttpSession session = req.getSession(false);
            if (session == null || session.getAttribute("user") == null) {
                resp.sendRedirect(req.getContextPath() + "/user/login");
                return;
            }

            String path = req.getPathInfo();
            if (path == null || path.isBlank()) {
                ServletUtils.redirectToErrorPage(req, resp, "Missing review action path.");
                return;
            }

            User user = (User) session.getAttribute("user");

            switch (path) {
                case "/submit" -> handleSubmitReview(req, resp, user);
                case "/like" -> handleLikeDislike(req, resp, true);
                case "/dislike" -> handleLikeDislike(req, resp, false);
                default -> ServletUtils.redirectToErrorPage(req, resp, "Unsupported review action: " + path);
            }

        } catch (Exception e) {
            LOGGER.error("ReviewServlet error: {}", e.getMessage(), e);
            ServletUtils.redirectToErrorPage(req, resp, "ReviewServlet error: " + e.getMessage());
        } finally {
            LogContext.removeAction();
            LogContext.removeResource();
        }
    }

    private void handleSubmitReview(HttpServletRequest req, HttpServletResponse resp, User user) throws Exception {
        int bookId = parseInt(req.getParameter("bookId"));
        int rating = parseInt(req.getParameter("rating"));
        String reviewText = req.getParameter("reviewText");

        if (bookId <= 0 || rating < 1 || rating > 5 || reviewText == null || reviewText.trim().isEmpty()) {
            ServletUtils.redirectToErrorPage(req, resp, "Invalid review input.");
            return;
        }

        Review review = new Review();
        review.setbookId(bookId);
        review.setUserId(user.getUserId());
        review.setRating(rating);
        review.setReviewText(reviewText.trim());
        review.setReviewDate(Timestamp.from(Instant.now()));

        try (Connection con = getConnection()) {
            new InsertReviewDAO(con, review).access();
        }

        resp.sendRedirect(req.getContextPath() + "/book/" + bookId);
    }

    private void handleLikeDislike(HttpServletRequest req, HttpServletResponse resp, boolean isLike) throws Exception {
        int reviewId = parseInt(req.getParameter("reviewId"));
        if (reviewId <= 0) {
            ServletUtils.redirectToErrorPage(req, resp, "Invalid review ID.");
            return;
        }

        try (Connection con = getConnection()) {
            if (isLike) {
                new UpdateReviewLikesDAO(con, reviewId, 1).access();
            } else {
                new UpdateReviewDisLikesDAO(con, reviewId, 1).access();
            }
        }

        // Optionally pass back the bookId to redirect correctly
        String bookId = req.getParameter("bookId");
        if (bookId != null) {
            resp.sendRedirect(req.getContextPath() + "/book/" + bookId);
        } else {
            resp.sendRedirect(req.getContextPath() + "/book");
        }
    }

    private int parseInt(String param) {
        try {
            return Integer.parseInt(param);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
