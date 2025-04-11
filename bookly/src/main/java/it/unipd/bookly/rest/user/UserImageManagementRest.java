package it.unipd.bookly.rest.user;

import java.io.IOException;
import java.sql.Connection;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import it.unipd.bookly.Resource.Image;
import it.unipd.bookly.Resource.Message;
import it.unipd.bookly.dao.user.UpdateUserImageDAO;
import it.unipd.bookly.rest.AbstractRestResource;

/**
 * Handles user image management:
 * - POST /api/user/image/{id}
 */
public class UserImageManagementRest extends AbstractRestResource {

    private final ObjectMapper mapper = new ObjectMapper();

    public UserImageManagementRest(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super("user-image-management", req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        final String method = req.getMethod();
        final String path = req.getRequestURI();

        try {
            switch (method) {
                case "POST" -> {
                    if (path.matches(".*/user/image/\\d+$")) {
                        int userId = Integer.parseInt(path.substring(path.lastIndexOf("/") + 1));
                        Image image = mapper.readValue(req.getInputStream(), Image.class);
                        handleUploadImage(userId, image);
                    } else {
                        sendNotFound("Invalid image upload path. Expected format: /api/user/image/{id}");
                    }
                }
                default -> sendMethodNotAllowed("Only POST is allowed for user image uploads.");
            }
        } catch (Exception e) {
            LOGGER.error("UserImageManagementRest error", e);
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            new Message("Internal server error", "E500", e.getMessage())
                .toJSON(res.getOutputStream());
        }
    }

    private void handleUploadImage(int userId, Image image) throws Exception {
        boolean updated = new UpdateUserImageDAO(con, userId, image).access().getOutputParam();

        if (updated) {
            res.setStatus(HttpServletResponse.SC_OK);
            new Message("Image uploaded successfully.", "200", "Image updated for user ID: " + userId)
                .toJSON(res.getOutputStream());
        } else {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            new Message("User not found.", "404", "No user with ID " + userId)
                .toJSON(res.getOutputStream());
        }
    }

    private void sendNotFound(String detail) throws IOException {
        res.setStatus(HttpServletResponse.SC_NOT_FOUND);
        new Message("Not Found", "404", detail).toJSON(res.getOutputStream());
    }

    private void sendMethodNotAllowed(String detail) throws IOException {
        res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        new Message("Method Not Allowed", "405", detail).toJSON(res.getOutputStream());
    }
}
