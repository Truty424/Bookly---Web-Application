package it.unipd.bookly.servlet.image;

import it.unipd.bookly.LogContext;
import it.unipd.bookly.Resource.Image;
import it.unipd.bookly.dao.image.UserImageLoaderDAO;
import it.unipd.bookly.servlet.AbstractDatabaseServlet;
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

        try {
            String userName = req.getParameter("username");
            Image image = new UserImageLoaderDAO(getConnection(), userName).access().getOutputParam();
            resp.setContentType(image.getPhotoMediaType());
            resp.getOutputStream().write(image.getPhoto());
            resp.getOutputStream().flush();
            LOGGER.info("Photo {} successfully rendered.", userName);
        } catch (Exception e) {
            LOGGER.error("Unable to load the photo.", e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } finally {
            LogContext.removeIPAddress();
            LogContext.removeAction();
            LogContext.removeUser();
        }
    }
}
