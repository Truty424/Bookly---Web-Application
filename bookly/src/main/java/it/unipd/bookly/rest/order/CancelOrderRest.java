package it.unipd.bookly.rest.order;

import it.unipd.bookly.Resource.Message;
import it.unipd.bookly.dao.order.CancelOrderDAO;
import it.unipd.bookly.rest.AbstractRestResource;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;

/**
 * Handles DELETE requests to cancel a specific order.
 * 
 * Endpoint:
 * DELETE /api/order/{orderId}/cancel
 */
public class CancelOrderRest extends AbstractRestResource {

    public CancelOrderRest(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super("cancel-order", req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        String method = req.getMethod();
        String path = req.getRequestURI();
        Message message;

        try {
            if (!"DELETE".equalsIgnoreCase(req.getMethod())) {
                res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                new Message("Method not allowed", "405", "Use DELETE to cancel an order.")
                        .toJSON(res.getOutputStream());
                return;
            }

            if (!path.matches(".*/order/\\d+/cancel$")) {
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                new Message("Invalid path", "400", "Expected format: /api/order/{orderId}/cancel")
                        .toJSON(res.getOutputStream());
                return;
            }

            int orderId = extractOrderId(path);
            new CancelOrderDAO(con, orderId).access();

            res.setStatus(HttpServletResponse.SC_OK);
            new Message("Order cancelled successfully", "200", "Order ID " + orderId + " has been cancelled.")
                    .toJSON(res.getOutputStream());

        } catch (Exception ex) {
            LOGGER.error("Error cancelling order", ex);
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            message = new Message("Failed to cancel order", "E500", ex.getMessage());
            message.toJSON(res.getOutputStream());
        }
    }

    private int extractOrderId(String path) {
        // Path format: /api/order/{orderId}/cancel
        String[] parts = path.split("/");
        return Integer.parseInt(parts[parts.length - 2]);
    }
}
