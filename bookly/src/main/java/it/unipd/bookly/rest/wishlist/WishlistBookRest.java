package it.unipd.bookly.rest.wishlist;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.unipd.bookly.Resource.Book;
import it.unipd.bookly.Resource.Message;
import it.unipd.bookly.dao.wishlist.AddBookToWishlistDAO;
import it.unipd.bookly.dao.wishlist.GetWishlistByUserDAO;
import it.unipd.bookly.dao.wishlist.IsBookInWishlistDAO;
import it.unipd.bookly.dao.wishlist.RemoveBookFromWishlistDAO;
import it.unipd.bookly.rest.AbstractRestResource;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 * Handles wishlist book operations:
 * - POST    /api/wishlist/add              → Add book to wishlist
 * - DELETE  /api/wishlist/remove           → Remove book from wishlist
 * - GET     /api/wishlist/{userId}         → Get wishlist books for user
 * - GET     /api/wishlist/contains         → Check if book is in wishlist
 */
public class WishlistBookRest extends AbstractRestResource {

    private final ObjectMapper mapper = new ObjectMapper();

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
                    if (path.endsWith("/remove")) handleRemoveBookFromWishlist();
                    else unsupported();
                }
                case "GET" -> {
                    if (path.matches(".*/wishlist/\\d+$")) handleGetWishlistBooks(path);
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
        // Expect JSON body like: { "userId": 5, "bookId": 10 }
        Map<String, Integer> body = mapper.readValue(req.getInputStream(), Map.class);

        Integer userId = body.get("userId");
        Integer bookId = body.get("bookId");

        if (userId == null || bookId == null) {
            sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters", "E400", "'userId' and 'bookId' are required in JSON body.");
            return;
        }

        try {
            new AddBookToWishlistDAO(con, userId, bookId).access();

            sendMessage("Book added to wishlist", "200", "Book ID " + bookId + " was added for User ID " + userId);
        } catch (Exception e) {
            LOGGER.error("Error adding book to wishlist", e);
            sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server error", "E500", "Failed to add book to wishlist: " + e.getMessage());
        }
    }

    private void handleRemoveBookFromWishlist() throws IOException {
        // Expect JSON body like: { "userId": 5, "bookId": 10 }
        Map<String, Integer> body = mapper.readValue(req.getInputStream(), Map.class);

        Integer userId = body.get("userId");
        Integer bookId = body.get("bookId");

        if (userId == null || bookId == null) {
            sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters", "E400", "'userId' and 'bookId' are required in JSON body.");
            return;
        }

        try {
            new RemoveBookFromWishlistDAO(con, userId, bookId).access();
            sendMessage("Book removed from wishlist", "200", "Book ID " + bookId + " removed for User ID " + userId);
        } catch (Exception e) {
            LOGGER.error("Error removing book from wishlist", e);
            sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server error", "E500", "Failed to remove book: " + e.getMessage());
        }
    }

    private void handleGetWishlistBooks(String path) throws Exception {
        int userId = Integer.parseInt(path.substring(path.lastIndexOf("/") + 1));
        List<Book> books = new GetWishlistByUserDAO(con, userId).access().getOutputParam();

        res.setContentType("application/json;charset=UTF-8");
        res.setStatus(HttpServletResponse.SC_OK);
        mapper.writeValue(res.getOutputStream(), books);
    }

    // === Utility ===

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
