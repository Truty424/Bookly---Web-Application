package it.unipd.bookly.servlet.user;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import it.unipd.bookly.LogContext;
import it.unipd.bookly.Resource.Image;
import it.unipd.bookly.Resource.Order;
import it.unipd.bookly.Resource.User;
import it.unipd.bookly.auth.JwtManager;
import it.unipd.bookly.dao.order.GetOrdersByUserDAO;
import it.unipd.bookly.dao.user.ChangeUserPasswordDAO;
import it.unipd.bookly.dao.user.LoginUserDAO;
import it.unipd.bookly.dao.user.RegisterUserDAO;
import it.unipd.bookly.dao.user.UpdateUserDAO;
import it.unipd.bookly.services.user.LoginServices;
import it.unipd.bookly.services.user.RegisterServices;
import it.unipd.bookly.servlet.AbstractDatabaseServlet;
import it.unipd.bookly.utilities.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "UserServlet", value = "/user/*")
@MultipartConfig
public class UserServlet extends AbstractDatabaseServlet {

    private final ErrorCode errorCode = null;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setResource(req.getRequestURI());
        LogContext.setAction("UserServlet");

        String operation = req.getRequestURI().substring(req.getRequestURI().lastIndexOf("user") + 4);
        LOGGER.info("GET operation: {}", operation);

        try {
            switch (operation) {
                case "/login" ->
                    req.getRequestDispatcher("/jsp/user/login.jsp").forward(req, res);
                case "/register" ->
                    req.getRequestDispatcher("/jsp/user/signUp.jsp").forward(req, res);
                case "/changePassword" ->
                    req.getRequestDispatcher("/jsp/user/changePassword.jsp").forward(req, res);
                case "/profile" ->
                    showUserProfile(req, res);
                case "/editUserProfile" ->
                    req.getRequestDispatcher("/jsp/user/editUserProfile.jsp").forward(req, res);
                default ->
                    writeError(res, ErrorCode.OPERATION_UNKNOWN);
            }
        } catch (IOException e) {
            LOGGER.error("IOException during GET: ", e);
            writeError(res, ErrorCode.INTERNAL_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setResource(req.getRequestURI());
        LogContext.setAction("UserServlet");

        String operation = req.getRequestURI().substring(req.getRequestURI().lastIndexOf("user") + 4);
        LOGGER.info("POST operation: {}", operation);

        switch (operation) {
            case "/login" ->
                handleLogin(req, res);
            case "/register" ->
                handleRegister(req, res);
            case "/changePassword" ->
                handlePasswordChange(req, res);
            case "/editUserProfile" ->
                handleUpdate(req, res);
            default ->
                writeError(res, ErrorCode.OPERATION_UNKNOWN);
        }
    }

    private void handlePasswordChange(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            res.sendRedirect(req.getContextPath() + "/user/login");
            return;
        }

        User user = (User) session.getAttribute("user");
        int userId = user.getUserId();
        String newPassword = req.getParameter("password");

        if (newPassword == null || newPassword.trim().isEmpty()) {
            req.setAttribute("error_message", "Password cannot be empty.");
            res.sendRedirect(req.getContextPath() + "/jsp/user/login.jsp");
            return;
        }
        try {
            boolean changed = new ChangeUserPasswordDAO(getConnection(), userId, newPassword).access().getOutputParam();
            if (changed) {
                req.setAttribute("success_message", "Password updated successfully.");
                res.sendRedirect(req.getContextPath() + "/jsp/user/login.jsp");
            } else {
                req.setAttribute("error_message", "Password update failed.");
                req.getRequestDispatcher("/jsp/user/changePassword.jsp").forward(req, res);
            }
        } catch (Exception e) {
            LOGGER.error("Password update error: {}", e.getMessage());
            req.setAttribute("error_message", "Internal error during password update.");
            req.getRequestDispatcher("/jsp/user/changePassword.jsp").forward(req, res);
        }
    }

    private void handleLogin(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        try {
            String username = req.getParameter("username");
            String password = req.getParameter("password");
            if (LoginServices.loginValidation(username, password, errorCode)) {
                User user = new LoginUserDAO(getConnection(), username, password).access().getOutputParam();
                if (user != null) {
                    String jwt = JwtManager.createToken("username", user.getUsername());
                    HttpSession session = req.getSession();
                    session.setAttribute("user", user);
                    session.setAttribute("userId", user.getUserId());
                    session.setAttribute("Authorization", jwt);
                    res.setHeader("Authorization", jwt);

                    if ("admin".equalsIgnoreCase(user.getRole())) {
                        res.sendRedirect(req.getContextPath() + "/admin/dashboard");
                    } else {
                        res.sendRedirect(req.getContextPath() + "/user/profile");
                    }
                } else {
                    req.setAttribute("error_message", "Invalid username or password.");
                    req.getRequestDispatcher("/jsp/user/login.jsp").forward(req, res);
                }
            } else {
                req.setAttribute("error_message", "Login validation failed.");
                req.getRequestDispatcher("/jsp/user/login.jsp").forward(req, res);
            }
        } catch (SQLException e) {
            LOGGER.error("SQLException during login: ", e);
            writeError(res, ErrorCode.INTERNAL_ERROR);
        }
    }

