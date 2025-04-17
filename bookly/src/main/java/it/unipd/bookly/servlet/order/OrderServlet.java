package it.unipd.bookly.servlet.order;

import it.unipd.bookly.LogContext;
import it.unipd.bookly.Resource.Cart;
import it.unipd.bookly.Resource.Order;
import it.unipd.bookly.Resource.User;
import it.unipd.bookly.dao.cart.ClearCartDAO;
import it.unipd.bookly.dao.cart.DeleteCartByUserIdDAO;
import it.unipd.bookly.dao.cart.GetCartByUserIdDAO;
import it.unipd.bookly.dao.cart.LinkOrderToCartDAO;
import it.unipd.bookly.dao.order.CancelOrderDAO;
import it.unipd.bookly.dao.order.GetLatestOrderForUserDAO;
import it.unipd.bookly.dao.order.GetOrdersByUserDAO;
import it.unipd.bookly.dao.order.InsertOrderDAO;
import it.unipd.bookly.servlet.AbstractDatabaseServlet;
import it.unipd.bookly.utilities.ServletUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

@WebServlet(name = "OrderServlet", value = "/orders/*")
public class OrderServlet extends AbstractDatabaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setResource(req.getRequestURI());
        LogContext.setAction("OrderServlet");

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            res.sendRedirect(req.getContextPath() + "/user/login");
            return;
        }

        User user = (User) session.getAttribute("user");

        try (Connection con = getConnection()) {
            List<Order> orders = new GetOrdersByUserDAO(con, user.getUserId()).access().getOutputParam();
            req.setAttribute("orders", orders);
            req.getRequestDispatcher("/jsp/order/userOrders.jsp").forward(req, res);
        } catch (Exception e) {
            LOGGER.error("Failed to load user orders: {}", e.getMessage(), e);
            req.setAttribute("error_message", "Unable to load your orders.");
            req.getRequestDispatcher("/jsp/order/userOrders.jsp").forward(req, res);
        } finally {
            LogContext.removeAction();
            LogContext.removeResource();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setResource(req.getRequestURI());
        LogContext.setAction("OrderServlet");

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            res.sendRedirect(req.getContextPath() + "/user/login");
            return;
        }

        User user = (User) session.getAttribute("user");

        try {
            Cart cart;
            try (Connection con = getConnection()) {
                cart = new GetCartByUserIdDAO(con, user.getUserId()).access().getOutputParam();

            }

            if (cart == null || cart.getCartId() == 0) {
                ServletUtils.redirectToErrorPage(req, res, "No cart available to place an order.");
                return;
            }
            Double finalTotal = (Double) req.getSession().getAttribute("cart_final_price");
            if (finalTotal == null) finalTotal = cart.getTotalPrice();
            Order order = new Order();
            order.setTotalPrice(finalTotal);
            order.setPaymentMethod("credit_card");
            order.setStatus("placed");
            order.setAddress("Default Address");
            order.setShipmentCode("DEFAULT_SHIP");

            boolean success;
            try (Connection con = getConnection()) {
                success = new InsertOrderDAO(con, order).access().getOutputParam();
            }

            if (!success) {
                ServletUtils.redirectToErrorPage(req, res, "Failed to insert order.");
                return;
            }

            int orderId = order.getOrderId();
            int cartId = cart.getCartId();

            try (Connection con = getConnection()) {
                new LinkOrderToCartDAO(con, cart.getCartId(), orderId).access();
            }

            try (Connection con = getConnection()) {
                new ClearCartDAO(con, cartId).access();
            }

            res.sendRedirect(req.getContextPath() + "/orders");

        } catch (Exception e) {
            LOGGER.error("Error placing order: {}", e.getMessage(), e);
            ServletUtils.redirectToErrorPage(req, res, "An error occurred while placing the order.");
        } finally {
            LogContext.removeAction();
            LogContext.removeResource();
        }
    }
}
