package it.unipd.bookly.servlet.image;

import it.unipd.bookly.LogContext;
import it.unipd.bookly.Resource.Image;
import it.unipd.bookly.dao.image.BookImageLoaderDAO;
import it.unipd.bookly.servlet.AbstractDatabaseServlet;
import it.unipd.bookly.utilities.ServletUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "BookImageLoaderServlet", value = "/load-book-img")
public class BookImageLoaderServlet extends AbstractDatabaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setAction("load-book-img");

        try {
            String bookIdParam = req.getParameter("bookId");

            if (bookIdParam == null || bookIdParam.isBlank()) {
                throw new IllegalArgumentException("Missing or empty bookId parameter.");
            }

            int bookId = Integer.parseInt(bookIdParam);
            Image image = new BookImageLoaderDAO(getConnection(), bookId).access().getOutputParam();

            if (image == null || image.getPhoto() == null) {
                throw new Exception("Image not found for bookId: " + bookId);
            }

            resp.setContentType(image.getPhotoMediaType());
            resp.getOutputStream().write(image.getPhoto());
            resp.getOutputStream().flush();
            LOGGER.info("Photo for bookId {} successfully rendered.", bookId);

        } catch (Exception e) {
            LOGGER.error("Unable to load the photo: {}", e.getMessage(), e);
            ServletUtils.redirectToErrorPage(req, resp, "BookImageLoaderServlet error: " + e.getMessage());
        } finally {
            LogContext.removeIPAddress();
            LogContext.removeAction();
            LogContext.removeUser();
        }
    }
}
