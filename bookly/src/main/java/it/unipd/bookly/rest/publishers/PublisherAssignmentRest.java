package it.unipd.bookly.rest.publishers;

import it.unipd.bookly.Resource.Book;
import it.unipd.bookly.Resource.Message;
import it.unipd.bookly.Resource.Publisher;
import it.unipd.bookly.dao.publisher.*;
import it.unipd.bookly.rest.AbstractRestResource;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

/**
 * Handles:
 * - GET    /api/publisher/book/{bookId} → get publishers for a book
 * - GET    /api/publisher/{publisherId}/books → get books by publisher
 * - GET    /api/publisher/{publisherId}/books/count → get count of books by publisher
 * - POST   → assign publisher to a book (params: bookId, publisherId)
 * - DELETE → remove publisher from a book (params: bookId, publisherId)
 */
public class PublisherAssignmentRest extends AbstractRestResource {

    public PublisherAssignmentRest(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super("publisher-assign", req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        final String method = req.getMethod();
        final String path = req.getRequestURI();

        try {
            switch (method) {
                case "GET" -> {
                    if (path.matches(".*/publisher/book/\\d+$")) {
                        handleGetPublishersByBook(path);
                    } else if (path.matches(".*/publisher/\\d+/books$")) {
                        handleGetBooksByPublisher(path);
                    } else if (path.matches(".*/publisher/\\d+/books/count$")) {
                        handleCountBooksByPublisher(path);
                    } else {
                        sendError("Invalid GET path.", "404", "Unsupported GET endpoint.");
                    }
                }

                case "POST" -> handleAssignPublisherToBook();

                case "DELETE" -> handleRemovePublisherFromBook();

                default -> sendError("Unsupported HTTP method.", "405", "Allowed methods: GET, POST, DELETE.");
            }

        } catch (Exception e) {
            LOGGER.error("PublisherAssignmentRest error", e);
            sendError("Internal server error", "500", e.getMessage());
        }
    }

    private void handleGetPublishersByBook(String path) throws Exception {
        int bookId = extractIdFromPath(path);
        List<Publisher> publishers = new GetPublishersByBookDAO(con, bookId).access().getOutputParam();

        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < publishers.size(); i++) {
            json.append("\"").append(publishers.get(i).getPublisherName()).append("\"");
            if (i < publishers.size() - 1) json.append(", ");
        }
        json.append("]");

        sendJson(json.toString());
    }

    private void handleGetBooksByPublisher(String path) throws Exception {
        int publisherId = extractIdFromPath(path);
        List<Book> books = new GetBooksByPublisherDAO(con, publisherId).access().getOutputParam();

        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < books.size(); i++) {
            json.append("\"").append(books.get(i)).append("\"");
            if (i < books.size() - 1) json.append(", ");
        }
        json.append("]");

        sendJson(json.toString());
    }

    private void handleCountBooksByPublisher(String path) throws Exception {
        int publisherId = extractIdFromPath(path);
        int count = new CountBooksByPublisherDAO(con, publisherId).access().getOutputParam();
        sendJson("{\"bookCount\": " + count + "}");
    }

    private void handleAssignPublisherToBook() throws Exception {
        int bookId = Integer.parseInt(req.getParameter("bookId"));
        int publisherId = Integer.parseInt(req.getParameter("publisherId"));

        boolean success = new AddPublisherToBookDAO(con, bookId, publisherId).access().getOutputParam();
        sendMessage(success ? "Assigned" : "Failed to assign", success ? "200" : "400", null, success ? HttpServletResponse.SC_OK : HttpServletResponse.SC_BAD_REQUEST);
    }

    private void handleRemovePublisherFromBook() throws Exception {
        int bookId = Integer.parseInt(req.getParameter("bookId"));
        int publisherId = Integer.parseInt(req.getParameter("publisherId"));

        boolean success = new RemovePublisherFromBookDAO(con, bookId, publisherId).access().getOutputParam();
        sendMessage(success ? "Removed" : "Failed to remove", success ? "200" : "400", null, success ? HttpServletResponse.SC_OK : HttpServletResponse.SC_BAD_REQUEST);
    }

    private int extractIdFromPath(String path) {
        String[] parts = path.split("/");
        for (int i = parts.length - 1; i >= 0; i--) {
            try {
                return Integer.parseInt(parts[i]);
            } catch (NumberFormatException ignored) {}
        }
        return -1;
    }

    private void sendJson(String body) throws IOException {
        res.setStatus(HttpServletResponse.SC_OK);
        res.setContentType("application/json;charset=UTF-8");
        res.getWriter().write(body);
    }

    private void sendMessage(String title, String code, String detail, int status) throws IOException {
        res.setStatus(status);
        new Message(title, code, detail).toJSON(res.getOutputStream());
    }

    private void sendError(String title, String code, String detail) throws IOException {
        sendMessage(title, code, detail, HttpServletResponse.SC_BAD_REQUEST);
    }
}
