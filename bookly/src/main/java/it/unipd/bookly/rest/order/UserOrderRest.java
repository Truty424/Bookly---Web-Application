package it.unipd.bookly.rest.order;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.unipd.bookly.Resource.Message;
import it.unipd.bookly.Resource.Order;
import it.unipd.bookly.dao.order.GetLatestOrderForUserDAO;
import it.unipd.bookly.dao.order.GetOrdersByUserDAO;
import it.unipd.bookly.rest.AbstractRestResource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Handles requests for retrieving user orders.
 *
 * <p>Supported operations:</p>
 * <ul>
 *   <li><strong>GET</strong> /api/user/{userId}/latest - Retrieves the latest order for the specified user.</li>
 *   <li><strong>GET</strong> /api/user/{userId} - Retrieves all orders for the specified user.</li>
 * </ul>
 */
public class UserOrderRest extends AbstractRestResource {

    public UserOrderRest(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super("user-order", req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        final String method = req.getMethod();
        final String path = req.getRequestURI();

        try {
            switch (method) {
                case "GET" -> {
                    if (path.matches(".*/user/\\d+/latest$")) {
                        handleGetLatestOrder(path);
                    } else if (path.matches(".*/user/\\d+$")) {
                        handleGetOrdersByUser(path);
                    } else {
                        sendError("Unsupported path", "404", "Expected /user/{id} or /user/{id}/latest", HttpServletResponse.SC_NOT_FOUND);
                    }
                }
                default -> sendError("Method not allowed", "405", "Only GET is supported for user orders.", HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            }
        } catch (Exception e) {
            LOGGER.error("UserOrderRest exception", e);
            sendError("Internal server error", "E500", e.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void handleGetOrdersByUser(String path) throws Exception {
        int userId = Integer.parseInt(path.substring(path.lastIndexOf("/") + 1));
        List<Order> orders = new GetOrdersByUserDAO(con, userId).access().getOutputParam();

        sendJsonResponse(orders, "Orders retrieved successfully.", "200");
    }

    private void handleGetLatestOrder(String path) throws Exception {
        String userIdStr = path.substring(path.lastIndexOf("/user/") + 6, path.lastIndexOf("/latest"));
        int userId = Integer.parseInt(userIdStr);

        Order latestOrder = new GetLatestOrderForUserDAO(con, userId).access().getOutputParam();

        if (latestOrder == null) {
            sendError("No latest order found for user.", "404", "User ID: " + userId, HttpServletResponse.SC_NOT_FOUND);
        } else {
            sendJsonResponse(latestOrder, "Latest order retrieved successfully.", "200");
        }
    }

    private void sendJsonResponse(Object data, String msg, String code) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(data);
        res.setContentType("application/json;charset=UTF-8");
        res.setStatus(HttpServletResponse.SC_OK);
        new Message(msg, code, json).toJSON(res.getOutputStream());
    }

    private void sendError(String title, String code, String detail, int status) throws IOException {
        res.setStatus(status);
        new Message(title, code, detail).toJSON(res.getOutputStream());
    }
}
