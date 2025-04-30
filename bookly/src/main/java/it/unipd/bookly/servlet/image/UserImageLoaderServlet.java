package it.unipd.bookly.servlet.image;

import it.unipd.bookly.LogContext;
import it.unipd.bookly.Resource.Image;
import it.unipd.bookly.dao.image.UserImageLoaderDAO;
import it.unipd.bookly.servlet.AbstractDatabaseServlet;
import it.unipd.bookly.utilities.ServletUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;

@WebServlet(name = "UserImageLoaderServlet", value = "/load-user-img")
public class UserImageLoaderServlet extends AbstractDatabaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setAction("load-user-img");

        String username = req.getParameter("username");

        if (username == null || username.isBlank()) {
            LOGGER.warn("Missing or empty 'username' parameter.");
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing or empty 'username' parameter.");
            return;
        }

        try (Connection con = getConnection()) {
            Image image = new UserImageLoaderDAO(con, username).access().getOutputParam();

            if (image == null || image.getPhoto() == null) {
                LOGGER.warn("No profile image found for user '{}'.", username);
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Profile image not found for user: " + username);
                return;
            }

            String contentType = image.getPhotoMediaType() != null ? image.getPhotoMediaType() : "image/jpeg";
            resp.setContentType(contentType);
            resp.setHeader("Content-Disposition", "inline; filename=\"" + username + "_profile.jpg\"");

            resp.getOutputStream().write(image.getPhoto());
            resp.getOutputStream().flush();

            LOGGER.info("Served profile image for user '{}'.", username);
        } catch (Exception e) {
            LOGGER.error("Error retrieving profile image for user '{}': {}", username, e.getMessage(), e);
            ServletUtils.redirectToErrorPage(req, resp, "Unable to load user profile image.");
        } finally {
            LogContext.removeAction();
            LogContext.removeIPAddress();
            LogContext.removeUser();
        }
    }
}
