package it.unipd.bookly.servlet.user;

import java.io.IOException;

import it.unipd.bookly.LogContext;
import it.unipd.bookly.Resource.User;
import it.unipd.bookly.dao.user.LoginUserDAO;
import it.unipd.bookly.servlet.AbstractDatabaseServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Handles login requests for users.
 */
@WebServlet(name = "LoginServlet", value = "/login")
public class LoginServlet extends AbstractDatabaseServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setResource(req.getRequestURI());
        LogContext.setAction("login");

        try {
            String usernameOrEmail = req.getParameter("usernameOrEmail");
            String password = req.getParameter("password");

            User user = new LoginUserDAO(getConnection(), usernameOrEmail, password).access().getOutputParam();

            if (user != null) {
                HttpSession session = req.getSession();
                session.setAttribute("user", user);

                resp.sendRedirect(req.getContextPath() + "/home");
            } else {
                req.setAttribute("error", "Invalid username or password.");
                req.getRequestDispatcher("/jsp/user/login.jsp").forward(req, resp);
            }
        } catch (Exception e) {
            LOGGER.error("LoginServlet error: {}", e.getMessage());
            resp.sendRedirect("/html/error.html");
        } finally {
            LogContext.removeAction();
            LogContext.removeResource();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Redirect to the login page
        req.getRequestDispatcher("/jsp/user/login.jsp").forward(req, resp);
    }
}