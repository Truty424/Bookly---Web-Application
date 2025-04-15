package it.unipd.bookly.rest.wishlist;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.unipd.bookly.Resource.Book;
import it.unipd.bookly.Resource.Message;
import it.unipd.bookly.Resource.Wishlist;
import it.unipd.bookly.dao.wishlist.*;
import it.unipd.bookly.rest.AbstractRestResource;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

/**
 * Handles:
 * - POST   /api/wishlist/books/add                  → Add book to wishlist
 * - DELETE /api/wishlist/books/remove               → Remove book from wishlist
 * - GET    /api/wishlist/books/{wishlistId}         → Get all books in wishlist
 * - GET    /api/wishlist/books/contains             → Check if book exists in wishlist
 * - GET    /api/wishlist/books/count                → Count books in wishlist
 */
public class WishlistBookRest extends AbstractRestResource {

    public WishlistBookRest(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super("wishlist-books", req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        String path = req.getRequestURI();
        String method = req.getMethod();

        try {
            switch (method) {
                case "POST" -> {
                    if (path.endsWith("/add")) {
                        handleAddBookToWishlist();
                    } else {
                        unsupported();
                    }
                }
                case "DELETE" -> {
                    if (path.contains("/remove")) {
                        handleRemoveBookFromWishlist();
                    } else {
                        unsupported();
                    }
                }
                case "GET" -> {
                    if (path.matches(".*/wishlist/books/\\d+$")) {
                        handleGetBooksInWishlist(path);
                    } else if (path.contains("/contains")) {
                        handleCheckBookInWishlist();
                    } else if (path.contains("/count")) {
                        handleCountBooksInWishlist();
                    } else {
                        unsupported();
                    }
                }
                default -> unsupported();
            }
        } catch (Exception e) {
            LOGGER.error("WishlistBookRest error", e);
            sendError("Wishlist book operation failed", "500", e.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void handleAddBookToWishlist() throws Exception {
        int wishlistId = Integer.parseInt(req.getParameter("wishlistId"));
        int bookId = Integer.parseInt(req.getParameter("bookId"));

        Wishlist wishlist = new Wishlist();
        wishlist.setWishlistId(wishlistId);

        Book book = new Book();
        book.setBookId(bookId);

        new AddBookToWishlistDAO(con, wishlist, book).access();
        sendMessage("Book added to wishlist", "200", "Book ID: " + bookId + " added to Wishlist ID: " + wishlistId);
    }

    private void handleRemoveBookFromWishlist() throws Exception {
        int wishlistId = Integer.parseInt(req.getParameter("wishlistId"));
        int bookId = Integer.parseInt(req.getParameter("bookId"));

        new RemoveBookFromWishlistDAO(con, bookId, wishlistId).access();
        sendMessage("Book removed from wishlist", "200", "Book ID: " + bookId + " removed from Wishlist ID: " + wishlistId);
    }

    private void handleGetBooksInWishlist(String path) throws Exception {
        int wishlistId = Integer.parseInt(path.substring(path.lastIndexOf("/") + 1));
        List<Book> books = new GetBooksInWishlistDAO(con, wishlistId).access().getOutputParam();

        res.setContentType("application/json;charset=UTF-8");
        res.setStatus(HttpServletResponse.SC_OK);
        new ObjectMapper().writeValue(res.getOutputStream(), books);
    }

    private void handleCheckBookInWishlist() throws Exception {
        int wishlistId = Integer.parseInt(req.getParameter("wishlistId"));
        int bookId = Integer.parseInt(req.getParameter("bookId"));

        Wishlist wishlist = new Wishlist();
        wishlist.setWishlistId(wishlistId);

        Book book = new Book();
        book.setBookId(bookId);

        boolean exists = new IsBookInWishlistDAO(con, wishlist, book).access().getOutputParam();
        res.setContentType("application/json;charset=UTF-8");
        res.setStatus(HttpServletResponse.SC_OK);
        res.getWriter().write("{\"exists\": " + exists + "}");
    }

    private void handleCountBooksInWishlist() throws Exception {
        int wishlistId = Integer.parseInt(req.getParameter("wishlistId"));
        Wishlist wishlist = new Wishlist();
        wishlist.setWishlistId(wishlistId);

        int count = new CountBooksInWishlistDAO(con, wishlist).access().getOutputParam();
        res.setContentType("application/json;charset=UTF-8");
        res.setStatus(HttpServletResponse.SC_OK);
        res.getWriter().write("{\"count\": " + count + "}");
    }

    private void unsupported() throws IOException {
        sendError("Unsupported operation", "400", "Operation on wishlist books not allowed", HttpServletResponse.SC_BAD_REQUEST);
    }

    private void sendMessage(String title, String code, String detail) throws IOException {
        res.setStatus(HttpServletResponse.SC_OK);
        new Message(title, code, detail).toJSON(res.getOutputStream());
    }

    private void sendError(String title, String code, String detail, int status) throws IOException {
        res.setStatus(status);
        new Message(title, code, detail).toJSON(res.getOutputStream());
    }
}
