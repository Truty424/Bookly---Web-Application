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
import java.util.List;

@WebServlet(name = "WishlistServlet", value = "/wishlist/*")
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

            String path = req.getRequestURI();

            if (path.matches(".*/wishlist/?")) {
                showWishlist(req, resp, userId);
            } else {
                ServletUtils.redirectToErrorPage(req, resp, "Invalid wishlist path: " + path);
            }

        } catch (Exception e) {
            LOGGER.error("WishlistServlet GET error: {}", e.getMessage(), e);
            ServletUtils.redirectToErrorPage(req, resp, "WishlistServlet GET error: " + e.getMessage());
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

            String path = req.getRequestURI();
            int wishlistId = getOrCreateWishlistId(userId);

            if (path.matches(".*/wishlist/add/\\d+/?")) {
                int bookId = extractBookIdFromPath(path);
                new AddBookToWishlistDAO(getConnection(), wishlistId, bookId).access();
                resp.sendRedirect("/wishlist");

            } else if (path.matches(".*/wishlist/remove/\\d+/?")) {
                int bookId = extractBookIdFromPath(path);
                new RemoveBookFromWishlistDAO(getConnection(), bookId, wishlistId).access();
                resp.sendRedirect("/wishlist");

            } else if (path.matches(".*/wishlist/clear/?")) {
                new ClearWishlistDAO(getConnection(), wishlistId).access();
                resp.sendRedirect("/wishlist");

            } else {
                ServletUtils.redirectToErrorPage(req, resp, "Invalid wishlist operation: " + path);
            }

        } catch (Exception e) {
            LOGGER.error("WishlistServlet POST error: {}", e.getMessage(), e);
            ServletUtils.redirectToErrorPage(req, resp, "WishlistServlet POST error: " + e.getMessage());
        } finally {
            LogContext.removeAction();
            LogContext.removeResource();
        }
    }

    private void showWishlist(HttpServletRequest req, HttpServletResponse resp, int userId) throws Exception {
        List<Wishlist> wishlists = new GetWishlistByUserDAO(getConnection(), userId).access().getOutputParam();

        Wishlist wishlist;
        if (wishlists == null || wishlists.isEmpty()) {
            wishlist = new CreateWishlistDAO(getConnection(), userId).access().getOutputParam();
            if (wishlist == null) {
                throw new IllegalStateException("Failed to create a new wishlist for user ID " + userId);
            }
        } else {
            wishlist = wishlists.get(0); // use the first wishlist
        }

        int wishlistId = wishlist.getWishlistId();
        List<Book> wishlistBooks = new GetBooksInWishlistDAO(getConnection(), wishlistId).access().getOutputParam();

        req.setAttribute("wishlist_books", wishlistBooks);
        req.setAttribute("wishlist_id", wishlistId);
        req.getRequestDispatcher("/jsp/wishlist/wishlistBooks.jsp").forward(req, resp);
    }

    private int extractBookIdFromPath(String path) {
        String[] segments = path.split("/");
        return Integer.parseInt(segments[segments.length - 1].trim());
    }

    private int getOrCreateWishlistId(int userId) throws Exception {
        List<Wishlist> wishlists = new GetWishlistByUserDAO(getConnection(), userId).access().getOutputParam();

        if (wishlists == null || wishlists.isEmpty()) {
            Wishlist newWishlist = new CreateWishlistDAO(getConnection(), userId).access().getOutputParam();
            if (newWishlist == null) {
                throw new IllegalStateException("Wishlist could not be created for user ID " + userId);
            }
            return newWishlist.getWishlistId();
        }

        return wishlists.get(0).getWishlistId(); // return the first available wishlist ID
    }

}
