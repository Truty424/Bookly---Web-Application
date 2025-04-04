package it.unipd.bookly.servlet.user;

import java.io.IOException;

import it.unipd.bookly.LogContext;
import it.unipd.bookly.Resource.User;
import it.unipd.bookly.dao.user.RegisterUserDAO;
import it.unipd.bookly.servlet.AbstractDatabaseServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Handles user registration requests.
 */
@WebServlet(name = "RegisterServlet", value = "/register")
public class RegisterServlet extends AbstractDatabaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/jsp/user/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setResource(req.getRequestURI());
        LogContext.setAction("register");

        try {
            String username = req.getParameter("username");
            String password = req.getParameter("password");
            String firstName = req.getParameter("firstName");
            String lastName = req.getParameter("lastName");
            String email = req.getParameter("email");
            String phone = req.getParameter("phone");
            String address = req.getParameter("address");

            User user = new User(username, password, firstName, lastName, email, phone, address, "user");

            User registeredUser = new RegisterUserDAO(getConnection(), user).access().getOutputParam();

            if (registeredUser != null) {
                resp.sendRedirect(req.getContextPath() + "/login");
            } else {
                req.setAttribute("error", "Registration failed. Please try again.");
                req.getRequestDispatcher("/jsp/user/register.jsp").forward(req, resp);
            }
        } catch (Exception e) {
            LOGGER.error("RegisterServlet error: {}", e.getMessage());
            resp.sendRedirect("/html/error.html");
        } finally {
            LogContext.removeAction();
            LogContext.removeResource();
        }
    }
}