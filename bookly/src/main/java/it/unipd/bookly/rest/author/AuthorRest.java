package it.unipd.bookly.rest.author;

import it.unipd.bookly.Resource.Author;
import it.unipd.bookly.Resource.Message;
import it.unipd.bookly.dao.author.*;
import it.unipd.bookly.rest.AbstractRestResource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

/**
 * REST endpoint for managing authors.
 * Handles:
 * - GET /api/authors → get all authors
 * - GET /api/authors/{id} → get author by ID
 * - POST /api/authors → add new author
 * - PUT /api/authors/{id} → update author
 * - DELETE /api/authors/{id} → delete author
 */
public class AuthorRest extends AbstractRestResource {

    public AuthorRest(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super("author-rest", req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        String method = req.getMethod();
        String path = req.getRequestURI();

        try {
            if ("GET".equals(method) && path.matches(".*/api/authors/?")) {
                handleGetAllAuthors();
            } else if ("GET".equals(method) && path.matches(".*/api/authors/\\d+")) {
                handleGetAuthorById(path);
            } else if ("POST".equals(method) && path.matches(".*/api/authors/?")) {
                handleInsertAuthor();
            } else if ("PUT".equals(method) && path.matches(".*/api/authors/\\d+")) {
                handleUpdateAuthor(path);
            } else if ("DELETE".equals(method) && path.matches(".*/api/authors/\\d+")) {
                handleDeleteAuthor(path);
            } else {
                res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                new Message("Unsupported method or path.", "405", "Check method and URL.").toJSON(res.getOutputStream());
            }

        } catch (Exception e) {
            LOGGER.error("Error in AuthorRest: {}", e.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            new Message("Internal server error", "E500", e.getMessage()).toJSON(res.getOutputStream());
        }
    }

    private void handleGetAllAuthors() throws Exception {
        List<Author> authors = new GetAllAuthorsDAO(con).access().getOutputParam();
        res.setContentType("application/json");
        res.setStatus(HttpServletResponse.SC_OK);
        for (Author author : authors) {
            author.toJSON(res.getOutputStream());
        }
    }

    private void handleGetAuthorById(String path) throws Exception {
        int authorId = Integer.parseInt(path.substring(path.lastIndexOf("/") + 1));
        Author author = new GetAuthorByIdDAO(con, authorId).access().getOutputParam();

        if (author == null) {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            new Message("Author not found", "404", "No author with ID " + authorId).toJSON(res.getOutputStream());
        } else {
            res.setContentType("application/json");
            res.setStatus(HttpServletResponse.SC_OK);
            author.toJSON(res.getOutputStream());
        }
    }

    private void handleInsertAuthor() throws Exception {
        Author author = Author.fromJSON(req.getInputStream());
        boolean success = new InsertAuthorDAO(con, author).access().getOutputParam();

        if (success) {
            res.setStatus(HttpServletResponse.SC_CREATED);
            new Message("Author inserted successfully", "201", author.getName()).toJSON(res.getOutputStream());
        } else {
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            new Message("Failed to insert author", "500", "Insert operation failed").toJSON(res.getOutputStream());
        }
    }

    private void handleUpdateAuthor(String path) throws Exception {
        int authorId = Integer.parseInt(path.substring(path.lastIndexOf("/") + 1));
        Author author = Author.fromJSON(req.getInputStream());
        author.setAuthor_id(authorId);

        boolean success = new UpdateAuthorDAO(con, author).access().getOutputParam();

        if (success) {
            res.setStatus(HttpServletResponse.SC_OK);
            new Message("Author updated successfully", "200", author.getName()).toJSON(res.getOutputStream());
        } else {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            new Message("Update failed", "404", "Author not found").toJSON(res.getOutputStream());
        }
    }

    private void handleDeleteAuthor(String path) throws Exception {
        int authorId = Integer.parseInt(path.substring(path.lastIndexOf("/") + 1));

        boolean success = new DeleteAuthorDAO(con, authorId).access().getOutputParam();

        if (success) {
            res.setStatus(HttpServletResponse.SC_OK);
            new Message("Author deleted successfully", "200", "ID: " + authorId).toJSON(res.getOutputStream());
        } else {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            new Message("Delete failed", "404", "Author not found").toJSON(res.getOutputStream());
        }
    }
}
