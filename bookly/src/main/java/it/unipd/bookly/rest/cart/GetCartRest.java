package it.unipd.bookly.rest.cart;

import it.unipd.bookly.Resource.Cart;
import it.unipd.bookly.Resource.Message;
import it.unipd.bookly.dao.cart.GetCartByUserIdDAO;
import it.unipd.bookly.rest.AbstractRestResource;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;

/**
 * REST endpoint to retrieve a user's cart.
 * Endpoint: GET /api/cart?userId=123
 */
public class GetCartRest extends AbstractRestResource {

    public GetCartRest(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super("get-cart", req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        final String method = req.getMethod();

        switch (method) {
            case "GET" -> handleGetCart();
            default -> sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED,
                    "Unsupported HTTP method", "E405", "Only GET is supported.");
        }
    }

    private void handleGetCart() throws IOException {
        String userIdParam = req.getParameter("userId");

        if (userIdParam == null || userIdParam.isBlank()) {
            sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Missing 'userId' parameter", "E400", "User ID is required.");
            return;
        }

        try {
            int userId = Integer.parseInt(userIdParam);
            Cart cart = new GetCartByUserIdDAO(con, userId).access().getOutputParam();

            if (cart == null) {
                sendError(HttpServletResponse.SC_NOT_FOUND,
                        "Cart not found", "E404", "No cart found for user ID " + userId);
            } else {
                res.setStatus(HttpServletResponse.SC_OK);
                res.setContentType("application/json;charset=UTF-8");
                new ObjectMapper().writeValue(res.getOutputStream(), cart);
            }

        } catch (NumberFormatException ex) {
            sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid userId format", "E401", "'userId' must be a valid integer.");
        } catch (Exception ex) {
            LOGGER.error("Error retrieving cart for user", ex);
            sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Internal server error", "E500", ex.getMessage());
        }
    }

    private void sendError(int status, String title, String code, String detail) throws IOException {
        res.setStatus(status);
        new Message(title, code, detail).toJSON(res.getOutputStream());
    }
}
