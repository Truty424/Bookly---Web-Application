package it.unipd.bookly.servlet.user;

import it.unipd.bookly.LogContext;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Handles user logout:
 * - POST /user/logout → invalidate session and redirect to home
 */
@WebServlet(name = "LogoutServlet", value = "/user/logout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setAction("logout");

        try {
            // Invalidate session if exists
            if (req.getSession(false) != null) {
                req.getSession().invalidate();
                LogContext.removeUser(); // Clear any logged user from context
            }

            // Clear Authorization header (optional)
            resp.setHeader("Authorization", "");

            // Redirect to homepage
            resp.sendRedirect(req.getContextPath() + "/");

        } finally {
            LogContext.removeAction();
            LogContext.removeIPAddress();
        }
    }

    // You could allow GET for graceful handling (optional)
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doPost(req, resp);
    }
}
