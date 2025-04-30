package it.unipd.bookly.servlet.wishlist;

import it.unipd.bookly.LogContext;
import it.unipd.bookly.Resource.Book;
import it.unipd.bookly.Resource.Wishlist;
import it.unipd.bookly.dao.wishlist.*;
import it.unipd.bookly.servlet.AbstractDatabaseServlet;
import it.unipd.bookly.utilities.ServletUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

@WebServlet(name = "WishlistServlet", value = "/user/wishlist/*")
public class WishlistServlet extends AbstractDatabaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setResource(req.getRequestURI());
        LogContext.setAction("wishlistServlet");

        try {
            Integer userId = (Integer) req.getSession().getAttribute("userId");
            if (userId == null) {
                resp.sendRedirect("/jsp/user/login.jsp");
                return;
            }

            String path = req.getPathInfo(); // e.g., "/", "/add/5"
            if (path == null || path.equals("/") || path.equals("")) {
                showWishlist(req, resp, userId);
            } else {
                ServletUtils.redirectToErrorPage(req, resp, "Invalid wishlist path: " + path);
            }

        } catch (Exception e) {
            LOGGER.error("WishlistServlet GET error", e);
            ServletUtils.redirectToErrorPage(req, resp, "Unable to load wishlist.");
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

        try {
            Integer userId = (Integer) req.getSession().getAttribute("userId");
            if (userId == null) {
                resp.sendRedirect("/jsp/user/login.jsp");
                return;
            }

            String path = req.getPathInfo();
            int wishlistId = getOrCreateWishlistId(userId);

            switch (getAction(path)) {
                case "add" -> {
                    int bookId = extractBookId(path);
                    new AddBookToWishlistDAO(getConnection(), wishlistId, bookId).access();
                    LOGGER.info("Book {} added to wishlist {}", bookId, wishlistId);
                }
                case "remove" -> {
                    int bookId = extractBookId(path);
                    new RemoveBookFromWishlistDAO(getConnection(), bookId, wishlistId).access();
                    LOGGER.info("Book {} removed from wishlist {}", bookId, wishlistId);
                }
                case "clear" -> {
                    new ClearWishlistDAO(getConnection(), wishlistId).access();
                    LOGGER.info("Wishlist {} cleared.", wishlistId);
                }
                default -> ServletUtils.redirectToErrorPage(req, resp, "Unsupported wishlist action: " + path);
            }

            resp.sendRedirect(req.getContextPath() + "/user/wishlist");

        } catch (Exception e) {
            LOGGER.error("WishlistServlet POST error", e);
            ServletUtils.redirectToErrorPage(req, resp, "Wishlist operation failed.");
        } finally {
            LogContext.removeAction();
            LogContext.removeResource();
        }
    }

    private void showWishlist(HttpServletRequest req, HttpServletResponse resp, int userId) throws Exception {
        try (Connection con = getConnection()) {
            List<Wishlist> wishlists = new GetWishlistByUserDAO(con, userId).access().getOutputParam();
            Wishlist wishlist = (wishlists == null || wishlists.isEmpty())
                    ? new CreateWishlistDAO(con, userId).access().getOutputParam()
                    : wishlists.get(0);

            List<Book> wishlistBooks = new GetBooksInWishlistDAO(con, wishlist.getWishlistId()).access().getOutputParam();

            req.setAttribute("wishlist_books", wishlistBooks);
            req.setAttribute("wishlist_id", wishlist.getWishlistId());
            req.getRequestDispatcher("/jsp/wishlist/wishlistBooks.jsp").forward(req, resp);
        }
    }

    private String getAction(String path) {
        if (path == null) return "";
        if (path.matches("/add/\\d+")) return "add";
        if (path.matches("/remove/\\d+")) return "remove";
        if (path.equals("/clear")) return "clear";
        return "";
    }

    private int extractBookId(String path) {
        return Integer.parseInt(path.substring(path.lastIndexOf('/') + 1));
    }

    private int getOrCreateWishlistId(int userId) throws Exception {
        try (Connection con = getConnection()) {
            List<Wishlist> wishlists = new GetWishlistByUserDAO(con, userId).access().getOutputParam();
            if (wishlists == null || wishlists.isEmpty()) {
                Wishlist newWishlist = new CreateWishlistDAO(con, userId).access().getOutputParam();
                if (newWishlist == null) {
                    throw new IllegalStateException("Wishlist creation failed for user ID " + userId);
                }
                return newWishlist.getWishlistId();
            }
            return wishlists.get(0).getWishlistId();
        }
    }
}
