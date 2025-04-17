package it.unipd.bookly.rest.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.unipd.bookly.Resource.Message;
import it.unipd.bookly.Resource.Order;
import it.unipd.bookly.dao.order.*;
import it.unipd.bookly.rest.AbstractRestResource;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

/**
 * REST endpoint for handling orders.
 * Supported routes:
 * - GET /api/order/{id}         → get order with books
 * - GET /api/orders             → get all orders
 * - POST /api/order             → insert a new order
 */
public class OrderRest extends AbstractRestResource {

    public OrderRest(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super("order", req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        final String method = req.getMethod();
        final String path = req.getRequestURI();

        try {
            switch (method) {
                case "GET" -> {
                    if (path.matches(".*/order/\\d+$")) {
                        handleGetOrderById(path);
                    } else if (path.endsWith("/orders")) {
                        handleGetAllOrders();
                    } else {
                        sendMethodNotAllowed("GET path not supported. Use /order/{id} or /orders");
                    }
                }

                case "POST" -> {
                    if (path.endsWith("/order")) {
                        handleInsertOrder();
                    } else {
                        sendMethodNotAllowed("POST path not supported. Use /order");
                    }
                }

                default ->
                    sendMethodNotAllowed("Only GET and POST are supported for /order endpoints.");
            }
        } catch (Exception e) {
            LOGGER.error("OrderRest exception", e);
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            new Message("Internal server error.", "500", e.getMessage()).toJSON(res.getOutputStream());
        }
    }

    private void handleGetOrderById(String path) throws Exception {
        int orderId = Integer.parseInt(path.substring(path.lastIndexOf("/") + 1));
        Order order = new GetOrderByIdDAO(con, orderId).access().getOutputParam();

        if (order == null) {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            new Message("Order not found", "404", "No order with ID " + orderId).toJSON(res.getOutputStream());
        } else {
            res.setContentType("application/json;charset=UTF-8");
            res.setStatus(HttpServletResponse.SC_OK);
            new ObjectMapper().writeValue(res.getOutputStream(), order);
        }
    }

    private void handleGetAllOrders() throws Exception {
        List<Order> orders = new GetAllOrdersDAO(con).access().getOutputParam();
        res.setContentType("application/json;charset=UTF-8");
        res.setStatus(HttpServletResponse.SC_OK);
        new ObjectMapper().writeValue(res.getOutputStream(), orders);
    }

    private void handleInsertOrder() throws Exception {
        Order order = new ObjectMapper().readValue(req.getInputStream(), Order.class);
        boolean success = new InsertOrderDAO(con, order).access().getOutputParam();

        if (success) {
            res.setStatus(HttpServletResponse.SC_CREATED);
            new Message("Order inserted successfully.", "201", "Order for user " + order.getOrderId() + " created.")
                    .toJSON(res.getOutputStream());
        } else {
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            new Message("Failed to insert order.", "500", "Insert operation failed.")
                    .toJSON(res.getOutputStream());
        }
    }

    private void sendMethodNotAllowed(String detail) throws IOException {
        res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        new Message("Method Not Allowed", "405", detail).toJSON(res.getOutputStream());
    }
}
