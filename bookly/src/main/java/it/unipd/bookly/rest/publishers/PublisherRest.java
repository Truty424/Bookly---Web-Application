package it.unipd.bookly.rest.publishers;

import it.unipd.bookly.Resource.Message;
import it.unipd.bookly.Resource.Publisher;
import it.unipd.bookly.dao.publisher.*;
import it.unipd.bookly.rest.AbstractRestResource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

public class PublisherRest extends AbstractRestResource {

    public PublisherRest(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super("publisher", req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        String method = req.getMethod();
        String path = req.getRequestURI();
        Message message = null;

        try {
            if ("GET".equals(method) && path.matches(".*/publishers/\\d+")) {
                handleGetById(path);
            } else if ("GET".equals(method)) {
                handleGetAll();
            } else if ("POST".equals(method)) {
                handleInsert();
            } else if ("PUT".equals(method)) {
                handleUpdate();
            } else if ("DELETE".equals(method) && path.matches(".*/publishers/\\d+")) {
                handleDelete(path);
            } else {
                res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                message = new Message("Unsupported method or path", "405", "Check endpoint syntax.");
                message.toJSON(res.getOutputStream());
            }
        } catch (Exception e) {
            LOGGER.error("PublisherRest exception", e);
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            message = new Message("Internal error", "500", e.getMessage());
            message.toJSON(res.getOutputStream());
        }
    }

    private void handleGetAll() throws Exception {
        List<Publisher> publishers = new GetAllPublishersDAO(con).access().getOutputParam();
        res.setContentType("application/json");
        for (Publisher p : publishers) {
            p.toJSON(res.getOutputStream());
        }
    }

    private void handleGetById(String path) throws Exception {
        int id = Integer.parseInt(path.substring(path.lastIndexOf("/") + 1));
        Publisher publisher = new GetPublisherByIdDAO(con, id).access().getOutputParam();
        if (publisher != null) {
            res.setContentType("application/json");
            publisher.toJSON(res.getOutputStream());
        } else {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            message = new Message("Publisher not found", "404");
            message.toJSON(res.getOutputStream());
        }
    }

    private void handleInsert() throws Exception {
        Publisher publisher = Publisher.fromJSON(req.getInputStream());
        boolean inserted = new InsertPublisherDAO(con, publisher).access().getOutputParam();
        res.setStatus(inserted ? HttpServletResponse.SC_CREATED : HttpServletResponse.SC_BAD_REQUEST);
        message = new Message(inserted ? "Publisher created" : "Insertion failed", inserted ? "201" : "400");
            message.toJSON(res.getOutputStream());
    }

    private void handleUpdate() throws Exception {
        Publisher publisher = Publisher.fromJSON(req.getInputStream());
        boolean updated = new UpdatePublisherDAO(con, publisher.getPublisherId(),
                publisher.getPublisherName(), publisher.getPhone(), publisher.getAddress())
                .access().getOutputParam();
        res.setStatus(updated ? HttpServletResponse.SC_OK : HttpServletResponse.SC_NOT_FOUND);
        message = new Message(updated ? "Publisher updated" : "Update failed", updated ? "200" : "404");
            message.toJSON(res.getOutputStream());
    }

    private void handleDelete(String path) throws Exception {
        int id = Integer.parseInt(path.substring(path.lastIndexOf("/") + 1));
        boolean deleted = new DeletePublisherDAO(con, id).access().getOutputParam();
        res.setStatus(deleted ? HttpServletResponse.SC_OK : HttpServletResponse.SC_NOT_FOUND);
        message = new Message(deleted ? "Publisher deleted" : "Deletion failed", deleted ? "200" : "404");
            message.toJSON(res.getOutputStream());
    }
}
