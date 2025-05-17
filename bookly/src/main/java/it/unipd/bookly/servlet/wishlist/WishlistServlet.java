package it.unipd.bookly.servlet.wishlist;

import it.unipd.bookly.LogContext;
import it.unipd.bookly.Resource.User;
import it.unipd.bookly.dao.wishlist.AddBookToWishlistDAO;
import it.unipd.bookly.dao.wishlist.GetWishlistByUserDAO;
import it.unipd.bookly.dao.wishlist.RemoveBookFromWishlistDAO;
import it.unipd.bookly.servlet.AbstractDatabaseServlet;
import it.unipd.bookly.utilities.ServletUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.unipd.bookly.Resource.Book;
import it.unipd.bookly.Resource.Wishlist;
import it.unipd.bookly.dao.wishlist.CreateWishlistDAO;
import it.unipd.bookly.dao.wishlist.GetBooksInWishlistDAO;

@WebServlet(name = "WishlistServlet", value = "/wishlist/*")
public class WishlistServlet extends AbstractDatabaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setResource(req.getRequestURI());
        LogContext.setAction("WishlistServlet");

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            res.sendRedirect(req.getContextPath() + "/user/login");
            return;
        }

        User user = (User) session.getAttribute("user");

        try (Connection con = getConnection()) {
            List<Book> books = new GetBooksInWishlistDAO(con, user.getUserId()).access().getOutputParam();

            req.setAttribute("wishlist_books", books != null ? books : Collections.emptyList());
            req.getRequestDispatcher("/jsp/wishlist/wishlistBooks.jsp").forward(req, res);

        } catch (Exception e) {
            LOGGER.error("WishlistServlet error: {}", e.getMessage(), e);
            ServletUtils.redirectToErrorPage(req, res, "Failed to load wishlist.");
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
            switch (action) {
                case "add" ->
                    handleAddToWishlist(req, resp, user.getUserId());
                case "remove" ->
                    handleRemoveFromWishlist(req, resp, user.getUserId());
                default ->
                    ServletUtils.redirectToErrorPage(req, resp, "Unknown action: " + action);
            }
            resp.sendRedirect(req.getContextPath() + "/wishlist?action=view");
        } catch (Exception e) {
            LOGGER.error("WishlistServlet error (POST): {}", e.getMessage(), e);
            ServletUtils.redirectToErrorPage(req, resp, "WishlistServlet error: " + e.getMessage());
        } finally {
            LogContext.removeAction();
            LogContext.removeResource();
        }
    }

    private void handleViewWishlist(HttpServletRequest req, HttpServletResponse resp, int userId) throws Exception {
        try (Connection con = getConnection()) {
            List<Wishlist> wishlistBooks = new GetWishlistByUserDAO(con, userId).access().getOutputParam();
            if (wishlistBooks == null) {
                wishlistBooks = new ArrayList<>(); // Never pass null to JSP
            }
            req.setAttribute("wishlist_books", wishlistBooks);
        }

        req.getRequestDispatcher("/jsp/wishlist/wishlistBooks.jsp").forward(req, resp);
    }

    private void handleAddToWishlist(HttpServletRequest req, HttpServletResponse resp, int userId) throws Exception {
        int bookId = Integer.parseInt(req.getParameter("book_id"));

        List<Wishlist> wishlists;
        try (Connection con = getConnection()) {
            wishlists = new GetWishlistByUserDAO(con, userId).access().getOutputParam();
            LOGGER.info("Checked existing wishlist(s) for user ID {}: {}", userId, wishlists);
        }

        Wishlist wishlist;
        if (wishlists == null || wishlists.isEmpty()) {
            LOGGER.info("No wishlist found for user ID {}. Creating a new one...", userId);

            try (Connection con = getConnection()) {
                wishlist = new CreateWishlistDAO(con, userId).access().getOutputParam();
                LOGGER.info("Created new wishlist ID {} for user ID {}", wishlist.getWishlistId(), userId);
            }
        } else {
            wishlist = wishlists.get(0);
            LOGGER.info("Using existing wishlist ID {} for user ID {}", wishlist.getWishlistId(), userId);
        }

        try (Connection con = getConnection()) {
            new AddBookToWishlistDAO(con, wishlist.getWishlistId(), bookId).access();
        }
    }

    private void handleRemoveFromWishlist(HttpServletRequest req, HttpServletResponse resp, int userId) throws Exception {
        int bookId = Integer.parseInt(req.getParameter("book_id"));

        List<Wishlist> wishlists;
        try (Connection con = getConnection()) {
            wishlists = new GetWishlistByUserDAO(con, userId).access().getOutputParam();
            LOGGER.info("Checked existing wishlist(s) for user ID {}: {}", userId, wishlists);
        }

        if (wishlists == null || wishlists.isEmpty()) {
            LOGGER.error("No wishlist found for user ID {}", userId);
            throw new Exception("No wishlist found for user.");
        }

        int wishlistId = wishlists.get(0).getWishlistId();
        try (Connection con = getConnection()) {
            new RemoveBookFromWishlistDAO(con, wishlistId, bookId).access();
            LOGGER.info("Removed book ID {} from wishlist ID {} (user ID {}).", bookId, wishlistId, userId);
        }
    }
}
