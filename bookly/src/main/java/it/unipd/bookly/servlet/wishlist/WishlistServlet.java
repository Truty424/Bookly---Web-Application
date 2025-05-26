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
import java.util.List;

@WebServlet(name = "WishlistServlet", value = "/wishlist/*")
public class WishlistServlet extends AbstractDatabaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
            switch (action != null ? action : "view") {
                case "view" -> handleViewWishlistBooks(req, resp, user.getUserId());
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

            String redirectBookId = req.getParameter("book_id_redirect");
            if (redirectBookId != null) {
                resp.sendRedirect(req.getContextPath() + "/book/details?bookId=" + redirectBookId);
            } else {
                resp.sendRedirect(req.getContextPath() + "/wishlist?action=view");
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
        Wishlist wishlist;

        try (Connection con = getConnection()) {
            wishlist = new GetWishlistByUserDAO(con, userId).access().getOutputParam();
        }

        if (wishlist == null) {
            try (Connection con = getConnection()) {
                wishlist = new CreateWishlistDAO(con, userId).access().getOutputParam();
            }
        }

        List<Book> books;
        try (Connection con = getConnection()) {
            books = new GetBooksInWishlistDAO(con, wishlist.getWishlistId()).access().getOutputParam();
        }

        req.setAttribute("wishlist_books", books);
        req.getRequestDispatcher("/jsp/wishlist/wishlistBooks.jsp").forward(req, resp);
    }

    private void handleAddToWishlist(HttpServletRequest req, HttpServletResponse resp, int userId) throws Exception {
        int bookId = Integer.parseInt(req.getParameter("book_id"));
        Wishlist wishlist;

        try (Connection con = getConnection()) {
            wishlist = new GetWishlistByUserDAO(con, userId).access().getOutputParam();
        }

        if (wishlist == null) {
            try (Connection con = getConnection()) {
                wishlist = new CreateWishlistDAO(con, userId).access().getOutputParam();
            }
        }

        boolean alreadyExists;
        try (Connection con = getConnection()) {
            Book book = new Book();
            book.setBookId(bookId);
            alreadyExists = new IsBookInWishlistDAO(con, wishlist, book).access().getOutputParam();
        }

        if (!alreadyExists) {
            try (Connection con = getConnection()) {
                new AddBookToWishlistDAO(con, wishlist.getWishlistId(), bookId).access();
                LOGGER.info("✅ Book ID {} added to wishlist ID {}", bookId, wishlist.getWishlistId());
            }
        } else {
            LOGGER.info("ℹ️ Book ID {} already in wishlist ID {}, skipping insert.", bookId, wishlist.getWishlistId());
        }
    }

    private void handleRemoveFromWishlist(HttpServletRequest req, HttpServletResponse resp, int userId) throws Exception {
        int bookId = Integer.parseInt(req.getParameter("book_id"));
        Wishlist wishlist;

        try (Connection con = getConnection()) {
            wishlist = new GetWishlistByUserDAO(con, userId).access().getOutputParam();
        }

        if (wishlist == null) {
            throw new Exception("No wishlist found for user.");
        }

        try (Connection con = getConnection()) {
            new RemoveBookFromWishlistDAO(con, wishlist.getWishlistId(), bookId).access();
            LOGGER.info("🗑️ Removed book {} from wishlist {}", bookId, wishlist.getWishlistId());
        }
    }
}
