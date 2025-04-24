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
 * Endpoint: DELETE /api/cart/remove?cartId=123&amp;bookId=456
 */
public class RemoveFromCartRest extends AbstractRestResource {

    public RemoveFromCartRest(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super("remove-from-cart", req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        final String method = req.getMethod();

        switch (method) {
            case "DELETE" -> handleRemoveFromCart();
            default -> sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED,
                    "Unsupported HTTP method", "E405", "Only DELETE is supported.");
        }
    }

    private void handleRemoveFromCart() throws IOException {
        String cartIdParam = req.getParameter("cartId");
        String bookIdParam = req.getParameter("bookId");

        if (cartIdParam == null || bookIdParam == null) {
            sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Missing parameters", "E400", "Both 'cartId' and 'bookId' must be provided.");
            return;
        }

        try {
            int cartId = Integer.parseInt(cartIdParam);
            int bookId = Integer.parseInt(bookIdParam);

            // DAO to remove book from cart
            new RemoveBookFromCartDAO(con, bookId, cartId).access();

            Message success = new Message("Book removed from cart successfully.", "200",
                    "Book ID " + bookId + " removed from cart " + cartId);
            res.setStatus(HttpServletResponse.SC_OK);
            success.toJSON(res.getOutputStream());

        } catch (NumberFormatException ex) {
            sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid parameter format", "E401", "'cartId' and 'bookId' must be valid integers.");
        } catch (Exception ex) {
            LOGGER.error("Error removing book from cart", ex);
            sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Internal server error", "E500", ex.getMessage());
        }
    }

    private void sendError(int status, String title, String code, String detail) throws IOException {
        res.setStatus(status);
        new Message(title, code, detail).toJSON(res.getOutputStream());
    }
}
