package it.unipd.bookly.rest.user;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.unipd.bookly.Resource.Message;
import it.unipd.bookly.Resource.User;
import it.unipd.bookly.dao.user.LoginUserDAO;
import it.unipd.bookly.dao.user.RegisterUserDAO;
import it.unipd.bookly.rest.AbstractRestResource;
import it.unipd.bookly.utilities.JWTUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;

/**
 * Handles user authentication:
 * - POST /api/user/login
 * - POST /api/user/signup
 */
public class AuthenticationUserRest extends AbstractRestResource {

    private final ObjectMapper mapper = new ObjectMapper();

    public AuthenticationUserRest(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super("user-authentication", req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        final String method = req.getMethod();
        final String path = req.getRequestURI();

        try {
            if ("POST".equalsIgnoreCase(method)) {
                if (path.endsWith("/user/login")) {
                    handleLogin();
                } else if (path.endsWith("/user/signup")) {
                    handleSignup();
                } else {
                    sendNotFound("Invalid authentication path. Use /api/user/login or /api/user/signup.");
                }
            } else {
                sendMethodNotAllowed("Only POST is supported for authentication.");
            }
        } catch (Exception e) {
            LOGGER.error("AuthenticationUserRest error", e);
            sendServerError("Internal server error during authentication.", e.getMessage());
        }
    }

    private void handleLogin() throws Exception {
        // Parse JSON input for login
        User loginRequest = mapper.readValue(req.getInputStream(), User.class);
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        if (username == null || password == null) {
            sendBadRequest("Missing credentials.", "Both username and password are required in JSON body.");
            return;
        }

        User authenticated = new LoginUserDAO(con, username, password).access().getOutputParam();

        if (authenticated != null) {
            String token = JWTUtil.generateToken(username);
            res.setStatus(HttpServletResponse.SC_OK);
            res.setContentType("application/json;charset=UTF-8");
            mapper.writeValue(res.getOutputStream(),
                    new Message("Authentication successful.", "200", token));
        } else {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            new Message("Invalid credentials.", "401", "Authentication failed.")
                    .toJSON(res.getOutputStream());
        }
    }

    private void handleSignup() throws Exception {
        // Parse JSON input for signup
        User newUser = mapper.readValue(req.getInputStream(), User.class);

        if (newUser.getUsername() == null || newUser.getEmail() == null || newUser.getPassword() == null) {
            sendBadRequest("Missing required fields", "Username, email, and password are required.");
            return;
        }

        User userCreated = new RegisterUserDAO(con, newUser).access().getOutputParam();

        if (userCreated != null) {
            res.setStatus(HttpServletResponse.SC_CREATED);
            mapper.writeValue(res.getOutputStream(),
                    new Message("User created successfully.", "201", userCreated.getUsername()));
        } else {
            res.setStatus(HttpServletResponse.SC_CONFLICT);
            new Message("Conflict", "409", "Username or email already exists.")
                    .toJSON(res.getOutputStream());
        }
    }

    // Utility methods
    private void sendBadRequest(String title, String detail) throws IOException {
        res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        new Message(title, "400", detail).toJSON(res.getOutputStream());
    }

    private void sendNotFound(String detail) throws IOException {
        res.setStatus(HttpServletResponse.SC_NOT_FOUND);
        new Message("Not Found", "404", detail).toJSON(res.getOutputStream());
    }

    private void sendMethodNotAllowed(String detail) throws IOException {
        res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        new Message("Method Not Allowed", "405", detail).toJSON(res.getOutputStream());
    }

    private void sendServerError(String title, String detail) throws IOException {
        res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        new Message(title, "500", detail).toJSON(res.getOutputStream());
    }
}
