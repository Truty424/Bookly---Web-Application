package it.unipd.bookly.servlet.publisher;

import it.unipd.bookly.LogContext;
import it.unipd.bookly.Resource.Book;
import it.unipd.bookly.Resource.Publisher;
import it.unipd.bookly.dao.publisher.GetAllPublishersDAO;
import it.unipd.bookly.dao.publisher.GetBooksByPublisherDAO;
import it.unipd.bookly.servlet.AbstractDatabaseServlet;
import it.unipd.bookly.utilities.ServletUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.unipd.bookly.dao.publisher.GetPublisherByIdDAO;

/**
 * Handles GET requests for: - /publisher → show all publishers -
 * /publisher/{publisherId} → show books by specific publisher
 */
@WebServlet(name = "PublisherServlet", value = "/publisher/*")
public class PublisherServlet extends AbstractDatabaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setResource(req.getRequestURI());
        LogContext.setAction("publisherServlet");

        try {
            String path = req.getPathInfo(); // returns null, "/", or "/7"

            if (path == null || "/".equals(path)) {
                showAllPublishers(req, resp);
            } else {
                path = path.replaceAll("^/+", "").replaceAll("/+$", ""); // strip slashes
                if (path.matches("\\d+")) {
                    int publisherId = Integer.parseInt(path);
                    showBooksByPublisher(req, resp, publisherId);
                } else {
                    ServletUtils.redirectToErrorPage(req, resp, "Invalid publisher path.");
                }
            }

        } catch (Exception e) {
            LOGGER.error("PublisherServlet error: {}", e.getMessage(), e);
            ServletUtils.redirectToErrorPage(req, resp, "PublisherServlet error: " + e.getMessage());
        } finally {
            LogContext.removeAction();
            LogContext.removeResource();
        }
    }

    private void showAllPublishers(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        try (var con = getConnection()) {
            List<Publisher> publishers = new GetAllPublishersDAO(con).access().getOutputParam();
            req.setAttribute("all_publishers", publishers);
            req.getRequestDispatcher("/jsp/publisher/allPublishers.jsp").forward(req, resp);
        }
    }

    private void showBooksByPublisher(HttpServletRequest req, HttpServletResponse resp, int publisherId) throws Exception {
        List<Book> books;
        Publisher publisher;
        Map<Integer, Double> bookRatings = new HashMap<>();
        try (var con = getConnection()) {
            books = new GetBooksByPublisherDAO(con, publisherId).access().getOutputParam();
        }

        // Compute ratings for each book
        for (Book book : books) {
            try (var con = getConnection()) {
                Double rating = new it.unipd.bookly.dao.review.GetAvgRatingForBookDAO(con, book.getBookId())
                        .access()
                        .getOutputParam();
                bookRatings.put(book.getBookId(), rating);
            }
        }
        try (var con = getConnection()) {
            // Fetch publisher details (name) by publisherId
            publisher = new GetPublisherByIdDAO(con, publisherId).access().getOutputParam();
        }

        // If publisher exists, set the publisher name, else set a default message
        if (publisher != null) {
            req.setAttribute("publisher_name", publisher.getPublisherName());  // Assuming Publisher has a getName() method
        } else {
            req.setAttribute("publisher_name", "Unknown Publisher");
        }

        req.setAttribute("publisher_books", books);
        req.setAttribute("publisher_id", publisherId);
        req.setAttribute("book_ratings", bookRatings);
        req.getRequestDispatcher("/jsp/publisher/publisherBooks.jsp").forward(req, resp);
    }
}
