package it.unipd.bookly.servlet.wishlist;

import it.unipd.bookly.LogContext;
import it.unipd.bookly.Resource.Author;
import it.unipd.bookly.Resource.Book;
import it.unipd.bookly.Resource.User;
import it.unipd.bookly.Resource.Wishlist;
import it.unipd.bookly.dao.author.GetAuthorsByBookDAO;
import it.unipd.bookly.dao.wishlist.*;
import it.unipd.bookly.servlet.AbstractDatabaseServlet;
import it.unipd.bookly.utilities.ServletUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "WishlistServlet", value = "/wishlist/*")
public class WishlistServlet extends AbstractDatabaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setResource(req.getRequestURI());
        LogContext.setAction("wishlistServlet");

        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/user/login");
            return;
        }

        try {
            String action = req.getParameter("action");
            if (action == null || action.equals("view")) {
                handleViewWishlistBooks(req, resp, user.getUserId());
            } else {
                ServletUtils.redirectToErrorPage(req, resp, "Unknown action: " + action);
            }
        } catch (Exception e) {
            LOGGER.error("WishlistServlet error (GET): ", e);
            ServletUtils.redirectToErrorPage(req, resp, "WishlistServlet error: " + e.getMessage());
        } finally {
            LogContext.removeAction();
            LogContext.removeResource();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setResource(req.getRequestURI());
        LogContext.setAction("wishlistServlet");

        String action = req.getParameter("action");
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/user/login");
            return;
        }

        try {
            String redirectTo = req.getParameter("redirect_to"); // NEW: get optional redirect target
            switch (action) {
                case "add" -> {
                    handleAddToWishlist(req, resp, user.getUserId());
                    resp.sendRedirect(redirectTo != null ? redirectTo : req.getContextPath() + "/wishlist");
                }
                case "remove" -> {
                    handleRemoveFromWishlist(req, resp, user.getUserId());
                    resp.sendRedirect(redirectTo != null ? redirectTo : req.getContextPath() + "/wishlist");
                }
                default ->
                    ServletUtils.redirectToErrorPage(req, resp, "Unknown action: " + action);
            }
        } catch (Exception e) {
            LOGGER.error("WishlistServlet error (POST): ", e);
            ServletUtils.redirectToErrorPage(req, resp, "WishlistServlet error: " + e.getMessage());
        } finally {
            LogContext.removeAction();
            LogContext.removeResource();
        }
    }

    private void handleViewWishlistBooks(HttpServletRequest req, HttpServletResponse resp, int userId) throws Exception {
        String bookIdParam = req.getParameter("book_id");
        Wishlist wishlist;
        List<Book> books;
        Map<Integer, List<Author>> wishlistAuthors = new HashMap<>();


        try (Connection con = getConnection()) {
            wishlist = new GetWishlistByUserDAO(con, userId).access().getOutputParam();
        }

        if (wishlist == null) {
            try (Connection con = getConnection()) {
                wishlist = new CreateWishlistDAO(con, userId).access().getOutputParam();
            }
        }


        try (Connection con = getConnection()) {
            books = new GetBooksInWishlistDAO(con, wishlist.getWishlistId()).access().getOutputParam();
        }
        for (Book book : books) {
            try (Connection con = getConnection()) {
                List<Author> authorsForBook = new GetAuthorsByBookDAO(con, book.getBookId()).access().getOutputParam();
                wishlistAuthors.put(book.getBookId(), authorsForBook);
            }
        }

        req.setAttribute("wishlist_books", books);
        req.setAttribute("wishlist_authors", wishlistAuthors);
        req.getRequestDispatcher("/jsp/wishlist/wishlistBooks.jsp").forward(req, resp);
    }

    private void handleAddToWishlist(HttpServletRequest req, HttpServletResponse resp, int userId) throws Exception {
        int bookId = Integer.parseInt(req.getParameter("book_id"));
        Wishlist wishlist;
        int wishlistId;
        // Step 1: Get or create the user's wishlist
        try (Connection con = getConnection()) {
            wishlist = new GetWishlistByUserDAO(con, userId).access().getOutputParam();
        }

        try (Connection con = getConnection()) {
            if (wishlist != null) {
                wishlistId = wishlist.getWishlistId();
                // use id safely
            } else {
                // handle case when wishlist is null
                wishlist = new CreateWishlistDAO(con, userId).access().getOutputParam();
                wishlistId = wishlist.getWishlistId(); // safe now
            }
        }
        try (Connection con = getConnection()) {
            new AddBookToWishlistDAO(con, wishlistId, bookId).access();
            LOGGER.info("✅ Book ID {} added", bookId);
        }
    }

    private void handleRemoveFromWishlist(HttpServletRequest req, HttpServletResponse resp, int userId) throws Exception {
        int bookId = Integer.parseInt(req.getParameter("book_id"));
        LOGGER.info("Attempting to remove book ID {} for user ID {}", bookId, userId);
        try (Connection con = getConnection()) {
            new RemoveBookFromWishlistDAO(con, bookId).access();
        } catch (Exception e) {
            LOGGER.error("❌ Error removing book from wishlist", e);
            throw e;
        }
    }

}
