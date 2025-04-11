package it.unipd.bookly.rest.order;

import it.unipd.bookly.Resource.Message;
import it.unipd.bookly.dao.order.CancelOrderDAO;
import it.unipd.bookly.rest.AbstractRestResource;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;

/**
 * @Handles:
 * - DELETE /api/order/{orderId}/cancel → Cancel an order by ID
 */
public class CancelOrderRest extends AbstractRestResource {

    public CancelOrderRest(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super("cancel-order", req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        final String method = req.getMethod();
        final String path = req.getRequestURI();

        try {
            switch (method) {
                case "DELETE" -> {
                    if (!path.matches(".*/order/\\d+/cancel$")) {
                        sendError("Invalid path format.", "E400", "Expected path: /api/order/{orderId}/cancel", HttpServletResponse.SC_BAD_REQUEST);
                        return;
                    }

                    int orderId = extractOrderId(path);
                    new CancelOrderDAO(con, orderId).access();

                    res.setStatus(HttpServletResponse.SC_OK);
                    new Message("Order cancelled successfully", "200", "Order ID " + orderId + " has been cancelled.")
                            .toJSON(res.getOutputStream());
                }
                default -> sendError("Method not allowed", "405", "Use DELETE to cancel an order.", HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            }

        } catch (Exception ex) {
            LOGGER.error("CancelOrderRest error", ex);
            sendError("Internal server error while cancelling order", "E500", ex.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private int extractOrderId(String path) {
        // Path format: /api/order/{orderId}/cancel
        String[] parts = path.split("/");
        return Integer.parseInt(parts[parts.length - 2]);
    }

    private void sendError(String title, String code, String detail, int status) throws IOException {
        res.setStatus(status);
        new Message(title, code, detail).toJSON(res.getOutputStream());
    }
}
