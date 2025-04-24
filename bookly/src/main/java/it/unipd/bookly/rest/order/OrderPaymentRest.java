package it.unipd.bookly.rest.order;

import it.unipd.bookly.Resource.Message;
import it.unipd.bookly.dao.order.UpdateOrderPaymentInfoDAO;
import it.unipd.bookly.rest.AbstractRestResource;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Timestamp;

/**
 * Handles requests for updating the payment status and amount of an order.
 */
public class OrderPaymentRest extends AbstractRestResource {

    public OrderPaymentRest(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super("order-payment", req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        final String method = req.getMethod();
        final String path = req.getRequestURI();
        final String paymentStatus = req.getParameter("value");
        final String amountParam = req.getParameter("amount");

        try {
            switch (method) {
                case "PUT" -> {
                    // Validate path format
                    if (!path.matches(".*/order/\\d+/payment$")) {
                        sendError("Invalid endpoint path.", "404", "Expected: /api/order/{orderId}/payment", HttpServletResponse.SC_NOT_FOUND);
                        return;
                    }

                    // Validate query parameters
                    if (paymentStatus == null || paymentStatus.isBlank() || amountParam == null) {
                        sendError("Missing required parameters.", "400", "Query params 'value' and 'amount' are required.", HttpServletResponse.SC_BAD_REQUEST);
                        return;
                    }

                    // Parse and update payment info
                    int orderId = extractOrderId(path);
                    double amount = Double.parseDouble(amountParam);
                    Timestamp now = new Timestamp(System.currentTimeMillis());

                    new UpdateOrderPaymentInfoDAO(con, orderId, amount, paymentStatus, now).access();

                    res.setStatus(HttpServletResponse.SC_OK);
                    new Message("Order payment status updated.", "200",
                            "Order ID " + orderId + " updated to '" + paymentStatus + "' with amount: " + amount)
                            .toJSON(res.getOutputStream());
                }

                default -> sendError("Method not allowed", "405", "Only PUT is supported for this endpoint.", HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            }
        } catch (NumberFormatException e) {
            sendError("Invalid amount format.", "E400", "Amount must be a numeric value.", HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            LOGGER.error("Error updating order payment", e);
            sendError("Internal server error", "E500", e.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private int extractOrderId(String path) {
        // Expected format: /api/order/{id}/payment
        String[] parts = path.split("/");
        return Integer.parseInt(parts[parts.length - 2]);
    }

    private void sendError(String title, String code, String detail, int status) throws IOException {
        res.setStatus(status);
        new Message(title, code, detail).toJSON(res.getOutputStream());
    }
}
