package it.unipd.bookly.servlet.order;

import it.unipd.bookly.LogContext;
import it.unipd.bookly.Resource.Order;
import it.unipd.bookly.Resource.User;
import it.unipd.bookly.dao.order.GetOrdersByUserDAO;
import it.unipd.bookly.servlet.AbstractDatabaseServlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

@WebServlet(name = "OrderServlet", value = "/user/orders")
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
            req.getRequestDispatcher("/jsp/user/userOrders.jsp").forward(req, res);
        } catch (Exception e) {
            LOGGER.error("Failed to load user orders: {}", e.getMessage(), e);
            res.sendRedirect(req.getContextPath() + "/html/error.html");
        } finally {
            LogContext.removeAction();
            LogContext.removeResource();
        }
    }
}