    private void handleRegister(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        try {
            String username = req.getParameter("username");
            String firstName = req.getParameter("firstName");
            String lastName = req.getParameter("lastName");
            String password = req.getParameter("password");
            String email = req.getParameter("email");
            String phone = req.getParameter("phone");
            String address = req.getParameter("address");

            Image image = null;

            User newUser = new User(username, password, firstName, lastName, email, phone, address, "user", image);

            if (RegisterServices.registerValidation(newUser, errorCode)) {
                User registered = new RegisterUserDAO(getConnection(), newUser).access().getOutputParam();
                if (registered != null) {
                    String jwt = JwtManager.createToken("username", registered.getUsername());
                    HttpSession session = req.getSession();
                    session.setAttribute("user", registered);
                    session.setAttribute("userId", registered.getUserId());
                    session.setAttribute("Authorization", jwt);
                    res.setHeader("Authorization", jwt);

                    if ("admin".equalsIgnoreCase(registered.getRole())) {
                        res.sendRedirect(req.getContextPath() + "/admin/dashboard");
                    } else {
                        res.sendRedirect(req.getContextPath() + "/user/profile");
                    }
                } else {
                    req.setAttribute("error_message", "Failed to register user.");
                    req.getRequestDispatcher("/jsp/user/signUp.jsp").forward(req, res);
                }
            } else {
                req.setAttribute("error_message", "Validation failed: Passwords must match and be valid.");
                req.getRequestDispatcher("/jsp/user/signUp.jsp").forward(req, res);
            }

        } catch (SQLException e) {
            LOGGER.error("Registration error: ", e);
            writeError(res, ErrorCode.INTERNAL_ERROR);
        }
    }

    private void handleUpdate(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            res.sendRedirect(req.getContextPath() + "/user/login");
            return;
        }

        User user = (User) session.getAttribute("user");
        int userId = user.getUserId();
        String role = user.getRole();

        String newUserName = req.getParameter("username");
        String newFirstName = req.getParameter("firstName");
        String newLastName = req.getParameter("lastName");
        String newEmail = req.getParameter("email");
        String newPhone = req.getParameter("phone");
        String newAddress = req.getParameter("address");

        try {
            boolean success = new UpdateUserDAO(
                    getConnection(),
                    userId,
                    newUserName,
                    newFirstName,
                    newLastName,
                    newEmail,
                    newPhone,
                    newAddress,
                    role
            ).access().getOutputParam();

            if (success) {
                // Update session user
                User updatedUser = new User(
                        userId,
                        newUserName,
                        user.getPassword(),
                        newFirstName,
                        newLastName,
                        newEmail,
                        newPhone,
                        newAddress,
                        role,
                        user.getProfileImage()
                );

                session.setAttribute("user", updatedUser);
                req.setAttribute("success_message", "Profile updated successfully.");
                res.sendRedirect(req.getContextPath() + "/user/profile");
            } else {
                req.setAttribute("error_message", "Failed to update profile.");
                req.getRequestDispatcher("/jsp/user/editUserProfile.jsp").forward(req, res);
            }

        } catch (Exception e) {
            LOGGER.error("Error updating user profile: {}", e.getMessage());
            req.setAttribute("error_message", "Internal server error while updating profile.");
            req.getRequestDispatcher("/jsp/user/editUserProfile.jsp").forward(req, res);
        }
    }

    private void showUserProfile(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            res.sendRedirect(req.getContextPath() + "/user/login");
            return;
        }

        User user = (User) session.getAttribute("user");
        req.setAttribute("user", user);

        if ("admin".equalsIgnoreCase(user.getRole())) {
            res.sendRedirect(req.getContextPath() + "/admin/dashboard");
        } else {
            try {
                List<Order> orders = new GetOrdersByUserDAO(getConnection(), user.getUserId()).access().getOutputParam();
                req.setAttribute("user_orders", orders);
            } catch (Exception e) {
                LOGGER.error("Error loading user orders: {}", e.getMessage(), e);
                req.setAttribute("order_error", "Unable to load order history.");
            }

            req.getRequestDispatcher("/jsp/user/userProfile.jsp").forward(req, res);
        }
    }

}
