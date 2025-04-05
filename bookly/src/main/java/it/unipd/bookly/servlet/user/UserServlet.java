package it.unipd.bookly.servlet.user;

import it.unipd.bookly.LogContext;
import it.unipd.bookly.Resource.Image;
import it.unipd.bookly.Resource.User;
import it.unipd.bookly.auth.JwtManager;
import it.unipd.bookly.dao.user.LoginUserDAO;
import it.unipd.bookly.dao.user.RegisterUserDAO;
import it.unipd.bookly.services.user.LoginServices;
import it.unipd.bookly.services.user.RegisterServices;
import it.unipd.bookly.utilities.ErrorCode;
import it.unipd.bookly.utilities.Util;
import it.unipd.bookly.servlet.AbstractDatabaseServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "UserServlet", value = "/user/*")
@MultipartConfig
public class UserServlet extends AbstractDatabaseServlet {

    final private ErrorCode errorCode = null;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setResource(req.getRequestURI());
        LogContext.setAction("User");

        String operation = req.getRequestURI().substring(req.getRequestURI().lastIndexOf("user") + 4);

        try {
            switch (operation) {
                case "/login" -> req.getRequestDispatcher("/html/login.html").forward(req, res);
                case "/register" -> req.getRequestDispatcher("/html/signup.html").forward(req, res);
                default -> Util.writeError(res, ErrorCode.OPERATION_UNKNOWN);
            }
        } catch (Exception e) {
            Util.writeError(res, ErrorCode.INTERNAL_ERROR);
            LOGGER.error("Exception in GET /user{}", operation, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setResource(req.getRequestURI());
        LogContext.setAction("User");

        String operation = req.getRequestURI().substring(req.getRequestURI().lastIndexOf("user") + 4);

        switch (operation) {
            case "/login" -> loginOperation(req, res);
            case "/register" -> registerOperation(req, res);
            default -> Util.writeError(res, ErrorCode.OPERATION_UNKNOWN);
        }
    }

    private void loginOperation(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
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
                    res.sendRedirect(req.getContextPath() + "/profile/");
                } else {
                    Util.writeError(res, ErrorCode.INTERNAL_ERROR);
                }
            } else {
                res.setStatus(errorCode.getHTTPCode());
                req.getRequestDispatcher("/html/login.html").forward(req, res);
            }

        } catch (SQLException e) {
            LOGGER.error("Login error:", e);
            Util.writeError(res, ErrorCode.INTERNAL_ERROR);
        }
    }

    private void registerOperation(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try {
            String username = req.getParameter("username");
            String password = req.getParameter("password");
            String rePassword = req.getParameter("password_check");
            String firstName = req.getParameter("first_name");
            String lastName = req.getParameter("last_name");
            String email = req.getParameter("email");
            String phone = req.getParameter("phone");
            String address = req.getParameter("address");
            String role = req.getParameter("role") == null ? "user" : req.getParameter("role");

            byte[] imageData = req.getPart("image").getInputStream().readAllBytes();
            String contentType = req.getPart("image").getContentType();
            Image profileImage = new Image(imageData, contentType);

            User newUser = new User(username, password, firstName, lastName, email, phone, address, role);
            newUser.setProfileImage(profileImage);

            if (RegisterServices.registerValidation(newUser, rePassword, errorCode)) {
                User registeredUser = new RegisterUserDAO(getConnection(), newUser).access().getOutputParam();

                if (registeredUser != null) {
                    String jwt = JwtManager.createToken("username", registeredUser.getUsername());

                    HttpSession session = req.getSession();
                    session.setAttribute("user", registeredUser);
                    session.setAttribute("Authorization", jwt);

                    res.setHeader("Authorization", jwt);
                    res.sendRedirect(req.getContextPath() + "/profile/");
                } else {
                    Util.writeError(res, ErrorCode.INTERNAL_ERROR);
                }
            } else {
                res.setStatus(errorCode.getHTTPCode());
                req.getRequestDispatcher("/html/signup.html").forward(req, res);
            }

        } catch (Exception e) {
            LOGGER.error("Registration error:", e);
            Util.writeError(res, ErrorCode.INTERNAL_ERROR);
        }
    }
}
