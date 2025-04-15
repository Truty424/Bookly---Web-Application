package it.unipd.bookly.servlet.order;

import it.unipd.bookly.LogContext;
import it.unipd.bookly.dao.order.CancelOrderDAO;
import it.unipd.bookly.servlet.AbstractDatabaseServlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;

@WebServlet(name = "CancelOrderServlet", value = "/user/orders/cancel")
public class CancelOrderServlet extends AbstractDatabaseServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setResource(req.getRequestURI());
        LogContext.setAction("CancelOrder");

        try {
            int orderId = Integer.parseInt(req.getParameter("orderId"));

            try (Connection con = getConnection()) {
                Boolean result = new CancelOrderDAO(con, orderId).access().getOutputParam();
                if (Boolean.TRUE.equals(result)) {
                    res.sendRedirect(req.getContextPath() + "/user/orders?cancel=success");
                } else {
                    res.sendRedirect(req.getContextPath() + "/user/orders?cancel=failed");
                }
            }

        } catch (Exception e) {
            LOGGER.error("Error cancelling order: {}", e.getMessage());
            res.sendRedirect(req.getContextPath() + "/html/error.html");
        } finally {
            LogContext.removeAction();
            LogContext.removeResource();
        }
    }
}
