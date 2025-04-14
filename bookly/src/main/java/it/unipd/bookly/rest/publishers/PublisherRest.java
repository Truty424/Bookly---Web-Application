package it.unipd.bookly.rest.publishers;

import com.fasterxml.jackson.databind.ObjectMapper;
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
 * Handles: - GET /api/publishers → get all publishers - GET
 * /api/publishers/{id} → get a specific publisher by ID - POST /api/publishers
 * → insert a new publisher - PUT /api/publishers/{id} → update an existing
 * publisher - DELETE /api/publishers/{id} → delete a publisher by ID
 */
public class PublisherRest extends AbstractRestResource {

    private final ObjectMapper mapper = new ObjectMapper();

    public PublisherRest(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super("publisher", req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        final String method = req.getMethod();
        final String path = req.getRequestURI();
        Message message;

        try {
            switch (method) {
                case "GET" -> {
                    if (path.matches(".*/publishers/?$")) {
                        handleGetAllPublishers();
                    } else if (path.matches(".*/publishers/\\d+$")) {
                        handleGetPublisherById(path);
                    } else {
                        sendMethodNotAllowed();
                    }
                }
                case "POST" -> {
                    if (path.matches(".*/publishers/?$")) {
                        handleInsertPublisher();
                    } else {
                        sendMethodNotAllowed();
                    }
                }
                case "PUT" -> {
                    if (path.matches(".*/publishers/\\d+$")) {
                        handleUpdatePublisher(path);
                    } else {
                        sendMethodNotAllowed();
                    }
                }
                case "DELETE" -> {
                    if (path.matches(".*/publishers/\\d+$")) {
                        handleDeletePublisher(path);
                    } else {
                        sendMethodNotAllowed();
                    }
                }
                default ->
                    sendMethodNotAllowed();
            }
        } catch (Exception e) {
            LOGGER.error("PublisherRest error", e);
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            message = new Message("Internal server error", "500", e.getMessage());
            message.toJSON(res.getOutputStream());
        }
    }

    private void handleGetAllPublishers() throws Exception {
        List<Publisher> publishers = new GetAllPublishersDAO(con).access().getOutputParam();
        res.setStatus(HttpServletResponse.SC_OK);
        res.setContentType("application/json;charset=UTF-8");
        mapper.writeValue(res.getOutputStream(), publishers);
    }

    private void handleGetPublisherById(String path) throws Exception {
        int publisherId = Integer.parseInt(path.substring(path.lastIndexOf("/") + 1));
        Publisher publisher = new GetPublisherByIdDAO(con, publisherId).access().getOutputParam();

        if (publisher == null) {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            new Message("Publisher not found.", "404", "No publisher with ID " + publisherId).toJSON(res.getOutputStream());
        } else {
            res.setStatus(HttpServletResponse.SC_OK);
            res.setContentType("application/json;charset=UTF-8");
            mapper.writeValue(res.getOutputStream(), publisher);
        }
    }

    private void handleInsertPublisher() throws Exception {
        Publisher publisher = mapper.readValue(req.getInputStream(), Publisher.class);
        boolean inserted = new InsertPublisherDAO(con, publisher).access().getOutputParam();

        if (inserted) {
            res.setStatus(HttpServletResponse.SC_CREATED);
            new Message("Publisher inserted successfully.", "201", publisher.getPublisherName()).toJSON(res.getOutputStream());
        } else {
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            new Message("Failed to insert publisher.", "500", "Insert operation failed.").toJSON(res.getOutputStream());
        }
    }

    private void handleUpdatePublisher(String path) throws Exception {
        int publisherId = Integer.parseInt(path.substring(path.lastIndexOf("/") + 1));

        // Read updated data from request body
        Publisher publisher = mapper.readValue(req.getInputStream(), Publisher.class);
        publisher.setPublisherId(publisherId); // Ensure the ID from path is used

        // Call DAO using updated Publisher object
        boolean updated = new UpdatePublisherDAO(con, publisher).access().getOutputParam();

        if (updated) {
            res.setStatus(HttpServletResponse.SC_OK);
            new Message("Publisher updated successfully.", "200", publisher.getPublisherName())
                    .toJSON(res.getOutputStream());
        } else {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            new Message("Update failed.", "404", "Publisher not found.")
                    .toJSON(res.getOutputStream());
        }
    }

    private void handleDeletePublisher(String path) throws Exception {
        int publisherId = Integer.parseInt(path.substring(path.lastIndexOf("/") + 1));
        boolean deleted = new DeletePublisherDAO(con, publisherId).access().getOutputParam();

        if (deleted) {
            res.setStatus(HttpServletResponse.SC_OK);
            new Message("Publisher deleted successfully.", "200", "Deleted ID: " + publisherId).toJSON(res.getOutputStream());
        } else {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            new Message("Delete failed.", "404", "Publisher not found.").toJSON(res.getOutputStream());
        }
    }

    private void sendMethodNotAllowed() throws IOException {
        res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        new Message("Unsupported method or path.", "405", "Allowed: GET, POST, PUT, DELETE.").toJSON(res.getOutputStream());
    }
}
