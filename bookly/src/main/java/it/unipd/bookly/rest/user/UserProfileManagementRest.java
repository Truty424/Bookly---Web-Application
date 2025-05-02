package it.unipd.bookly.rest.user;

import java.io.IOException;
import java.sql.Connection;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.unipd.bookly.Resource.Image;
import it.unipd.bookly.Resource.Message;
import it.unipd.bookly.Resource.User;
import it.unipd.bookly.dao.user.UpdateUserDAO;
import it.unipd.bookly.rest.AbstractRestResource;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Handles user profile updates: - PUT /api/user/profile/{id}
 */
public class UserProfileManagementRest extends AbstractRestResource {

    private final ObjectMapper mapper = new ObjectMapper();

    public UserProfileManagementRest(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super("user-profile-management", req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        final String method = req.getMethod();
        final String path = req.getRequestURI();
        Message message;

        try {
            if ("PUT".equalsIgnoreCase(method)) {
                if (path.matches(".*/user/profile/\\d+$")) {
                    handleUpdate(path);
                } else {
                    sendNotFound("Invalid user profile update path.");
                }
            } else {
                sendMethodNotAllowed("Only PUT method is supported for user profile update.");
            }
        } catch (Exception e) {
            LOGGER.error("UserProfileManagementRest error: ", e);
            message = new Message("Internal server error.", "E500", e.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            message.toJSON(res.getOutputStream());
        }
    }

    private void handleUpdate(String path) throws Exception {
        int userId = Integer.parseInt(path.substring(path.lastIndexOf("/") + 1));

        // Parse JSON input
        JsonNode jsonNode = mapper.readTree(req.getInputStream());

        // Parse user fields
        User user = mapper.treeToValue(jsonNode.get("user"), User.class);

        // Enforce userId from path
        user.setUserId(userId);

        Boolean updated = new UpdateUserDAO(
                con,
                user.getUserId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhone(),
                user.getAddress(),
                user.getRole()
        ).access().getOutputParam();

        if (updated != null && updated) {
            res.setStatus(HttpServletResponse.SC_OK);
            mapper.writeValue(res.getOutputStream(), new Message("Profile updated successfully.", "200", null));
        } else {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            mapper.writeValue(res.getOutputStream(), new Message("User not found.", "404", "No user with ID " + userId));
        }
    }

    private void sendMethodNotAllowed(String details) throws IOException {
        res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        new Message("Method not allowed", "405", details).toJSON(res.getOutputStream());
    }

    private void sendNotFound(String details) throws IOException {
        res.setStatus(HttpServletResponse.SC_NOT_FOUND);
        new Message("Invalid path", "404", details).toJSON(res.getOutputStream());
    }
}
