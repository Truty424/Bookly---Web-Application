package it.unipd.bookly.servlet.book;

import it.unipd.bookly.LogContext;
import it.unipd.bookly.Resource.Image;
import it.unipd.bookly.dao.image.BookImageLoaderDAO;
import it.unipd.bookly.servlet.AbstractDatabaseServlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;

@WebServlet(name = "BookImageServlet", value = "/book/image/*")
public class BookImageServlet extends AbstractDatabaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setResource(req.getRequestURI());
        LogContext.setAction("BookImageServlet");

        String pathInfo = req.getPathInfo(); // e.g. /23

        if (pathInfo == null || pathInfo.length() <= 1) {
            LOGGER.warn("Missing book ID in path.");
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing book ID.");
            return;
        }

        try {
            int bookId = Integer.parseInt(pathInfo.substring(1));
            LOGGER.info("Requesting image for book ID {}", bookId);

            try (Connection con = getConnection()) {
                Image image = new BookImageLoaderDAO(con, bookId).access().getOutputParam();

                if (image != null && image.getPhoto() != null) {
                    res.setContentType(image.getPhotoMediaType());
                    res.getOutputStream().write(image.getPhoto());
                    LOGGER.info("Image streamed for book ID {}", bookId);
                } else {
                    LOGGER.warn("No image found for book ID {}", bookId);
                    res.sendError(HttpServletResponse.SC_NOT_FOUND, "No image found for book.");
                }
            }

        } catch (NumberFormatException e) {
            LOGGER.error("Invalid book ID format: {}", pathInfo, e);
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid book ID format.");
        } catch (Exception e) {
            LOGGER.error("Error loading book image: {}", e.getMessage(), e);
            res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to load image.");
        } finally {
            LogContext.removeAction();
            LogContext.removeResource();
        }
    }
}
