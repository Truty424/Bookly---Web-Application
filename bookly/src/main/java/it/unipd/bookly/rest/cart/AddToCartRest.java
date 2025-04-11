package it.unipd.bookly.rest.cart;

import it.unipd.bookly.Resource.Message;
import it.unipd.bookly.dao.cart.AddBookToCartDAO;
import it.unipd.bookly.rest.AbstractRestResource;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;

public class AddToCartRest extends AbstractRestResource {

    public AddToCartRest(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super("add-to-cart", req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        final String method = req.getMethod();

        switch (method) {
            case "POST" -> handleAddToCart();
            default -> sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED,
                    "Unsupported method", "E405", "Only POST is allowed for this endpoint.");
        }
    }

    private void handleAddToCart() throws IOException {
        String cartIdParam = req.getParameter("cartId");
        String bookIdParam = req.getParameter("bookId");

        if (cartIdParam == null || bookIdParam == null) {
            sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters", "E400",
                    "'cartId' and 'bookId' are required.");
            return;
        }

        try {
            int cartId = Integer.parseInt(cartIdParam);
            int bookId = Integer.parseInt(bookIdParam);

            new AddBookToCartDAO(con, bookId, cartId).access();

            res.setStatus(HttpServletResponse.SC_OK);
            new Message("Book added to cart successfully", "200",
                    "Book ID " + bookId + " was added to cart ID " + cartId).toJSON(res.getOutputStream());

        } catch (NumberFormatException e) {
            sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid ID format", "E401",
                    "'cartId' and 'bookId' must be valid integers.");
        } catch (Exception e) {
            LOGGER.error("Error adding book to cart", e);
            sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server error", "E500",
                    "Error adding book to cart: " + e.getMessage());
        }
    }

    private void sendError(int status, String title, String code, String detail) throws IOException {
        res.setStatus(status);
        new Message(title, code, detail).toJSON(res.getOutputStream());
    }
}
