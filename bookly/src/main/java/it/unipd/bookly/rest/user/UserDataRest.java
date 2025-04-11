package it.unipd.bookly.rest.user;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.unipd.bookly.Resource.Message;
import it.unipd.bookly.Resource.User;
import it.unipd.bookly.dao.user.UpdateUserDAO;
import it.unipd.bookly.rest.AbstractRestResource;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;

import it.unipd.bookly.dao.user.GetUserByUsernameDAO;

/**
 * Handles user data retrieval and updates: - GET /api/user/{id} - PUT
 * /api/user/{id}
 */
public class UserDataRest extends AbstractRestResource {

    private final ObjectMapper mapper = new ObjectMapper();

    public UserDataRest(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super("user-data", req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        final String method = req.getMethod();
        final String path = req.getRequestURI();

        try {
            switch (method) {
                case "GET" -> {
                    if (path.matches(".*/user/\\d+$")) {
                        handleGetUserByUsername();
                    } else {
                        sendNotFound("Invalid path for GET request.");
                    }
                }
                case "PUT" -> {
                    if (path.matches(".*/user/\\d+$")) {
                        handleUpdateUser(path);
                    } else {
                        sendNotFound("Invalid path for PUT request.");
                    }
                }
                default ->
                    sendMethodNotAllowed("Only GET and PUT are supported.");
            }
        } catch (Exception e) {
            LOGGER.error("UserDataRest error: ", e);
            sendServerError("Internal server error: " + e.getMessage());
        }
    }

    private void handleGetUserByUsername() throws Exception {
        String usernameParam = req.getParameter("username");
        User username = new GetUserByUsernameDAO(con, usernameParam).access().getOutputParam();

        if (username != null) {
            res.setStatus(HttpServletResponse.SC_OK);
            res.setContentType("application/json;charset=UTF-8");
            mapper.writeValue(res.getOutputStream(), username);
        } else {
            sendNotFound("User with username " + username + " not found.");
        }
    }

    private void handleUpdateUser(String path) throws Exception {
        int userId = extractIdFromPath(path);
        String newEmail = req.getParameter("email");
        String newPhone = req.getParameter("phone");
        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");
        String address = req.getParameter("address");
        String role = req.getParameter("role");

        if (newEmail == null || newPhone == null || firstName == null || lastName == null || address == null || role == null) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            new Message("Missing parameters", "400", "All fields (email, phone, firstName, lastName, address, role) are required.")
                    .toJSON(res.getOutputStream());
            return;
        }

        Boolean updated = new UpdateUserDAO(
                con,
                userId,
                firstName,
                lastName,
                newEmail,
                newPhone,
                address,
                role
        ).access().getOutputParam();

        if (Boolean.TRUE.equals(updated)) {
            res.setStatus(HttpServletResponse.SC_OK);
            new Message("User updated successfully.", "200", "User ID " + userId + " updated.")
                    .toJSON(res.getOutputStream());
        } else {
            sendNotFound("User with ID " + userId + " not found.");
        }
    }

    private int extractIdFromPath(String path) {
        return Integer.parseInt(path.substring(path.lastIndexOf("/") + 1));
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
