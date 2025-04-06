package it.unipd.bookly.rest.user;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unipd.bookly.Resource.Message;
import it.unipd.bookly.dao.user.UpdateUserImageDAO;
import it.unipd.bookly.rest.AbstractRestResource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

/**
 * Handles user image management:
 * - POST /api/user/image/{id}
 */
public class UserImageManagementRest extends AbstractRestResource {

    public UserImageManagementRest(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super("user-image-management", req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        String method = req.getMethod();
        String path = req.getRequestURI();

        try {
            if ("POST".equals(method) && path.matches(".*/user/image/\\d+$")) {
                handleUploadImage(path);
            } else {
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                new Message("Invalid user image management path.", "404", "Check supported routes.")
                    .toJSON(res.getOutputStream());
            }

        } catch (Exception e) {
            LOGGER.error("UserImageManagementRest error: ", e);
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            new Message("Internal server error.", "E500", e.getMessage()).toJSON(res.getOutputStream());
        }
    }

    private void handleUploadImage(String path) throws Exception {
        int userId = Integer.parseInt(path.substring(path.lastIndexOf("/") + 1));
        Part filePart = req.getPart("image"); // Assuming image is passed as a part

        if (filePart == null) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            new Message("No image uploaded.", "400", "Please upload an image.").toJSON(res.getOutputStream());
            return;
        }

        String filePath = "/path/to/upload/folder/" + userId + "_image.jpg"; // Replace with your actual file path
        filePart.write(filePath);

        UpdateUserImageDAO updateImageDAO = new UpdateUserImageDAO(con, userId, filePath);
        boolean updated = updateImageDAO.updateUserImage();

        if (updated) {
            res.setStatus(HttpServletResponse.SC_OK);
            new Message("Image uploaded successfully.", "200", "User image uploaded").toJSON(res.getOutputStream());
        } else {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            new Message("User not found.", "404", "No user with ID " + userId).toJSON(res.getOutputStream());
        }
    }
}
