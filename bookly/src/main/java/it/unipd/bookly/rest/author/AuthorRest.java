package it.unipd.bookly.rest.author;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.unipd.bookly.Resource.Author;
import it.unipd.bookly.Resource.Message;
import it.unipd.bookly.dao.author.DeleteAuthorDAO;
import it.unipd.bookly.dao.author.GetAllAuthorsDAO;
import it.unipd.bookly.dao.author.GetAuthorByIdDAO;
import it.unipd.bookly.dao.author.InsertAuthorDAO;
import it.unipd.bookly.dao.author.UpdateAuthorDAO;
import it.unipd.bookly.rest.AbstractRestResource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AuthorRest extends AbstractRestResource {

    private final ObjectMapper mapper = new ObjectMapper();

    public AuthorRest(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super("author", req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        final String method = req.getMethod();
        final String path = req.getRequestURI();
        Message message;

        try {
            switch (method) {
                case "GET" -> {
                    if (path.matches(".*/authors/?$")) {
                        handleGetAllAuthors();
                    } else if (path.matches(".*/authors/\\d+$")) {
                        handleGetAuthorById(path);
                    } else {
                        sendMethodNotAllowed();
                    }
                }
                case "POST" -> {
                    if (path.matches(".*/authors/?$")) {
                        handleInsertAuthor();
                    } else {
                        sendMethodNotAllowed();
                    }
                }
                case "PUT" -> {
                    if (path.matches(".*/authors/\\d+$")) {
                        handleUpdateAuthor(path);
                    } else {
                        sendMethodNotAllowed();
                    }
                }
                case "DELETE" -> {
                    if (path.matches(".*/authors/\\d+$")) {
                        handleDeleteAuthor(path);
                    } else {
                        sendMethodNotAllowed();
                    }
                }
                default ->
                    sendMethodNotAllowed();
            }
        } catch (Exception e) {
            LOGGER.error("AuthorRest error", e);
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            message = new Message("Internal server error", "500", e.getMessage());
            message.toJSON(res.getOutputStream());
        }
    }

    private void handleGetAllAuthors() throws Exception {
        List<Author> authors = new GetAllAuthorsDAO(con).access().getOutputParam();
        res.setStatus(HttpServletResponse.SC_OK);
        res.setContentType("application/json;charset=UTF-8");
        mapper.writeValue(res.getOutputStream(), authors);
    }

    private void handleGetAuthorById(String path) throws Exception {
        int authorId = Integer.parseInt(path.substring(path.lastIndexOf("/") + 1));
        Author author = new GetAuthorByIdDAO(con, authorId).access().getOutputParam();

        if (author == null) {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            new Message("Author not found.", "404", "No author with ID " + authorId).toJSON(res.getOutputStream());
        } else {
            res.setStatus(HttpServletResponse.SC_OK);
            res.setContentType("application/json;charset=UTF-8");
            mapper.writeValue(res.getOutputStream(), author);
        }
    }

    private void handleInsertAuthor() throws Exception {
        Author author = mapper.readValue(req.getInputStream(), Author.class);
        boolean success = new InsertAuthorDAO(con, author).access().getOutputParam();

        if (success) {
            res.setStatus(HttpServletResponse.SC_CREATED);
            new Message("Author inserted successfully.", "201", author.getName()).toJSON(res.getOutputStream());
        } else {
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            new Message("Failed to insert author.", "500", "Insert operation failed.").toJSON(res.getOutputStream());
        }
    }

    private void handleUpdateAuthor(String path) throws Exception {
        int authorId = Integer.parseInt(path.substring(path.lastIndexOf("/") + 1));
        Author author = mapper.readValue(req.getInputStream(), Author.class);
        author.setAuthor_id(authorId);

        boolean success = new UpdateAuthorDAO(con, author).access().getOutputParam();

        if (success) {
            res.setStatus(HttpServletResponse.SC_OK);
            new Message("Author updated successfully.", "200", author.getName()).toJSON(res.getOutputStream());
        } else {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            new Message("Update failed.", "404", "Author not found.").toJSON(res.getOutputStream());
        }
    }

    private void handleDeleteAuthor(String path) throws Exception {
        int authorId = Integer.parseInt(path.substring(path.lastIndexOf("/") + 1));
        boolean success = new DeleteAuthorDAO(con, authorId).access().getOutputParam();

        if (success) {
            res.setStatus(HttpServletResponse.SC_OK);
            new Message("Author deleted successfully.", "200", "Deleted ID: " + authorId).toJSON(res.getOutputStream());
        } else {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            new Message("Delete failed.", "404", "Author not found.").toJSON(res.getOutputStream());
        }
    }

    private void sendMethodNotAllowed() throws IOException {
        res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        new Message("Unsupported method or path.", "405", "Allowed: GET, POST, PUT, DELETE.").toJSON(res.getOutputStream());
    }
}
