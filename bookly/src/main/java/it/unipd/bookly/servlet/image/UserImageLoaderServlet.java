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

@WebServlet(name = "UserImageLoaderServlet", value = "/load-user-img")
public class UserImageLoaderServlet extends AbstractDatabaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setAction("load-user-img");

        try {
            String userName = req.getParameter("username");

            if (userName == null || userName.isBlank()) {
                throw new IllegalArgumentException("Missing or empty username parameter.");
            }

            Image image = new UserImageLoaderDAO(getConnection(), userName).access().getOutputParam();

            if (image == null || image.getPhoto() == null) {
                throw new Exception("No image found for username: " + userName);
            }

            resp.setContentType(image.getPhotoMediaType());
            resp.getOutputStream().write(image.getPhoto());
            resp.getOutputStream().flush();

            LOGGER.info("Photo for user '{}' successfully rendered.", userName);

        } catch (Exception e) {
            LOGGER.error("Unable to load user photo: {}", e.getMessage(), e);
            ServletUtils.redirectToErrorPage(req, resp, "UserImageLoaderServlet error: " + e.getMessage());
        } finally {
            LogContext.removeIPAddress();
            LogContext.removeAction();
            LogContext.removeUser();
        }
    }
}
