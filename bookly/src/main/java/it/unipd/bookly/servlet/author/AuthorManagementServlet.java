package it.unipd.bookly.servlet.author;

import it.unipd.bookly.Resource.Author;
import it.unipd.bookly.dao.author.*;
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

@WebServlet(name = "AuthorManagementServlet", value = "/admin/authors/*")
public class AuthorManagementServlet extends AbstractDatabaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setResource(req.getRequestURI());
        LogContext.setAction("authorManagement");

        try (Connection con = getConnection()) {
            List<Author> authors = new GetAllAuthorsDAO(con).access().getOutputParam();
            req.setAttribute("authors", authors);
            req.getRequestDispatcher("/jsp/admin/manageAuthors.jsp").forward(req, resp);
        } catch (Exception e) {
            LOGGER.error("Error loading author management page: {}", e.getMessage(), e);
            ServletUtils.redirectToErrorPage(req, resp, "AuthorManagementServlet error: " + e.getMessage());
        } finally {
            LogContext.removeAction();
            LogContext.removeResource();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setResource(req.getRequestURI());
        LogContext.setAction("authorManagement");

        String action = req.getParameter("action");

        try (Connection con = getConnection()) {
            switch (action) {
                case "create" -> createAuthor(req, resp, con);
                case "update" -> updateAuthor(req, resp, con);
                case "delete" -> deleteAuthor(req, resp, con);
                default -> ServletUtils.redirectToErrorPage(req, resp, "Unknown author action: " + action);
            }
        } catch (Exception e) {
            LOGGER.error("Error handling author POST action: {}", e.getMessage(), e);
            ServletUtils.redirectToErrorPage(req, resp, "AuthorManagementServlet error: " + e.getMessage());
        } finally {
            LogContext.removeAction();
            LogContext.removeResource();
        }
    }

    private void createAuthor(HttpServletRequest req, HttpServletResponse resp, Connection con) throws Exception {
        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");
        String bio = req.getParameter("biography");
        String nationality = req.getParameter("nationality");

        Author author = new Author(firstName, lastName, bio, nationality);
        boolean created = new InsertAuthorDAO(con, author).access().getOutputParam();

        LOGGER.info("Author created: {}", created);
        resp.sendRedirect(req.getContextPath() + "/admin/authors");
    }

    private void updateAuthor(HttpServletRequest req, HttpServletResponse resp, Connection con) throws Exception {
        int id = Integer.parseInt(req.getParameter("author_id"));
        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");
        String bio = req.getParameter("biography");
        String nationality = req.getParameter("nationality");

        Author author = new Author(firstName, lastName, bio, nationality);
        author.setAuthor_id(id);

        boolean updated = new UpdateAuthorDAO(con, author).access().getOutputParam();

        LOGGER.info("Author updated: {}", updated);
        resp.sendRedirect(req.getContextPath() + "/admin/authors");
    }

    private void deleteAuthor(HttpServletRequest req, HttpServletResponse resp, Connection con) throws Exception {
        int id = Integer.parseInt(req.getParameter("author_id"));
        boolean deleted = new DeleteAuthorDAO(con, id).access().getOutputParam();

        LOGGER.info("Author deleted: {}", deleted);
        resp.sendRedirect(req.getContextPath() + "/admin/authors");
    }
}
