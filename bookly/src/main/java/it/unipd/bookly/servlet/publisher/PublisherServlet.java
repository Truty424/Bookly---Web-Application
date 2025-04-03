package it.unipd.bookly.servlet.publisher;

import it.unipd.bookly.LogContext;
import it.unipd.bookly.Resource.Book;
import it.unipd.bookly.Resource.Publisher;
import it.unipd.bookly.dao.publisher.GetAllPublishersDAO;
import it.unipd.bookly.dao.publisher.GetBooksByPublisherDAO;
import it.unipd.bookly.servlet.AbstractDatabaseServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Handles GET requests for:
 * - /publisher               → show all publishers
 * - /publisher/{publisherId} → show books by specific publisher
 */
@WebServlet(name = "PublisherServlet", value = "/publisher/*")
public class PublisherServlet extends AbstractDatabaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setResource(req.getRequestURI());
        LogContext.setAction("publisherServlet");

        String path = req.getRequestURI();

        try {
            if (path.matches(".*/publisher/?")) {
                showAllPublishers(req, resp);
            } else if (path.matches(".*/publisher/\\d+")) {
                showBooksByPublisher(req, resp);
            } else {
                resp.sendRedirect("/html/error.html");
            }
        } catch (Exception e) {
            LOGGER.error("PublisherServlet error: {}", e.getMessage());
            resp.sendRedirect("/html/error.html");
        } finally {
            LogContext.removeAction();
            LogContext.removeResource();
        }
    }

    private void showAllPublishers(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        List<Publisher> publishers = new GetAllPublishersDAO(getConnection()).access().getOutputParam();
        req.setAttribute("all_publishers", publishers);
        req.getRequestDispatcher("/jsp/publisher/allPublishers.jsp").forward(req, resp);
    }

    private void showBooksByPublisher(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String[] segments = req.getRequestURI().split("/");
        int publisherId = Integer.parseInt(segments[segments.length - 1]);

        List<Book> books = new GetBooksByPublisherDAO(getConnection(), publisherId).access().getOutputParam();
        req.setAttribute("publisher_books", books);
        req.setAttribute("publisher_id", publisherId);
        req.getRequestDispatcher("/jsp/publisher/publisherBooks.jsp").forward(req, resp);
    }
}
