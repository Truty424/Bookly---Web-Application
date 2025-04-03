package it.unipd.bookly.rest.order;

import it.unipd.bookly.Resource.Message;
import it.unipd.bookly.dao.order.UpdateOrderStatusDAO;
import it.unipd.bookly.dao.order.UpdateOrderPaymentInfoDAO;
import it.unipd.bookly.rest.AbstractRestResource;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;

/**
 * Handles PUT requests for updating order status or payment info.
 * 
 * Endpoints:
 * - PUT /api/order/{orderId}/status?value=SHIPPED
 * - PUT /api/order/{orderId}/payment?value=PAID
 */
public class OrderStatusRest extends AbstractRestResource {

    public OrderStatusRest(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super("order-status", req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        final String method = req.getMethod();
        final String path = req.getRequestURI(); // e.g. /api/order/42/status
        final String value = req.getParameter("value");

        try {
            if (!"PUT".equals(method) || value == null || value.isBlank()) {
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                new Message("Missing or invalid method/parameter.", "E400", "PUT method and value parameter are required.")
                        .toJSON(res.getOutputStream());
                return;
            }

            if (path.matches(".*/order/\\d+/status$")) {
                updateStatus(path, value);
            } else if (path.matches(".*/order/\\d+/payment$")) {
                updatePayment(path, value);
            } else {
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                new Message("Unsupported path.", "404", "Only /status or /payment paths are allowed.")
                        .toJSON(res.getOutputStream());
            }

        } catch (Exception e) {
            LOGGER.error("OrderStatusRest error", e);
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            new Message("Internal server error", "E500", e.getMessage())
                    .toJSON(res.getOutputStream());
        }
    }

    private void updateStatus(String path, String newStatus) throws Exception {
        int orderId = extractOrderId(path);
        new UpdateOrderStatusDAO(con, orderId, newStatus).access();

        res.setStatus(HttpServletResponse.SC_OK);
        new Message("Order status updated.", "200", "Order ID " + orderId + " set to " + newStatus)
                .toJSON(res.getOutputStream());
    }

    private void updatePayment(String path, String newPaymentStatus) throws Exception {
        int orderId = extractOrderId(path);
        new UpdateOrderPaymentInfoDAO(con, orderId, newPaymentStatus).access();

        res.setStatus(HttpServletResponse.SC_OK);
        new Message("Order payment status updated.", "200", "Order ID " + orderId + " set to " + newPaymentStatus)
                .toJSON(res.getOutputStream());
    }

    private int extractOrderId(String path) {
        // Extract order ID from path like /api/order/42/status
        String[] parts = path.split("/");
        return Integer.parseInt(parts[parts.length - 2]);
    }
}
