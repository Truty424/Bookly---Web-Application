package it.unipd.bookly.rest.cart;

import it.unipd.bookly.Resource.Message;
import it.unipd.bookly.dao.cart.GetCartDAO;
import it.unipd.bookly.model.Cart;
import it.unipd.bookly.rest.AbstractRestResource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;

/**
 * REST endpoint to get a user's shopping cart.
 * 
 * Endpoint: GET /api/cart?userId=123
 */
public class GetCartRest extends AbstractRestResource {

    public GetCartRest(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super("get-cart", req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        String userIdParam = req.getParameter("userId");

        Message message;

        if (userIdParam == null) {
            message = new Message("Missing 'userId' parameter.", "E400", "User ID is required.");
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            message.toJSON(res.getOutputStream());
            return;
        }

        try {
            int userId = Integer.parseInt(userIdParam);

            // DAO call to get the cart
            Cart cart = new GetCartDAO(con, userId).access();

            if (cart == null) {
                message = new Message("Cart not found.", "E404", "No cart found for user ID " + userId);
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            } else {
                message = new Message("Cart retrieved successfully.", "200", cart);
                res.setStatus(HttpServletResponse.SC_OK);
            }
            
            message.toJSON(res.getOutputStream());

        } catch (NumberFormatException ex) {
            message = new Message("Invalid 'userId' format.", "E401", "User ID must be an integer.");
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            message.toJSON(res.getOutputStream());
        } catch (Exception ex) {
            LOGGER.error("Error retrieving cart", ex);
            message = new Message("Internal server error while retrieving cart.", "E500", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            message.toJSON(res.getOutputStream());
        }
    }
}