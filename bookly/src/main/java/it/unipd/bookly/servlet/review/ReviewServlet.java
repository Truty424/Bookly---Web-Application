package it.unipd.bookly.servlet.review;

import it.unipd.bookly.LogContext;
import it.unipd.bookly.Resource.Review;
import it.unipd.bookly.Resource.User;
import it.unipd.bookly.dao.review.*;
import it.unipd.bookly.servlet.AbstractDatabaseServlet;
import it.unipd.bookly.utilities.ServletUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;

@WebServlet(name = "ReviewServlet", value = "/review/*")
public class ReviewServlet extends AbstractDatabaseServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setResource(req.getRequestURI());
        LogContext.setAction("ReviewServlet");

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            res.sendRedirect(req.getContextPath() + "/user/login");
            return;
        }

        String path = req.getPathInfo();
        User user = (User) session.getAttribute("user");

        try (Connection con = getConnection()) {
            if (path == null || path.equals("/submit")) {
                submitReview(req, res, con, user);
            } else if ("/like".equals(path)) {
                handleThumb(req, res, con, true);
            } else if ("/dislike".equals(path)) {
                handleThumb(req, res, con, false);
            } else {
                ServletUtils.redirectToErrorPage(req, res, "Unknown review action: " + path);
            }
        } catch (Exception e) {
            LOGGER.error("ReviewServlet error: {}", e.getMessage(), e);
            ServletUtils.redirectToErrorPage(req, res, "ReviewServlet error: " + e.getMessage());
        } finally {
            LogContext.removeAction();
            LogContext.removeResource();
        }
    }

    private void submitReview(HttpServletRequest req, HttpServletResponse res, Connection con, User user) throws Exception {
        try {
            int bookId = Integer.parseInt(req.getParameter("bookId"));
            int rating = Integer.parseInt(req.getParameter("rating"));
            String reviewText = req.getParameter("reviewText");

            Review review = new Review();
            review.setbookId(bookId);
            review.setUserId(user.getUserId());
            review.setRating(rating);
            review.setReviewText(reviewText);

            new InsertReviewDAO(con, review).access();
            res.sendRedirect(req.getContextPath() + "/book/" + bookId);
        } catch (Exception e) {
            throw new ServletException("Error submitting review: " + e.getMessage(), e);
        }
    }

    private void handleThumb(HttpServletRequest req, HttpServletResponse res, Connection con, boolean isLike) throws Exception {
        try {
            int reviewId = Integer.parseInt(req.getParameter("reviewId"));

            if (isLike) {
                new UpdateReviewLikesDAO(con, reviewId).access().getOutputParam();
            } else {
                new UpdateReviewDisLikesDAO(con, reviewId).access().getOutputParam();
            }

            String referer = req.getHeader("Referer");
            res.sendRedirect((referer != null && referer.contains("/book/")) ? referer : req.getContextPath() + "/book");

        } catch (NumberFormatException e) {
            throw new ServletException("Invalid review ID: " + e.getMessage(), e);
        }
    }
}
