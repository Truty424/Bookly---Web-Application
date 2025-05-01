package it.unipd.bookly.rest.book;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.unipd.bookly.Resource.Book;
import it.unipd.bookly.Resource.Message;
import it.unipd.bookly.dao.book.SearchBookByTitleDAO;
import it.unipd.bookly.rest.AbstractRestResource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

/**
 * REST endpoint to search for books by title.
 * Endpoint: GET /api/books/search?title=example
 */
public class SearchBookRest extends AbstractRestResource {

    private final ObjectMapper mapper = new ObjectMapper();

    public SearchBookRest(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super("search-book", req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        res.setContentType("application/json;charset=UTF-8"); // Always set JSON Content-Type

        if ("GET".equalsIgnoreCase(req.getMethod())) {
            handleSearchByTitle();
        } else {
            res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            new Message("Method not allowed", "E405", "Only GET is supported for searching books.")
                    .toJSON(res.getOutputStream());
        }
    }

    private void handleSearchByTitle() throws IOException {
        final String titleParam = req.getParameter("title");

        if (titleParam == null || titleParam.isBlank()) {
            sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing 'title' parameter.", "E400",
                    "You must provide a 'title' to search for books.");
            return;
        }

        try {
            List<Book> books = new SearchBookByTitleDAO(con, titleParam).access().getOutputParam();

            if (books == null || books.isEmpty()) {
                sendError(HttpServletResponse.SC_NOT_FOUND, "No books found", "E404",
                        "No books found matching the title: " + titleParam);
                return;
            }

            res.setStatus(HttpServletResponse.SC_OK);
            mapper.writeValue(res.getOutputStream(), books);

            LOGGER.info("{} book(s) found for title search '{}'.", books.size(), titleParam);

        } catch (Exception e) {
            LOGGER.error("Error while searching books with title '{}'", titleParam, e);
            sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server error", "E500",
                    "Something went wrong while searching: " + e.getMessage());
        }
    }

    private void sendError(int status, String title, String code, String detail) throws IOException {
        res.setStatus(status);
        mapper.writeValue(res.getOutputStream(), new Message(title, code, detail));
    }
}
