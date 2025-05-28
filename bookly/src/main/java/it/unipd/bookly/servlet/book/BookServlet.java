package it.unipd.bookly.servlet.book;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

import it.unipd.bookly.LogContext;
import it.unipd.bookly.Resource.Book;
import it.unipd.bookly.Resource.Author;
import it.unipd.bookly.Resource.Review;
import it.unipd.bookly.Resource.User;
import it.unipd.bookly.Resource.Wishlist;
import it.unipd.bookly.dao.book.GetAllBooksDAO;
import it.unipd.bookly.dao.book.GetBookByIdDAO;
import it.unipd.bookly.dao.author.GetAuthorsByBookDAO;
import it.unipd.bookly.dao.review.CountReviewsForBookDAO;
import it.unipd.bookly.dao.review.GetAvgRatingForBookDAO;
import it.unipd.bookly.dao.review.GetReviewsByBookDAO;
import it.unipd.bookly.dao.wishlist.IsBookInWishlistDAO;
import it.unipd.bookly.dao.wishlist.GetWishlistByUserDAO;
import it.unipd.bookly.servlet.AbstractDatabaseServlet;
import it.unipd.bookly.utilities.ServletUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "BookServlet", value = "/book/*")
public class BookServlet extends AbstractDatabaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setResource(req.getRequestURI());
        LogContext.setAction("bookServlet");

        String path = req.getRequestURI();

        try {
            if (path.matches(".*/book/?")) {
                showAllBooks(req, resp);
            } else if (path.matches(".*/book/\\d+")) {
                showBookDetails(req, resp);
            } else {
                ServletUtils.redirectToErrorPage(req, resp, "Invalid path in BookServlet: " + path);
            }
        } catch (Exception e) {
            LOGGER.error("BookServlet error: {}", e.getMessage(), e);
            ServletUtils.redirectToErrorPage(req, resp, "BookServlet error: " + e.getMessage());
        } finally {
            LogContext.removeAction();
            LogContext.removeResource();
        }
    }

    private void showAllBooks(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        try (Connection con = getConnection()) {
            List<Book> books = new GetAllBooksDAO(con).access().getOutputParam();
            req.setAttribute("all_books", books);
            req.getRequestDispatcher("/jsp/book/allBooks.jsp").forward(req, resp);
        }
    }

    private void showBookDetails(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String[] segments = req.getRequestURI().split("/");
        int bookId = Integer.parseInt(segments[segments.length - 1]);

        Book book;
        List<Author> authors;
        List<Review> reviews;
        Double averageRating = 0.0;
        Integer reviewCount = 0;
        boolean isInWishlist = false;
        Wishlist wishlist;

        // Step 1: Get book
        try (Connection con = getConnection()) {
            book = new GetBookByIdDAO(con, bookId).access().getOutputParam();
        }

        // Step 2: Get authors
        try (Connection con = getConnection()) {
            authors = new GetAuthorsByBookDAO(con, bookId).access().getOutputParam();
        }

        // Step 3: Get reviews
        try (Connection con = getConnection()) {
            reviews = new GetReviewsByBookDAO(con, bookId).access().getOutputParam();
        }

        // Step 4: Get average rating
        try (Connection con = getConnection()) {
            averageRating = new GetAvgRatingForBookDAO(con, bookId).access().getOutputParam();
        }

        // Step 5: Get review count
        try (Connection con = getConnection()) {
            reviewCount = new CountReviewsForBookDAO(con, bookId).access().getOutputParam();
        }

        // Step 6: Check if book is in user's wishlist
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            try (Connection con = getConnection()) {
                wishlist = new GetWishlistByUserDAO(con, user.getUserId()).access().getOutputParam();
            }
            try (Connection con = getConnection()) {
                if (wishlist != null) {
                    isInWishlist = new IsBookInWishlistDAO(con, wishlist.getWishlistId(), bookId).access().getOutputParam();
                }
            }
        }

        if (book != null) {
            req.setAttribute("book_details", book);
            req.setAttribute("authors", authors);
            req.setAttribute("reviews", reviews);
            req.setAttribute("average_rating", averageRating);
            req.setAttribute("review_count", reviewCount);
            req.setAttribute("isInWishlist", isInWishlist);
            req.getRequestDispatcher("/jsp/book/bookDetails.jsp").forward(req, resp);
        } else {
            ServletUtils.redirectToErrorPage(req, resp, "Book not found for ID: " + bookId);
        }
    }

}
