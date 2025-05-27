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
import java.util.ArrayList;
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

        String action = req.getParameter("action");

        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/user/login");
            return;
        }

        try {
            if ("view".equals(action) || action == null) {
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
            switch (action) {
                case "add" -> handleAddToWishlist(req, resp, user.getUserId());
                case "remove" -> handleRemoveFromWishlist(req, resp, user.getUserId());
                default -> ServletUtils.redirectToErrorPage(req, resp, "Unknown action: " + action);
            }

            String redirectBookId = req.getParameter("book_id_redirect");
            if (redirectBookId != null) {
                resp.sendRedirect(req.getContextPath() + "/book/" + redirectBookId);
            } else {
                resp.sendRedirect(req.getContextPath() + "/wishlist");
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

        List<Book> books = new ArrayList<>();
        
        Map<Integer, List<Author>> wishlistAuthors = new HashMap<>();
        try (Connection con = getConnection()) {
            for (Book book : books) {
                List<Author> authors = new GetAuthorsByBookDAO(con, book.getBookId()).access().getOutputParam();
                wishlistAuthors.put(book.getBookId(), authors);
            }
        }
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


        req.setAttribute("wishlist_books", books);
        req.setAttribute("wishlist_authors", wishlistAuthors);
        req.getRequestDispatcher("/jsp/wishlist/wishlistBooks.jsp").forward(req, resp);
    }

    private void handleAddToWishlist(HttpServletRequest req, HttpServletResponse resp, int userId) throws Exception {
        int bookId = Integer.parseInt(req.getParameter("book_id"));
        Wishlist wishlist;

        try (Connection con = getConnection()) {
            wishlist = new GetWishlistByUserDAO(con, userId).access().getOutputParam();
            if (wishlist == null) {
                wishlist = new CreateWishlistDAO(con, userId).access().getOutputParam();
            }

            Book book = new Book();
            book.setBookId(bookId);
            boolean alreadyExists = new IsBookInWishlistDAO(con, wishlist, book).access().getOutputParam();

            if (!alreadyExists) {
                new AddBookToWishlistDAO(con, wishlist.getWishlistId(), bookId).access();
                LOGGER.info("✅ Book ID {} added to wishlist ID {}", bookId, wishlist.getWishlistId());
            } else {
                LOGGER.info("ℹ️ Book ID {} already exists in wishlist ID {}", bookId, wishlist.getWishlistId());
            }
        }
    }

    private void handleRemoveFromWishlist(HttpServletRequest req, HttpServletResponse resp, int userId) throws Exception {
        int bookId = Integer.parseInt(req.getParameter("book_id"));
        LOGGER.info("Attempting to remove book ID {} for user ID {}", bookId, userId);
        Wishlist wishlist;
        try (Connection con = getConnection()) {
            wishlist = new GetWishlistByUserDAO(con, userId).access().getOutputParam();
            if (wishlist == null) {
                throw new Exception("No wishlist found for user.");
            }
        }
        try (Connection con = getConnection()) {
            new RemoveBookFromWishlistDAO(con, wishlist.getWishlistId(), bookId).access();
            LOGGER.info("🗑️ Removed book {} from wishlist {}", bookId, wishlist.getWishlistId());
        }
    }
}
