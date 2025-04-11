package it.unipd.bookly.rest.author;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.unipd.bookly.Resource.Author;
import it.unipd.bookly.Resource.Message;
import it.unipd.bookly.dao.author.AddAuthorToBookDAO;
import it.unipd.bookly.dao.author.GetAuthorsByBookDAO;
import it.unipd.bookly.rest.AbstractRestResource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

public class AuthorBookAssignmentRest extends AbstractRestResource {

    private final ObjectMapper mapper = new ObjectMapper();

    public AuthorBookAssignmentRest(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super("author-book-assignment", req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        final String method = req.getMethod();
        final String path = req.getRequestURI();
        Message message;

        try {
            switch (method) {
                case "POST" -> {
                    if (path.endsWith("/authors/book")) {
                        handleAddAuthorToBook();
                    } else {
                        sendMethodNotAllowed();
                    }
                }
                case "GET" -> {
                    if (path.matches(".*/authors/book/\\d+$")) {
                        handleGetAuthorsByBook(path);
                    } else {
                        sendMethodNotAllowed();
                    }
                }
                default -> sendMethodNotAllowed();
            }
        } catch (Exception e) {
            LOGGER.error("AuthorBookAssignmentRest error: ", e);
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            message = new Message("Internal server error", "E500", e.getMessage());
            message.toJSON(res.getOutputStream());
        }
    }

    private void handleAddAuthorToBook() throws Exception {
        String authorIdParam = req.getParameter("authorId");
        String bookIdParam = req.getParameter("bookId");

        if (authorIdParam == null || bookIdParam == null) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            new Message("Missing authorId or bookId parameter.", "E400",
                    "Both authorId and bookId must be provided.")
                    .toJSON(res.getOutputStream());
            return;
        }

        int authorId = Integer.parseInt(authorIdParam);
        int bookId = Integer.parseInt(bookIdParam);

        new AddAuthorToBookDAO(con, bookId, authorId).access();

        res.setStatus(HttpServletResponse.SC_CREATED);
        new Message("Author assigned to book successfully.", "201",
                "Author ID " + authorId + " → Book ID " + bookId)
                .toJSON(res.getOutputStream());
    }

    private void handleGetAuthorsByBook(String path) throws Exception {
        int bookId = Integer.parseInt(path.substring(path.lastIndexOf("/") + 1));
        List<Author> authors = new GetAuthorsByBookDAO(con, bookId).access().getOutputParam();

        res.setStatus(HttpServletResponse.SC_OK);
        res.setContentType("application/json;charset=UTF-8");

        String authorsJson = mapper.writeValueAsString(authors);
        new Message("Authors retrieved successfully", "200", authorsJson)
                .toJSON(res.getOutputStream());
    }

    private void sendMethodNotAllowed() throws IOException {
        res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        new Message("Unsupported operation.", "405",
                "Only GET and POST are supported for author-book relationships.")
                .toJSON(res.getOutputStream());
    }
}
