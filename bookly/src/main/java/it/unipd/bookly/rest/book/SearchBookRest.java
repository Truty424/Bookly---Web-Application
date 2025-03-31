package it.unipd.bookly.rest.book;

import it.unipd.bookly.Resource.Book;
import it.unipd.bookly.Resource.Message;
import it.unipd.bookly.dao.book.SearchBookByTitleDAO;
import it.unipd.bookly.rest.AbstractRestResource;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

/**
 * Handles GET requests to search for books by title.
 * Endpoint: /api/book/search?title=example
 */
public class SearchBookRest extends AbstractRestResource {

    public SearchBookRest(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super("search-book", req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        final String titleParam = req.getParameter("title");
        Message message = null;

        if (titleParam == null || titleParam.isBlank()) {
            message = new Message("Missing 'title' parameter.", "E400", "You must provide a title to search for books.");
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            message.toJSON(res.getOutputStream());
            return;
        }

        try {
            List<Book> books = new SearchBookByTitleDAO(con, titleParam).access().getOutputParam();

            if (books == null || books.isEmpty()) {
                message = new Message("No books found for the given title.", "E404", "Try a different title or keyword.");
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                message.toJSON(res.getOutputStream());
                return;
            }

            res.setContentType("application/json;charset=UTF-8");
            res.setStatus(HttpServletResponse.SC_OK);

            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(res.getOutputStream(), books);

        } catch (Exception e) {
            LOGGER.error("Error while searching books by title: {}", titleParam, e);
            message = new Message("Internal server error while searching books.", "E500", e.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            message.toJSON(res.getOutputStream());
        }
    }
}
