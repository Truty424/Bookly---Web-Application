package it.unipd.bookly.rest.order;

import it.unipd.bookly.Resource.Message;
import it.unipd.bookly.Resource.Order;
import it.unipd.bookly.dao.order.GetLatestOrderForUserDAO;
import it.unipd.bookly.dao.order.GetOrdersByUserDAO;
import it.unipd.bookly.rest.AbstractRestResource;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;

/**
 * Handles requests related to user-specific orders.
 * 
 * Supported endpoints:
 * - GET /api/order/user/{userId}
 * - GET /api/order/user/{userId}/latest
 */

public class UserOrderRest extends AbstractRestResource {

    public UserOrderRest(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super("user-order", req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        final String method = req.getMethod();
        final String path = req.getRequestURI(); // e.g., /api/order/user/5 or /api/order/user/5/latest
        Message message = null;

        try {
            if ("GET".equals(method) && path.matches(".*/user/\\d+/latest$")) {
                handleGetLatestOrder(path);
            } else if ("GET".equals(method) && path.matches(".*/user/\\d+$")) {
                handleGetOrdersByUser(path);
            } else {
                res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                message = new Message("Unsupported HTTP method or path.", "405", "Only GET is supported for user orders.");
                message.toJSON(res.getOutputStream());
            }
        } catch (Exception e) {
            LOGGER.error("UserOrderRest exception", e);
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            new Message("Internal server error", "E500", e.getMessage()).toJSON(res.getOutputStream());
        }
    }

    private void handleGetOrdersByUser(String path) throws Exception {
        int userId = Integer.parseInt(path.substring(path.lastIndexOf("/") + 1));
        List<Order> orders = new GetOrdersByUserDAO(con, userId).access().getOutputParam();

        res.setContentType("application/json;charset=UTF-8");
        res.setStatus(HttpServletResponse.SC_OK);
        for (Order o : orders) {
            o.toJSON(res.getOutputStream());
        }
    }

    private void handleGetLatestOrder(String path) throws Exception {
        String userIdStr = path.substring(path.lastIndexOf("/user/") + 6, path.lastIndexOf("/latest"));
        int userId = Integer.parseInt(userIdStr);

        Order latestOrder = new GetLatestOrderForUserDAO(con, userId).access().getOutputParam();

        if (latestOrder == null) {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            new Message("No latest order found for user.", "404", "User ID: " + userId).toJSON(res.getOutputStream());
            return;
        }

        res.setContentType("application/json;charset=UTF-8");
        res.setStatus(HttpServletResponse.SC_OK);
        latestOrder.toJSON(res.getOutputStream());
    }
}
