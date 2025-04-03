package it.unipd.bookly.rest.publisher;

import it.unipd.bookly.Resource.Message;
import it.unipd.bookly.dao.publisher.*;
import it.unipd.bookly.rest.AbstractRestResource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

public class PublisherAssignmentRest extends AbstractRestResource {

    public PublisherAssignmentRest(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super("publisher-assign", req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        String method = req.getMethod();
        String path = req.getRequestURI();
        Message message = null;

        try {
            if ("GET".equals(method) && path.matches(".*/publishers/book/\\d+")) {
                handleGetPublishersByBook(path);
            } else if ("GET".equals(method) && path.matches(".*/publishers/\\d+/books")) {
                handleGetBooksByPublisher(path);
            } else if ("GET".equals(method) && path.matches(".*/publishers/\\d+/books/count")) {
                handleCountBooksByPublisher(path);
            } else if ("POST".equals(method)) {
                handleAssignPublisherToBook();
            } else if ("DELETE".equals(method)) {
                handleRemovePublisherFromBook();
            } else {
                res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                message = new Message("Invalid method or path", "405", "Review the endpoint URL.");
                message.toJSON(res.getOutputStream());
            }

        } catch (Exception e) {
            LOGGER.error("PublisherAssignmentRest exception", e);
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            message = new Message("Server error", "500", e.getMessage());
            message.toJSON(res.getOutputStream());
        }
    }

    private void handleGetPublishersByBook(String path) throws Exception {
        int bookId = Integer.parseInt(path.substring(path.lastIndexOf("/") + 1));
        List<String> publishers = new GetPublishersByBookDAO(con, bookId).access().getOutputParam();
        res.setContentType("application/json");
        for (String name : publishers) {
            res.getWriter().write("\"" + name + "\"\n");
        }
    }

    private void handleGetBooksByPublisher(String path) throws Exception {
        int publisherId = extractIdFromPath(path);
        List<String> books = new GetBooksByPublisherDAO(con, publisherId).access().getOutputParam();
        res.setContentType("application/json");
        for (String title : books) {
            res.getWriter().write("\"" + title + "\"\n");
        }
    }

    private void handleCountBooksByPublisher(String path) throws Exception {
        int publisherId = extractIdFromPath(path);
        int count = new CountBooksByPublisherDAO(con, publisherId).access().getOutputParam();
        res.getWriter().write("{\"bookCount\": " + count + "}");
    }

    private void handleAssignPublisherToBook() throws Exception {
        int bookId = Integer.parseInt(req.getParameter("bookId"));
        int publisherId = Integer.parseInt(req.getParameter("publisherId"));
        boolean success = new AddPublisherToBookDAO(con, bookId, publisherId).access().getOutputParam();
        res.setStatus(success ? HttpServletResponse.SC_OK : HttpServletResponse.SC_BAD_REQUEST);
        new Message(success ? "Assigned" : "Failed", success ? "200" : "400").toJSON(res.getOutputStream());
    }

    private void handleRemovePublisherFromBook() throws Exception {
        int bookId = Integer.parseInt(req.getParameter("bookId"));
        int publisherId = Integer.parseInt(req.getParameter("publisherId"));
        boolean success = new RemovePublisherFromBookDAO(con, bookId, publisherId).access().getOutputParam();
        res.setStatus(success ? HttpServletResponse.SC_OK : HttpServletResponse.SC_BAD_REQUEST);
        new Message(success ? "Removed" : "Removal failed", success ? "200" : "400").toJSON(res.getOutputStream());
    }

    private int extractIdFromPath(String path) {
        String[] parts = path.split("/");
        return Integer.parseInt(parts[parts.length - 2]);
    }
}
