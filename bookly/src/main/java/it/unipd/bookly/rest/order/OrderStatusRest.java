package it.unipd.bookly.rest.order;

import it.unipd.bookly.Resource.Message;
import it.unipd.bookly.dao.order.UpdateOrderStatusDAO;
import it.unipd.bookly.dao.order.UpdateOrderPaymentInfoDAO;
import it.unipd.bookly.rest.AbstractRestResource;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Timestamp;

public class OrderStatusRest extends AbstractRestResource {

    public OrderStatusRest(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super("order-status", req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        final String method = req.getMethod();
        final String path = req.getRequestURI();
        final String value = req.getParameter("value");
        final String amountParam = req.getParameter("amount");
        final String methodParam = req.getParameter("method");

        try {
            if (!"PUT".equalsIgnoreCase(method) || value == null || value.isBlank()) {
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                new Message("Missing or invalid method/parameter.", "E400", "PUT method and 'value' parameter are required.")
                        .toJSON(res.getOutputStream());
                return;
            }

            if (path.matches(".*/order/\\d+/status$")) {
                updateStatus(path, value);
            } else if (path.matches(".*/order/\\d+/payment$")) {
                if (amountParam == null || methodParam == null) {
                    res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    new Message("Missing payment details.", "E400", "Both 'amount' and 'method' parameters are required for payment.")
                            .toJSON(res.getOutputStream());
                    return;
                }
                double amount = Double.parseDouble(amountParam);
                updatePayment(path, amount, methodParam, value);
            } else {
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                new Message("Unsupported path.", "404", "Only /status or /payment paths are supported.")
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
        new Message("Order status updated.", "200", "Order ID " + orderId + " set to: " + newStatus)
                .toJSON(res.getOutputStream());
    }

    private void updatePayment(String path, double amount, String paymentMethod, String paymentStatus) throws Exception {
        int orderId = extractOrderId(path);
        Timestamp paymentDate = new Timestamp(System.currentTimeMillis());

        new UpdateOrderPaymentInfoDAO(con, orderId, amount, paymentMethod, paymentDate).access();

        res.setStatus(HttpServletResponse.SC_OK);
        new Message("Order payment info updated.", "200",
                "Order ID " + orderId + " paid " + amount + " via " + paymentMethod + " - status: " + paymentStatus)
                .toJSON(res.getOutputStream());
    }

    private int extractOrderId(String path) {
        String[] parts = path.split("/");
        return Integer.parseInt(parts[parts.length - 2]);
    }
}
