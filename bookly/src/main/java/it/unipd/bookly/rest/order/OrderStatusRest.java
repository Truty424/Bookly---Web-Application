package it.unipd.bookly.rest.order;

import it.unipd.bookly.Resource.Message;
import it.unipd.bookly.dao.order.UpdateOrderPaymentInfoDAO;
import it.unipd.bookly.dao.order.UpdateOrderStatusDAO;
import it.unipd.bookly.rest.AbstractRestResource;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Timestamp;

/**
 * @Handles:
 * - PUT /api/order/{orderId}/status?value=STATUS
 * - PUT /api/order/{orderId}/payment?value=PAID&amount=99.99&method=CARD
 */
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
            switch (method) {
                case "PUT" -> {
                    if (value == null || value.isBlank()) {
                        sendError("Missing value parameter.", "E400", "'value' is required for status/payment updates.", HttpServletResponse.SC_BAD_REQUEST);
                        return;
                    }

                    if (path.matches(".*/order/\\d+/status$")) {
                        int orderId = extractOrderId(path);
                        new UpdateOrderStatusDAO(con, orderId, value).access();
                        sendMessage("Order status updated.", "200", "Order ID " + orderId + " set to: " + value);
                    } else if (path.matches(".*/order/\\d+/payment$")) {
                        if (amountParam == null || methodParam == null) {
                            sendError("Missing payment details.", "E400", "Both 'amount' and 'method' parameters are required.", HttpServletResponse.SC_BAD_REQUEST);
                            return;
                        }
                        int orderId = extractOrderId(path);
                        double amount = Double.parseDouble(amountParam);
                        Timestamp now = new Timestamp(System.currentTimeMillis());
                        new UpdateOrderPaymentInfoDAO(con, orderId, amount, methodParam, now).access();

                        sendMessage("Order payment info updated.", "200",
                                "Order ID " + orderId + " paid " + amount + " via " + methodParam + " - status: " + value);
                    } else {
                        sendError("Unsupported path.", "404", "Only /status or /payment paths are supported.", HttpServletResponse.SC_NOT_FOUND);
                    }
                }
                default -> sendError("Method not allowed", "405", "Only PUT is supported for this endpoint.", HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            }

        } catch (NumberFormatException e) {
            sendError("Invalid number format.", "E400", "'amount' must be a valid number.", HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            LOGGER.error("OrderStatusRest error", e);
            sendError("Internal server error", "E500", e.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private int extractOrderId(String path) {
        String[] parts = path.split("/");
        return Integer.parseInt(parts[parts.length - 2]);
    }

    private void sendMessage(String title, String code, String detail) throws IOException {
        res.setStatus(HttpServletResponse.SC_OK);
        new Message(title, code, detail).toJSON(res.getOutputStream());
    }

    private void sendError(String title, String code, String detail, int status) throws IOException {
        res.setStatus(status);
        new Message(title, code, detail).toJSON(res.getOutputStream());
    }
}
