package it.unipd.bookly.rest.cart;

import java.io.IOException;
import java.sql.Connection;

import it.unipd.bookly.Resource.Message;
import it.unipd.bookly.dao.cart.RemoveBookFromCartDAO;
import it.unipd.bookly.rest.AbstractRestResource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * REST endpoint to remove a book from the shopping cart.
 * 
 * Endpoint: DELETE /api/cart/remove?cartId=123&bookId=456
 */
public class RemoveFromCartRest extends AbstractRestResource {

    public RemoveFromCartRest(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super("remove-from-cart", req, res, con);
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

            // DAO call to remove book from cart
            new RemoveBookFromCartDAO(con, bookId, cartId).access();

            message = new Message("Book removed from cart successfully.", "200", "Book ID " + bookId + " removed from cart " + cartId);
            res.setStatus(HttpServletResponse.SC_OK);
            message.toJSON(res.getOutputStream());

        } catch (NumberFormatException ex) {
            message = new Message("Invalid 'cartId' or 'bookId' format.", "E401", "Both parameters must be integers.");
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            message.toJSON(res.getOutputStream());
        } catch (Exception ex) {
            LOGGER.error("Error removing book from cart", ex);
            message = new Message("Internal server error while removing book from cart.", "E500", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            message.toJSON(res.getOutputStream());
        }
    }
}