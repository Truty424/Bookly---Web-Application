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
 * Handles wishlist book operations:
 * - POST    /api/wishlist/books/add           → Add book to wishlist
 * - DELETE  /api/wishlist/books/remove        → Remove book from wishlist
 * - GET     /api/wishlist/books/{wishlistId}  → Get books in wishlist
 * - GET     /api/wishlist/books/contains      → Check if book is in wishlist
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
                    if (path.endsWith("/add")) handleAddBookToWishlist();
                    else unsupported();
                }
                case "DELETE" -> {
                    if (path.contains("/remove")) handleRemoveBookFromWishlist();
                    else unsupported();
                }
                case "GET" -> {
                    if (path.matches(".*/wishlist/books/\\d+$")) handleGetBooksInWishlist(path);
                    else if (path.contains("/contains")) handleCheckBookInWishlist();
                    else unsupported();
                }
                default -> unsupported();
            }
        } catch (Exception e) {
            LOGGER.error("WishlistBookRest error", e);
            sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Wishlist book operation failed", "E500", e.getMessage());
        }
    }

    private void handleAddBookToWishlist() throws IOException {
        String wishlistIdParam = req.getParameter("wishlistId");
        String bookIdParam = req.getParameter("book_id");

        if (wishlistIdParam == null || bookIdParam == null) {
            sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters", "E400", "'wishlistId' and 'book_id' are required.");
            return;
        }

        try {
            int wishlistId = Integer.parseInt(wishlistIdParam);
            int bookId = Integer.parseInt(bookIdParam);

            new AddBookToWishlistDAO(con, wishlistId, bookId).access();

            res.setStatus(HttpServletResponse.SC_OK);
            new Message("Book added to wishlist", "200",
                    "Book ID " + bookId + " was added to Wishlist ID " + wishlistId).toJSON(res.getOutputStream());

        } catch (NumberFormatException e) {
            sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid ID format", "E401", "'wishlistId' and 'book_id' must be valid integers.");
        } catch (Exception e) {
            LOGGER.error("Error adding book to wishlist", e);
            sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server error", "E500", "Failed to add book to wishlist: " + e.getMessage());
        }
    }

    private void handleRemoveBookFromWishlist() throws Exception {
        int wishlistId = Integer.parseInt(req.getParameter("wishlistId"));
        int bookId = Integer.parseInt(req.getParameter("book_id"));

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
        int bookId = Integer.parseInt(req.getParameter("book_id"));

        Wishlist wishlist = new Wishlist();
        wishlist.setWishlistId(wishlistId);

        Book book = new Book();
        book.setBookId(bookId);

        boolean exists = new IsBookInWishlistDAO(con, wishlist, book).access().getOutputParam();

        res.setContentType("application/json;charset=UTF-8");
        res.setStatus(HttpServletResponse.SC_OK);
        res.getWriter().write("{\"exists\": " + exists + "}");
    }


    private void unsupported() throws IOException {
        sendError(HttpServletResponse.SC_BAD_REQUEST, "Unsupported operation", "E405", "This endpoint or method is not supported.");
    }

    private void sendMessage(String title, String code, String detail) throws IOException {
        res.setStatus(HttpServletResponse.SC_OK);
        new Message(title, code, detail).toJSON(res.getOutputStream());
    }

    private void sendError(int status, String title, String code, String detail) throws IOException {
        res.setStatus(status);
        new Message(title, code, detail).toJSON(res.getOutputStream());
    }
}
