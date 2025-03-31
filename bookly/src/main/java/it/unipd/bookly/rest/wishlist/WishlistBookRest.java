package it.unipd.bookly.rest.wishlist;

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

public class WishlistBookRest extends AbstractRestResource {

    public WishlistBookRest(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super("wishlist-books", req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        String path = req.getRequestURI();
        String method = req.getMethod();
        Message message = null;

        try {
            if ("POST".equals(method) && path.endsWith("/add")) {
                handleAddBookToWishlist();
            } else if ("DELETE".equals(method) && path.contains("/remove")) {
                handleRemoveBookFromWishlist();
            } else if ("GET".equals(method) && path.matches(".*/wishlist/books/\\d+")) {
                handleGetBooksInWishlist(path);
            } else if ("GET".equals(method) && path.contains("/contains")) {
                handleCheckBookInWishlist();
            } else if ("GET".equals(method) && path.contains("/count")) {
                handleCountBooksInWishlist();
            } else {
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                message = new Message("Unsupported operation on wishlist books", "400");
                message.toJSON(res.getOutputStream());
            }

        } catch (Exception e) {
            LOGGER.error("WishlistBookRest error", e);
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            new Message("Wishlist book operation failed", "500", e.getMessage()).toJSON(res.getOutputStream());
        }
    }

    private void handleAddBookToWishlist() throws Exception {
        int wishlistId = Integer.parseInt(req.getParameter("wishlistId"));
        int bookId = Integer.parseInt(req.getParameter("bookId"));
        Wishlist wishlist = new Wishlist();
        wishlist.setWishlistId(wishlistId);
        Book book = new Book();
        book.setBook_id(bookId);
        new AddBookToWishlistDAO(con, wishlist, book).access();
        res.setStatus(HttpServletResponse.SC_OK);
        new Message("Book added to wishlist", "200").toJSON(res.getOutputStream());
    }

    private void handleRemoveBookFromWishlist() throws Exception {
        int wishlistId = Integer.parseInt(req.getParameter("wishlistId"));
        int bookId = Integer.parseInt(req.getParameter("bookId"));
        new RemoveBookFromWishlistDAO(con, bookId, wishlistId).access();
        res.setStatus(HttpServletResponse.SC_OK);
        new Message("Book removed from wishlist", "200").toJSON(res.getOutputStream());
    }

    private void handleGetBooksInWishlist(String path) throws Exception {
        int wishlistId = Integer.parseInt(path.substring(path.lastIndexOf("/") + 1));
        List<Book> books = new GetBooksInWishlistDAO(con, wishlistId).access().getOutputParam();
        res.setContentType("application/json");
        res.setStatus(HttpServletResponse.SC_OK);
        for (Book book : books) {
            book.toJSON(res.getOutputStream());
        }
    }

    private void handleCheckBookInWishlist() throws Exception {
        int wishlistId = Integer.parseInt(req.getParameter("wishlistId"));
        int bookId = Integer.parseInt(req.getParameter("bookId"));
        Wishlist wishlist = new Wishlist();
        wishlist.setWishlistId(wishlistId);
        Book book = new Book();
        book.setBook_id(bookId);
        boolean exists = new IsBookInWishlistDAO(con, wishlist, book).access().getOutputParam();
        res.getWriter().write("{\"exists\":" + exists + "}");
    }

    private void handleCountBooksInWishlist() throws Exception {
        int wishlistId = Integer.parseInt(req.getParameter("wishlistId"));
        Wishlist wishlist = new Wishlist();
        wishlist.setWishlistId(wishlistId);
        int count = new CountBooksInWishlistDAO(con, wishlist).access().getOutputParam();
        res.getWriter().write("{\"count\":" + count + "}");
    }
}
