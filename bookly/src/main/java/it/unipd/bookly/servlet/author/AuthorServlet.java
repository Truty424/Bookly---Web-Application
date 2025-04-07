package it.unipd.bookly.servlet.author;

import it.unipd.bookly.Resource.Author;
import it.unipd.bookly.Resource.Book;
import it.unipd.bookly.dao.author.GetAllAuthorsDAO;
import it.unipd.bookly.dao.book.GetBooksByAuthorIdDAO;
import it.unipd.bookly.servlet.AbstractDatabaseServlet;
import it.unipd.bookly.LogContext;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "AuthorServlet", value = "/author/*")
public class AuthorServlet extends AbstractDatabaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setResource(req.getRequestURI());
        LogContext.setAction("authorServlet");

        try {
            String path = req.getRequestURI();

            if (path.matches(".*/author/?")) {
                showAllAuthors(req, resp);
            } else if (path.matches(".*/author/\\d+/?")) {
                int authorId = extractAuthorIdFromPath(path);
                showBooksByAuthor(req, resp, authorId);
            } else {
                resp.sendRedirect("/html/error.html");
            }

        } catch (Exception e) {
            LOGGER.error("AuthorServlet GET error: {}", e.getMessage());
            resp.sendRedirect("/html/error.html");
        } finally {
            LogContext.removeAction();
            LogContext.removeResource();
        }
    }

    private void showAllAuthors(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        List<Author> authors = new GetAllAuthorsDAO(getConnection()).access().getOutputParam();
        req.setAttribute("all_authors", authors);
        req.getRequestDispatcher("/jsp/author/allAuthors.jsp").forward(req, resp);
    }

    private void showBooksByAuthor(HttpServletRequest req, HttpServletResponse resp, int authorId) throws Exception {
        List<Book> books = new GetBooksByAuthorIdDAO(getConnection(), authorId).access().getOutputParam();
        req.setAttribute("author_books", books);
        req.setAttribute("author_id", authorId);
        req.getRequestDispatcher("/jsp/author/authorBook.jsp").forward(req, resp);
    }

    private int extractAuthorIdFromPath(String path) {
        String[] segments = path.split("/");
        return Integer.parseInt(segments[segments.length - 1]);
    }
}
