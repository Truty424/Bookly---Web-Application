package it.unipd.bookly.servlet.wishlist;

import it.unipd.bookly.LogContext;
import it.unipd.bookly.Resource.Book;
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
import java.util.List;

@WebServlet(name = "WishlistServlet", value = "/wishlist/*")
public class WishlistServlet extends AbstractDatabaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setResource(req.getRequestURI());
        LogContext.setAction("wishlistServlet");

        String action = req.getParameter("action");  // e.g., "view"
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/user/login");
            return;
        }

        try {
            switch (action != null ? action : "view") {
                case "view" -> handleViewWishlist(req, resp, user.getUserId());
                default -> ServletUtils.redirectToErrorPage(req, resp, "Unknown action: " + action);
            }
        } catch (Exception e) {
            LOGGER.error("WishlistServlet error (GET): {}", e.getMessage(), e);
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
            switch (action) {
                case "add" -> handleAddToWishlist(req, resp, user.getUserId());
                case "remove" -> handleRemoveFromWishlist(req, resp, user.getUserId());
                default -> ServletUtils.redirectToErrorPage(req, resp, "Unknown action: " + action);
            }
            resp.sendRedirect(req.getContextPath() + "/wishlist?action=view");  // After add/remove, refresh wishlist
        } catch (Exception e) {
            LOGGER.error("WishlistServlet error (POST): {}", e.getMessage(), e);
            ServletUtils.redirectToErrorPage(req, resp, "WishlistServlet error: " + e.getMessage());
        } finally {
            LogContext.removeAction();
            LogContext.removeResource();
        }
    }

    // ========================== HANDLERS ==========================

    private void handleViewWishlist(HttpServletRequest req, HttpServletResponse resp, int userId) throws Exception {
        List<Book> wishlist;
        try (Connection con = getConnection()) {
            wishlist = new GetWishlistByUserDAO(con, userId).access().getOutputParam();
        }
        req.setAttribute("wishlist", wishlist);
        req.getRequestDispatcher("/jsp/wishlist/wishlistBooks.jsp").forward(req, resp);
    }

    private void handleAddToWishlist(HttpServletRequest req, HttpServletResponse resp, int userId) throws Exception {
        int bookId = Integer.parseInt(req.getParameter("book_id"));
        try (Connection con = getConnection()) {
            new AddBookToWishlistDAO(con, userId, bookId).access();
            LOGGER.info("Added book ID {} to wishlist for user ID {}", bookId, userId);
        }
    }

    private void handleRemoveFromWishlist(HttpServletRequest req, HttpServletResponse resp, int userId) throws Exception {
        int bookId = Integer.parseInt(req.getParameter("book_id"));
        try (Connection con = getConnection()) {
            new RemoveBookFromWishlistDAO(con, userId, bookId).access();
            LOGGER.info("Removed book ID {} from wishlist for user ID {}", bookId, userId);
        }
    }
}
