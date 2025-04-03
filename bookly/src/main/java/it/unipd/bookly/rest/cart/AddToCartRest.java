package it.unipd.bookly.rest.cart;

import it.unipd.bookly.Resource.Message;
import it.unipd.bookly.dao.cart.AddBookToCartDAO;
import it.unipd.bookly.rest.AbstractRestResource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;

/**
 * REST endpoint to add a book to the shopping cart.
 * 
 * Endpoint: POST /api/cart/add?cartId=123&bookId=456
 */
public class AddToCartRest extends AbstractRestResource {

    public AddToCartRest(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super("add-to-cart", req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        String cartIdParam = req.getParameter("cartId");
        String bookIdParam = req.getParameter("bookId");

        Message message;

        if (cartIdParam == null || bookIdParam == null) {
            message = new Message("Missing 'cartId' or 'bookId' parameter.", "E400", "Both parameters are required.");
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            message.toJSON(res.getOutputStream());
            return;
        }

        try {
            int cartId = Integer.parseInt(cartIdParam);
            int bookId = Integer.parseInt(bookIdParam);

            // DAO call to add book to cart
            new AddBookToCartDAO(con, bookId, cartId).access();

            message = new Message("Book added to cart successfully.", "200", "Book ID " + bookId + " added to cart " + cartId);
            res.setStatus(HttpServletResponse.SC_OK);
            message.toJSON(res.getOutputStream());

        } catch (NumberFormatException ex) {
            message = new Message("Invalid 'cartId' or 'bookId' format.", "E401", "Both parameters must be integers.");
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            message.toJSON(res.getOutputStream());
        } catch (Exception ex) {
            LOGGER.error("Error adding book to cart", ex);
            message = new Message("Internal server error while adding book to cart.", "E500", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            message.toJSON(res.getOutputStream());
        }
    }
}