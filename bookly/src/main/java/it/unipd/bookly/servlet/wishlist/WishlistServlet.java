package it.unipd.bookly.servlet.wishlist;

import it.unipd.bookly.LogContext;
import it.unipd.bookly.Resource.Book;
import it.unipd.bookly.Resource.User;
import it.unipd.bookly.Resource.Wishlist;
import it.unipd.bookly.dao.wishlist.AddBookToWishlistDAO;
import it.unipd.bookly.dao.wishlist.CreateWishlistDAO;
import it.unipd.bookly.dao.wishlist.GetBooksInWishlistDAO;
import it.unipd.bookly.dao.wishlist.GetWishlistByUserDAO;
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
        LogContext.setAction("WishlistServlet");

        String path = req.getRequestURI();

        try (Connection con = getConnection()) {
            if (path.matches(".*/wishlist/user/\\d+")) {
                showUserWishlists(req, resp, con);
            } else if (path.matches(".*/wishlist/\\d+")) {
                showWishlistBooks(req, resp, con);
            } else if (path.endsWith("/wishlist") || path.endsWith("/wishlist/")) {
                HttpSession session = req.getSession(false);
                User user = (session != null) ? (User) session.getAttribute("user") : null;

                if (user != null) {
                    resp.sendRedirect(req.getContextPath() + "/wishlist/user/" + user.getUserId());
                } else {
                    ServletUtils.redirectToErrorPage(req, resp, "You must be logged in to view your wishlist.");
                }
            } else {
                ServletUtils.redirectToErrorPage(req, resp, "Invalid wishlist path.");
            }
        } catch (Exception e) {
            LOGGER.error("WishlistServlet error: {}", e.getMessage(), e);
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
        LogContext.setAction("WishlistServlet");

        String path = req.getRequestURI();

        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/user/login");
            return;
        }

        Connection con = null;

        try {
            con = getConnection();  // <- one connection for everything below

            if (path.matches(".*/wishlist/add/\\d+")) {
                int bookId = Integer.parseInt(path.substring(path.lastIndexOf("/") + 1));
                Wishlist wishlist;

                // 1. Try to fetch the user's wishlist
                List<Wishlist> wishlists = new GetWishlistByUserDAO(con, user.getUserId()).access().getOutputParam();

                if (wishlists == null || wishlists.isEmpty()) {
                    // 2. Create a new wishlist with the SAME connection
                    wishlist = new CreateWishlistDAO(con, user.getUserId()).access().getOutputParam();
                    if (wishlist == null) {
                        ServletUtils.redirectToErrorPage(req, resp, "Failed to create a wishlist for the user.");
                        return;
                    }
                } else {
                    wishlist = wishlists.get(0);
                }

                // 3. Add book to wishlist (same connection)
                Book book = new Book();
                book.setBookId(bookId);
                new AddBookToWishlistDAO(con, wishlist, book).access();

                // 4. Done, redirect
                resp.sendRedirect(req.getContextPath() + "/wishlist/" + wishlist.getWishlistId());

            } else {
                ServletUtils.redirectToErrorPage(req, resp, "Invalid wishlist action.");
            }

        } catch (Exception e) {
            LOGGER.error("WishlistServlet POST error: {}", e.getMessage(), e);
            ServletUtils.redirectToErrorPage(req, resp, "WishlistServlet POST error: " + e.getMessage());
        } finally {
            if (con != null) {
                try {
                    con.close();
                    LOGGER.debug("Connection successfully closed.");
                } catch (Exception ex) {
                    LOGGER.error("Error closing connection: {}", ex.getMessage(), ex);
                }
            }
            LogContext.removeAction();
            LogContext.removeResource();
        }
    }




    private void showUserWishlists(HttpServletRequest req, HttpServletResponse resp, Connection con) throws Exception {
        String[] parts = req.getRequestURI().split("/");
        int userId = Integer.parseInt(parts[parts.length - 1]);

        List<Wishlist> wishlists = new GetWishlistByUserDAO(con, userId).access().getOutputParam();
        req.setAttribute("user_wishlists", wishlists);
        req.getRequestDispatcher("/jsp/wishlist/userWishlists.jsp").forward(req, resp);
    }

    private void showWishlistBooks(HttpServletRequest req, HttpServletResponse resp, Connection con) throws Exception {
        String[] parts = req.getRequestURI().split("/");
        int wishlistId = Integer.parseInt(parts[parts.length - 1]);

        List<Book> books = new GetBooksInWishlistDAO(con, wishlistId).access().getOutputParam();
        req.setAttribute("wishlist_books", books);
        req.setAttribute("wishlist_id", wishlistId);
        req.getRequestDispatcher("/jsp/wishlist/wishlistBooks.jsp").forward(req, resp);
    }
}
