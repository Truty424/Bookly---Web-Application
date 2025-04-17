package it.unipd.bookly.servlet.order;

import it.unipd.bookly.LogContext;
import it.unipd.bookly.Resource.Cart;
import it.unipd.bookly.Resource.Order;
import it.unipd.bookly.Resource.User;
import it.unipd.bookly.dao.cart.ClearCartDAO;
import it.unipd.bookly.dao.cart.GetCartByUserIdDAO;
import it.unipd.bookly.dao.cart.LinkOrderToCartDAO;
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

        try {
            User user = (User) session.getAttribute("user");
            List<Order> orders = new GetOrdersByUserDAO(getConnection(), user.getUserId()).access().getOutputParam();
            req.setAttribute("orders", orders);
            req.getRequestDispatcher("/jsp/order/userOrders.jsp").forward(req, res);
        } catch (Exception e) {
            LOGGER.error("OrderServlet GET error: {}", e.getMessage(), e);
            ServletUtils.redirectToErrorPage(req, res, "Failed to load orders.");
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

        try {
            User user = (User) session.getAttribute("user");
            Cart cart = getCartByUser(getConnection(), user.getUserId());

            if (cart == null || cart.getCartId() == 0) {
                ServletUtils.redirectToErrorPage(req, res, "No cart available to place an order.");
                return;
            }

            Order order = buildOrder(cart,req);
            boolean success = insertOrder(getConnection(), order);

            if (!success) {
                ServletUtils.redirectToErrorPage(req, res, "Failed to insert order.");
                return;
            }

            linkOrderToCart(getConnection(), cart.getCartId(), order.getOrderId());
            clearCart(getConnection(), cart.getCartId());

            res.sendRedirect(req.getContextPath() + "/orders");
        } catch (Exception e) {
            LOGGER.error("OrderServlet POST error: {}", e.getMessage(), e);
            ServletUtils.redirectToErrorPage(req, res, "Error placing the order.");
        } finally {
            LogContext.removeAction();
            LogContext.removeResource();
        }
    }

    // ================================
    // 🔽 DAO/Utility Functions
    // ================================
    private Cart getCartByUser(Connection con, int userId) throws Exception {
        return new GetCartByUserIdDAO(con, userId).access().getOutputParam();
    }

    private boolean insertOrder(Connection con, Order order) throws Exception {
        return new InsertOrderDAO(con, order).access().getOutputParam();
    }

    private void linkOrderToCart(Connection con, int cartId, int orderId) throws Exception {
        new LinkOrderToCartDAO(con, cartId, orderId).access();
    }

    private void clearCart(Connection con, int cartId) throws Exception {
        new ClearCartDAO(con, cartId).access();
    }

    private Order buildOrder(Cart cart, HttpServletRequest req) {
        Double finalTotal = (Double) req.getSession().getAttribute("cart_final_price");
        if (finalTotal == null) {
            finalTotal = cart.getTotalPrice();
        }

        Order order = new Order();
        order.setTotalPrice(finalTotal);
        order.setPaymentMethod("credit_card");
        order.setStatus("placed");
        order.setAddress("Default Address");
        order.setShipmentCode("DEFAULT_SHIP");
        return order;
    }
}
