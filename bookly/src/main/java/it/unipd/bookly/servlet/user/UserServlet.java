package it.unipd.bookly.servlet.user;

import java.io.IOException;
import java.sql.SQLException;

import it.unipd.bookly.LogContext;
import it.unipd.bookly.Resource.Image;
import it.unipd.bookly.Resource.User;
import it.unipd.bookly.auth.JwtManager;
import it.unipd.bookly.dao.user.ChangeUserPasswordDAO;
import it.unipd.bookly.dao.user.LoginUserDAO;
import it.unipd.bookly.dao.user.RegisterUserDAO;
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
        String newPassword = req.getParameter("new_password");

        try {
            boolean changed = new ChangeUserPasswordDAO(getConnection(), userId, newPassword).access().getOutputParam();
            if (changed) {
                req.setAttribute("success_message", "Password updated successfully.");
            } else {
                req.setAttribute("error_message", "Password update failed.");
            }
            req.getRequestDispatcher("/jsp/user/changePassword.jsp").forward(req, res);
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
            // try {
            //     if (req.getPart("image") != null && req.getPart("image").getSize() > 0) {
            //         byte[] imageData = req.getPart("image").getInputStream().readAllBytes();
            //         image = new Image(imageData, req.getPart("image").getContentType());
            //     }
            // } catch (ServletException | IOException e) {
            //     LOGGER.error("Image error: ", e);
            //     writeError(res, ErrorCode.INTERNAL_ERROR);
            // }

            User newUser = new User(username, password, firstName, lastName, email, phone, address, "user", image);

            if (RegisterServices.registerValidation(newUser, errorCode)) {
                User registered = new RegisterUserDAO(getConnection(), newUser).access().getOutputParam();
                if (registered != null) {
                    String jwt = JwtManager.createToken("username", registered.getUsername());
                    HttpSession session = req.getSession();
                    session.setAttribute("user", registered);
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

    private void showUserProfile(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            req.setAttribute("user", user);

            if ("admin".equalsIgnoreCase(user.getRole())) {
                res.sendRedirect(req.getContextPath() + "/admin/dashboard");
            } else {
                req.getRequestDispatcher("/jsp/user/userProfile.jsp").forward(req, res);
            }
        } else {
            res.sendRedirect(req.getContextPath() + "/user/login");
        }
    }

}
