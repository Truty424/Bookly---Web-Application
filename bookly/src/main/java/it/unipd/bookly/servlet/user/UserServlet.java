package it.unipd.bookly.servlet.user;

import it.unipd.bookly.LogContext;
import it.unipd.bookly.Resource.Order;
import it.unipd.bookly.Resource.User;
import it.unipd.bookly.auth.JwtManager;
import it.unipd.bookly.dao.order.GetOrdersByUserDAO;
import it.unipd.bookly.dao.user.*;
import it.unipd.bookly.services.user.LoginServices;
import it.unipd.bookly.services.user.RegisterServices;
import it.unipd.bookly.servlet.AbstractDatabaseServlet;
import it.unipd.bookly.utilities.ErrorCode;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

import it.unipd.bookly.Resource.Image;

@WebServlet(name = "UserServlet", value = "/user/*")
@MultipartConfig
public class UserServlet extends AbstractDatabaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setResource(req.getRequestURI());
        LogContext.setAction("UserServlet");

        String action = req.getPathInfo();

        try {
            switch (action) {
                case "/login" ->
                    forward(req, res, "/jsp/user/login.jsp");
                case "/register" ->
                    forward(req, res, "/jsp/user/signUp.jsp");
                case "/changePassword" ->
                    forward(req, res, "/jsp/user/changePassword.jsp");
                case "/profile" ->
                    showProfile(req, res);
                case "/editUserProfile" ->
                    forward(req, res, "/jsp/user/editUserProfile.jsp");
                default ->
                    writeError(res, ErrorCode.OPERATION_UNKNOWN);
            }
        } finally {
            LogContext.removeAction();
            LogContext.removeResource();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setResource(req.getRequestURI());
        LogContext.setAction("UserServlet");

        String action = req.getPathInfo();

        try {
            switch (action) {
                case "/login" ->
                    handleLogin(req, res);
                case "/register" ->
                    handleRegister(req, res);
                case "/changePassword" ->
                    changePassword(req, res);
                case "/editUserProfile" ->
                    updateProfile(req, res);
                case "/uploadProfileImage" ->
                    uploadProfileImage(req, res);
                default ->
                    writeError(res, ErrorCode.OPERATION_UNKNOWN);
            }
        } finally {
            LogContext.removeAction();
            LogContext.removeResource();
        }
    }

    private void forward(HttpServletRequest req, HttpServletResponse res, String path) throws ServletException, IOException {
        req.getRequestDispatcher(path).forward(req, res);
    }

    private void showProfile(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
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
            try (Connection con = getConnection()) {
                List<Order> orders = new GetOrdersByUserDAO(con, user.getUserId()).access().getOutputParam();
                req.setAttribute("user_orders", orders);
            } catch (Exception e) {
                LOGGER.error("Failed to fetch user orders: {}", e.getMessage(), e);
                req.setAttribute("order_error", "Unable to load your orders.");
            }

            forward(req, res, "/jsp/user/userProfile.jsp");
        }
    }

    private void handleLogin(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        try (Connection con = getConnection()) {
            if (LoginServices.loginValidation(username, password, null)) {
                User user = new LoginUserDAO(con, username, password).access().getOutputParam();
                if (user != null) {
                    HttpSession session = req.getSession();
                    String token = JwtManager.createToken("username", user.getUsername());

                    session.setAttribute("user", user);
                    session.setAttribute("userId", user.getUserId());
                    session.setAttribute("Authorization", token);
                    res.setHeader("Authorization", token);

                    res.sendRedirect(req.getContextPath() + (user.getRole().equalsIgnoreCase("admin") ? "/admin/dashboard" : "/user/profile"));
                    return;
                }
            }

            req.setAttribute("error_message", "Invalid credentials.");
            forward(req, res, "/jsp/user/login.jsp");

        } catch (Exception e) {
            LOGGER.error("Login failed: {}", e.getMessage(), e);
            writeError(res, ErrorCode.INTERNAL_ERROR);
        }
    }

    private void handleRegister(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");

        User user = new User(username, password, firstName, lastName, email, phone, address, "user", null);

        try (Connection con = getConnection()) {
            if (RegisterServices.registerValidation(user, null)) {
                User registered = new RegisterUserDAO(con, user).access().getOutputParam();
                if (registered != null) {
                    HttpSession session = req.getSession();
                    String token = JwtManager.createToken("username", registered.getUsername());

                    session.setAttribute("user", registered);
                    session.setAttribute("userId", registered.getUserId());
                    session.setAttribute("Authorization", token);
                    res.setHeader("Authorization", token);

                    res.sendRedirect(req.getContextPath() + "/user/profile");
                    return;
                }
            }

            req.setAttribute("error_message", "Registration failed or validation error.");
            forward(req, res, "/jsp/user/signUp.jsp");

        } catch (Exception e) {
            LOGGER.error("Registration failed: {}", e.getMessage(), e);
            writeError(res, ErrorCode.INTERNAL_ERROR);
        }
    }

    private void changePassword(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            res.sendRedirect(req.getContextPath() + "/user/login");
            return;
        }

        User user = (User) session.getAttribute("user");
        String newPassword = req.getParameter("password");

        if (newPassword == null || newPassword.trim().isEmpty()) {
            req.setAttribute("error_message", "Password cannot be empty.");
            forward(req, res, "/jsp/user/changePassword.jsp");
            return;
        }

        try (Connection con = getConnection()) {
            boolean updated = new ChangeUserPasswordDAO(con, user.getUserId(), newPassword).access().getOutputParam();

            if (updated) {
                session.invalidate(); // force re-login
                res.sendRedirect(req.getContextPath() + "/user/login");
            } else {
                req.setAttribute("error_message", "Failed to update password.");
                forward(req, res, "/jsp/user/changePassword.jsp");
            }

        } catch (Exception e) {
            LOGGER.error("Password update failed: {}", e.getMessage(), e);
            forward(req, res, "/jsp/user/changePassword.jsp");
        }
    }

    private void updateProfile(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            res.sendRedirect(req.getContextPath() + "/user/login");
            return;
        }

        User user = (User) session.getAttribute("user");

        String username = req.getParameter("username");
        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");

        try (Connection con = getConnection()) {
            boolean updated = new UpdateUserDAO(
                    con,
                    user.getUserId(),
                    username,
                    firstName,
                    lastName,
                    email,
                    phone,
                    address,
                    user.getRole()
            ).access().getOutputParam();

            if (updated) {
                user.setUsername(username);
                user.setFirst_name(firstName);
                user.setLast_name(lastName);
                user.setEmail(email);
                user.setPhone(phone);
                user.setAddress(address);
                session.setAttribute("user", user);
                res.sendRedirect(req.getContextPath() + "/user/profile");
            } else {
                req.setAttribute("error_message", "Failed to update profile.");
                forward(req, res, "/jsp/user/editUserProfile.jsp");
            }

        } catch (Exception e) {
            LOGGER.error("Update failed: {}", e.getMessage(), e);
            forward(req, res, "/jsp/user/editUserProfile.jsp");
        }

    }

    private void uploadProfileImage(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            res.sendRedirect(req.getContextPath() + "/user/login");
            return;
        }

        User user = (User) session.getAttribute("user");
        Part filePart = req.getPart("profileImage");

        if (filePart == null || filePart.getSize() == 0) {
            req.setAttribute("error_message", "No file selected.");
            forward(req, res, "/jsp/user/editUserProfile.jsp");
            return;
        }

        try (Connection con = getConnection()) {
            byte[] imageBytes = filePart.getInputStream().readAllBytes();
            String contentType = filePart.getContentType();
            Image image = new Image(imageBytes, contentType);

            boolean updated = new UpdateUserImageIfExistsDAO(con, user.getUserId(), image).access().getOutputParam();

            if (updated) {
                req.setAttribute("success_message", "Profile image updated successfully!");
            } else {
                req.setAttribute("error_message", "Failed to update profile image.");
            }

            forward(req, res, "/jsp/user/editUserProfile.jsp");

        } catch (Exception e) {
            LOGGER.error("Failed to upload profile image: {}", e.getMessage(), e);
            req.setAttribute("error_message", "An error occurred while uploading.");
            forward(req, res, "/jsp/user/editUserProfile.jsp");
        }
    }

}
