package it.unipd.bookly.servlet.wishlist;

import it.unipd.bookly.LogContext;
import it.unipd.bookly.Resource.Book;
import it.unipd.bookly.Resource.Wishlist;
import it.unipd.bookly.dao.wishlist.GetBooksInWishlistDAO;
import it.unipd.bookly.dao.wishlist.GetWishlistByUserDAO;
import it.unipd.bookly.servlet.AbstractDatabaseServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Servlet to handle wishlist operations for JSP pages.
 * 
 * Handles:
 * - GET /wishlist/user/{userId} → view all wishlists by user
 * - GET /wishlist/{wishlistId}  → view books in a specific wishlist
 */
@WebServlet(name = "WishlistServlet", value = "/wishlist/*")
public class WishlistServlet extends AbstractDatabaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setResource(req.getRequestURI());
        LogContext.setAction("WishlistServlet");

        String path = req.getRequestURI();

        try {
            if (path.matches(".*/wishlist/user/\\d+")) {
                showUserWishlists(req, resp);
            } else if (path.matches(".*/wishlist/\\d+")) {
                showWishlistBooks(req, resp);
            } else {
                resp.sendRedirect("/html/error.html");
            }
        } catch (Exception e) {
            LOGGER.error("WishlistServlet error: {}", e.getMessage());
            resp.sendRedirect("/html/error.html");
        } finally {
            LogContext.removeAction();
            LogContext.removeResource();
        }
    }

    private void showUserWishlists(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String[] parts = req.getRequestURI().split("/");
        int userId = Integer.parseInt(parts[parts.length - 1]);

        List<Wishlist> wishlists = new GetWishlistByUserDAO(getConnection(), userId).access().getOutputParam();

        req.setAttribute("user_wishlists", wishlists);
        req.getRequestDispatcher("/jsp/wishlist/userWishlists.jsp").forward(req, resp);
    }

    private void showWishlistBooks(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String[] parts = req.getRequestURI().split("/");
        int wishlistId = Integer.parseInt(parts[parts.length - 1]);

        List<Book> books = new GetBooksInWishlistDAO(getConnection(), wishlistId).access().getOutputParam();

        req.setAttribute("wishlist_books", books);
        req.setAttribute("wishlist_id", wishlistId);
        req.getRequestDispatcher("/jsp/wishlist/wishlistBooks.jsp").forward(req, resp);
    }
}
