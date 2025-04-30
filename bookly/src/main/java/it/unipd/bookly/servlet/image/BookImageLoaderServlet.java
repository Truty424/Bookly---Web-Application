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
import java.sql.Connection;

@WebServlet(name = "BookImageLoaderServlet", value = "/load-book-img")
public class BookImageLoaderServlet extends AbstractDatabaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setAction("load-book-img");

        String bookIdParam = req.getParameter("bookId");

        if (bookIdParam == null || bookIdParam.isBlank()) {
            LOGGER.warn("Missing 'bookId' parameter.");
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing 'bookId' parameter.");
            return;
        }

        try {
            int bookId = Integer.parseInt(bookIdParam);

            try (Connection con = getConnection()) {
                Image image = new BookImageLoaderDAO(con, bookId).access().getOutputParam();

                if (image == null || image.getPhoto() == null) {
                    LOGGER.warn("No image found for bookId {}", bookId);
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "No image available for this book.");
                    return;
                }

                String mediaType = image.getPhotoMediaType() != null ? image.getPhotoMediaType() : "image/jpeg";
                resp.setContentType(mediaType);
                resp.setHeader("Content-Disposition", "inline; filename=\"book_" + bookId + ".jpg\"");
                resp.getOutputStream().write(image.getPhoto());
                resp.getOutputStream().flush();

                LOGGER.info("Image served successfully for bookId {}", bookId);
            }

        } catch (NumberFormatException e) {
            LOGGER.warn("Invalid 'bookId' format: {}", bookIdParam);
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid 'bookId' format.");
        } catch (Exception e) {
            LOGGER.error("Error loading book image for ID '{}': {}", bookIdParam, e.getMessage(), e);
            ServletUtils.redirectToErrorPage(req, resp, "Unable to load image for book.");
        } finally {
            LogContext.removeAction();
            LogContext.removeIPAddress();
            LogContext.removeUser();
        }
    }
}
