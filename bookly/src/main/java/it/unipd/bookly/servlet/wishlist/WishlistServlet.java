package it.unipd.bookly.servlet.wishlist;

import it.unipd.bookly.LogContext;
import it.unipd.bookly.Resource.Book;
import it.unipd.bookly.Resource.User;
import it.unipd.bookly.Resource.Wishlist;
import it.unipd.bookly.dao.wishlist.*;
import it.unipd.bookly.servlet.AbstractDatabaseServlet;
import it.unipd.bookly.utilities.ServletUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "WishlistServlet", value = "/wishlist/*")
public class WishlistServlet extends AbstractDatabaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setResource(req.getRequestURI());
        LogContext.setAction("wishlistServlet");

        String action = req.getParameter("action");
        String view = req.getParameter("view");

        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/user/login");
            return;
        }

        try {
            switch (action != null ? action : "view") {
                case "view" -> {
                    if ("wishlists".equals(view)) {
                        handleViewWishlistList(req, resp, user.getUserId());
                    } else {
                        handleViewWishlistBooks(req, resp, user.getUserId());
                    }
                }
                default -> ServletUtils.redirectToErrorPage(req, resp, "Unknown action: " + action);
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
            switch (action) {
                case "add" -> handleAddToWishlist(req, resp, user.getUserId());
                case "remove" -> handleRemoveFromWishlist(req, resp, user.getUserId());
                default -> ServletUtils.redirectToErrorPage(req, resp, "Unknown action: " + action);
            }
            resp.sendRedirect(req.getContextPath() + "/wishlist?action=view");
        } catch (Exception e) {
            LOGGER.error("WishlistServlet error (POST): ", e);
            ServletUtils.redirectToErrorPage(req, resp, "WishlistServlet error: " + e.getMessage());
        } finally {
            LogContext.removeAction();
            LogContext.removeResource();
        }
    }

    private void handleViewWishlistBooks(HttpServletRequest req, HttpServletResponse resp, int userId) throws Exception {
        try (Connection con = getConnection()) {
            List<Wishlist> wishlistBooks = new GetWishlistByUserDAO(con, userId).access().getOutputParam();
            if (wishlistBooks == null) {
                wishlistBooks = new ArrayList<>();
            }
            req.setAttribute("wishlist_books", wishlistBooks);
        }
        req.getRequestDispatcher("/jsp/wishlist/wishlistBooks.jsp").forward(req, resp);
    }

    private void handleViewWishlistList(HttpServletRequest req, HttpServletResponse resp, int userId) throws Exception {
        try (Connection con = getConnection()) {
            List<Wishlist> userWishlists = new GetWishlistByUserDAO(con, userId).access().getOutputParam();
            if (userWishlists == null) {
                userWishlists = new ArrayList<>();
            }
            req.setAttribute("user_wishlists", userWishlists);
        }
        req.getRequestDispatcher("/jsp/wishlist/userWishlists.jsp").forward(req, resp);
    }

private void handleAddToWishlist(HttpServletRequest req, HttpServletResponse resp, int userId) throws Exception {
    int bookId = Integer.parseInt(req.getParameter("book_id"));

    // Step 1: Get or create the user's wishlist
    Wishlist wishlist;
    try (Connection con = getConnection()) {
        List<Wishlist> wishlists = new GetWishlistByUserDAO(con, userId).access().getOutputParam();
        wishlist = (wishlists == null || wishlists.isEmpty())
            ? new CreateWishlistDAO(con, userId).access().getOutputParam()
            : wishlists.get(0);
    }

    // Step 2: Check if book is already in wishlist
    boolean alreadyExists;
    try (Connection con = getConnection()) {
        Book book = new Book();
        book.setBookId(bookId);
        alreadyExists = new IsBookInWishlistDAO(con, wishlist, book).access().getOutputParam();
    }

    // Step 3: Add the book only if it doesn't exist
    if (!alreadyExists) {
        try (Connection con = getConnection()) {
            new AddBookToWishlistDAO(con, wishlist.getWishlistId(), bookId).access();
            LOGGER.info("Book ID {} added to wishlist ID {}.", bookId, wishlist.getWishlistId());
        }
    } else {
        LOGGER.info("Book ID {} already in wishlist ID {}, skipping insert.", bookId, wishlist.getWishlistId());
    }
}


    private void handleRemoveFromWishlist(HttpServletRequest req, HttpServletResponse resp, int userId) throws Exception {
        int bookId = Integer.parseInt(req.getParameter("book_id"));
        int wishlistId;

        try (Connection con = getConnection()) {
            List<Wishlist> wishlists = new GetWishlistByUserDAO(con, userId).access().getOutputParam();
            if (wishlists == null || wishlists.isEmpty()) {
                throw new Exception("No wishlist found for user.");
            }
            wishlistId = wishlists.get(0).getWishlistId();
        }

        try (Connection con = getConnection()) {
            new RemoveBookFromWishlistDAO(con, wishlistId, bookId).access();
            LOGGER.info("🗑️ Removed book {} from wishlist {}", bookId, wishlistId);
        }
    }
}
