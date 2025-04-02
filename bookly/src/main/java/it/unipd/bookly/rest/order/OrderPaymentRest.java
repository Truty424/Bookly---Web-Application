package it.unipd.bookly.rest.order;

import it.unipd.bookly.Resource.Message;
import it.unipd.bookly.dao.order.UpdateOrderPaymentInfoDAO;
import it.unipd.bookly.rest.AbstractRestResource;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;

/**
 * Handles PUT requests to update payment info of an order.
 * 
 * Endpoint:
 * PUT /api/order/{orderId}/payment?value=PAID
 */
public class OrderPaymentRest extends AbstractRestResource {

    public OrderPaymentRest(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super("order-payment", req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        final String method = req.getMethod();
        final String path = req.getRequestURI(); // e.g. /api/order/42/payment
        final String paymentStatus = req.getParameter("value");

        try {
            if (!"PUT".equalsIgnoreCase(method)) {
                res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                new Message("Only PUT method is allowed.", "405", "Use PUT to update payment info.")
                        .toJSON(res.getOutputStream());
                return;
            }

            if (paymentStatus == null || paymentStatus.isBlank()) {
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                new Message("Missing value parameter.", "400", "Payment status value is required.")
                        .toJSON(res.getOutputStream());
                return;
            }

            if (!path.matches(".*/order/\\d+/payment$")) {
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                new Message("Invalid endpoint path.", "404", "Use /api/order/{orderId}/payment")
                        .toJSON(res.getOutputStream());
                return;
            }

            int orderId = extractOrderId(path);
            new UpdateOrderPaymentInfoDAO(con, orderId, paymentStatus).access();

            res.setStatus(HttpServletResponse.SC_OK);
            new Message("Order payment status updated.", "200", "Order ID " + orderId + " updated to: " + paymentStatus)
                    .toJSON(res.getOutputStream());

        } catch (Exception e) {
            LOGGER.error("Error updating order payment", e);
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            new Message("Internal server error", "E500", e.getMessage())
                    .toJSON(res.getOutputStream());
        }
    }

    private int extractOrderId(String path) {
        // Expected path: /api/order/{id}/payment
        String[] parts = path.split("/");
        return Integer.parseInt(parts[parts.length - 2]);
    }
}
