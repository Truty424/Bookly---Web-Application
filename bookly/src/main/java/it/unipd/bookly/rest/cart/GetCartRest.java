package it.unipd.bookly.rest.cart;

import it.unipd.bookly.Resource.Cart;
import it.unipd.bookly.Resource.Message;
import it.unipd.bookly.dao.cart.GetCartByUserIdDAO;
import it.unipd.bookly.rest.AbstractRestResource;
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
        Message message = null;
        String userIdParam = req.getParameter("userId");

        if (userIdParam == null || userIdParam.isBlank()) {
            message = new Message("Missing 'userId' parameter.", "E400", "User ID is required.");
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            message.toJSON(res.getOutputStream());
            return;
        }

        try {
            int userId = Integer.parseInt(userIdParam);

            Cart cart = new GetCartByUserIdDAO(con, userId).access().getOutputParam();

            if (cart == null) {
                message = new Message("Cart not found.", "E404", "No cart found for user ID " + userId);
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            } else {
                res.setStatus(HttpServletResponse.SC_OK);
                cart.toJSON(res.getOutputStream()); // Assuming Cart has a toJSON method
                return;
            }

        } catch (NumberFormatException ex) {
            message = new Message("Invalid 'userId' format.", "E401", "User ID must be an integer.");
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception ex) {
            LOGGER.error("Error retrieving cart for user", ex);
            message = new Message("Internal server error.", "E500", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

        if (message != null) {
            message.toJSON(res.getOutputStream());
        }
    }
}
