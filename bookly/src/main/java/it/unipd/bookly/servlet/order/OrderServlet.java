package it.unipd.bookly.servlet.order;

import it.unipd.bookly.LogContext;
import it.unipd.bookly.Resource.Order;
import it.unipd.bookly.Resource.User;
import it.unipd.bookly.dao.order.CancelOrderDAO;
import it.unipd.bookly.dao.order.GetOrdersByUserDAO;
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

        String action = req.getParameter("action");
        String orderIdParam = req.getParameter("orderId");

        if ("cancel".equalsIgnoreCase(action) && orderIdParam != null) {
            try (Connection con = getConnection()) {
                int orderId = Integer.parseInt(orderIdParam);
                boolean cancelled = new CancelOrderDAO(con, orderId).access().getOutputParam();

                if (cancelled) {
                    LOGGER.info("Order {} cancelled successfully.", orderId);
                    res.sendRedirect(req.getContextPath() + "/orders");
                } else {
                    LOGGER.warn("Order cancellation failed for ID {}", orderId);
                    ServletUtils.redirectToErrorPage(req, res, "Unable to cancel order.");
                }

            } catch (Exception e) {
                LOGGER.error("Error cancelling order: {}", e.getMessage(), e);
                ServletUtils.redirectToErrorPage(req, res, "An error occurred while cancelling your order.");
            }
        } else {
            ServletUtils.redirectToErrorPage(req, res, "Invalid order operation.");
        }

        LogContext.removeAction();
        LogContext.removeResource();
    }
}
