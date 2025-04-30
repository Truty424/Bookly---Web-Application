package it.unipd.bookly.servlet.author;

import it.unipd.bookly.Resource.Author;
import it.unipd.bookly.Resource.Book;
import it.unipd.bookly.dao.author.GetAllAuthorsDAO;
import it.unipd.bookly.dao.book.GetBooksByAuthorIdDAO;
import it.unipd.bookly.servlet.AbstractDatabaseServlet;
import it.unipd.bookly.LogContext;
import it.unipd.bookly.utilities.ServletUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

@WebServlet(name = "AuthorServlet", value = "/author/*")
public class AuthorServlet extends AbstractDatabaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setResource(req.getRequestURI());
        LogContext.setAction("AuthorServlet");

        String path = req.getPathInfo(); // e.g. / or /12

        try (Connection con = getConnection()) {
            if (path == null || "/".equals(path)) {
                showAllAuthors(req, resp, con);
                return;
            }

            // Clean path and determine action
            path = path.replaceAll("^/+", "").replaceAll("/+$", "");

            switch (getPathType(path)) {
                case "authorId" -> showBooksByAuthor(req, resp, con, Integer.parseInt(path));
                default -> {
                    LOGGER.warn("Unrecognized path in AuthorServlet: {}", path);
                    ServletUtils.redirectToErrorPage(req, resp, "Unknown author path: " + path);
                }
            }

        } catch (Exception e) {
            LOGGER.error("AuthorServlet GET error", e);
            ServletUtils.redirectToErrorPage(req, resp, "AuthorServlet error: " + e.getMessage());
        } finally {
            LogContext.removeAction();
            LogContext.removeResource();
        }
    }

    private void showAllAuthors(HttpServletRequest req, HttpServletResponse resp, Connection con) throws Exception {
        List<Author> authors = new GetAllAuthorsDAO(con).access().getOutputParam();
        req.setAttribute("all_authors", authors);
        req.getRequestDispatcher("/jsp/author/allAuthors.jsp").forward(req, resp);
    }

    private void showBooksByAuthor(HttpServletRequest req, HttpServletResponse resp, Connection con, int authorId) throws Exception {
        List<Book> books = new GetBooksByAuthorIdDAO(con, authorId).access().getOutputParam();
        req.setAttribute("author_books", books);
        req.setAttribute("author_id", authorId);
        req.getRequestDispatcher("/jsp/author/authorBooks.jsp").forward(req, resp);
    }

    private String getPathType(String path) {
        if (path.matches("\\d+")) {
            return "authorId";
        }
        return "unknown";
    }
}
