package it.unipd.bookly.rest.order;

import it.unipd.bookly.Resource.Message;
import it.unipd.bookly.Resource.Order;
import it.unipd.bookly.dao.order.GetAllOrdersDAO;
import it.unipd.bookly.dao.order.GetOrderByIdDAO;
import it.unipd.bookly.dao.order.InsertOrderDAO;
import it.unipd.bookly.rest.AbstractRestResource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

public class OrderRest extends AbstractRestResource {

    public OrderRest(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super("order", req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        final String method = req.getMethod();
        final String path = req.getRequestURI();
        Message message = null;

        try {
            if ("GET".equals(method) && path.matches(".*/order/\\d+")) {
                handleGetOrderById(path);
            } else if ("GET".equals(method) && path.endsWith("/orders")) {
                handleGetAllOrders();
            } else if ("POST".equals(method) && path.endsWith("/order")) {
                handleInsertOrder();
            } else {
                res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                new Message("Unsupported HTTP method or path.", "405", "Use GET, POST on /order or /orders")
                        .toJSON(res.getOutputStream());
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
            order.toJSON(res.getOutputStream());
        }
    }

    private void handleGetAllOrders() throws Exception {
        List<Order> orders = new GetAllOrdersDAO(con).access().getOutputParam();

        res.setContentType("application/json;charset=UTF-8");
        res.setStatus(HttpServletResponse.SC_OK);
        for (Order order : orders) {
            order.toJSON(res.getOutputStream());
        }
    }

    private void handleInsertOrder() throws Exception {
        Order order = Order.fromJSON(req.getInputStream());
        int id = new InsertOrderDAO(con, order).access().getOutputParam();

        res.setStatus(HttpServletResponse.SC_CREATED);
        message = new Message("Order created", "201", "Order created with ID " + id);
        message.toJSON(res.getOutputStream());
    }
}
