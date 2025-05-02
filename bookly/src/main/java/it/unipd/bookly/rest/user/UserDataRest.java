package it.unipd.bookly.rest.user;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.unipd.bookly.Resource.Message;
import it.unipd.bookly.Resource.User;
import it.unipd.bookly.rest.AbstractRestResource;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;

import it.unipd.bookly.dao.user.GetUserByUsernameDAO;

/**
 * Handles user data retrieval:
 * - GET /api/user?username={username}
 */
public class UserDataRest extends AbstractRestResource {

    private final ObjectMapper mapper = new ObjectMapper();

    public UserDataRest(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super("user-data", req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        final String method = req.getMethod();

        try {
            if ("GET".equalsIgnoreCase(method)) {
                handleGetUserByUsername();
            } else {
                sendMethodNotAllowed("Only GET is supported for this endpoint.");
            }
        } catch (Exception e) {
            LOGGER.error("UserDataRest error: ", e);
            sendServerError("Internal server error: " + e.getMessage());
        }
    }

    private void handleGetUserByUsername() throws Exception {
        String usernameParam = req.getParameter("username");

        if (usernameParam == null || usernameParam.trim().isEmpty()) {
            sendBadRequest("Missing or empty 'username' query parameter.");
            return;
        }

        User user = new GetUserByUsernameDAO(con, usernameParam).access().getOutputParam();

        if (user != null) {
            res.setStatus(HttpServletResponse.SC_OK);
            res.setContentType("application/json;charset=UTF-8");
            mapper.writeValue(res.getOutputStream(), user);
        } else {
            sendNotFound("User with username '" + usernameParam + "' not found.");
        }
    }

    private void sendBadRequest(String detail) throws IOException {
        res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        new Message("Bad Request", "400", detail).toJSON(res.getOutputStream());
    }

    private void sendNotFound(String detail) throws IOException {
        res.setStatus(HttpServletResponse.SC_NOT_FOUND);
        new Message("Not Found", "404", detail).toJSON(res.getOutputStream());
    }

    private void sendMethodNotAllowed(String detail) throws IOException {
        res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        new Message("Method Not Allowed", "405", detail).toJSON(res.getOutputStream());
    }

    private void sendServerError(String detail) throws IOException {
        res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        new Message("Internal Server Error", "500", detail).toJSON(res.getOutputStream());
    }
}
