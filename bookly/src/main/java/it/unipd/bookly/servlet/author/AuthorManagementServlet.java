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

import it.unipd.bookly.Resource.User;
import jakarta.servlet.http.HttpSession;

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
            LOGGER.error("Error loading author management page", e);
            ServletUtils.redirectToErrorPage(req, resp, "Failed to load authors: " + e.getMessage());
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

        try {
            HttpSession session = req.getSession(false);
            User user = (session != null) ? (User) session.getAttribute("user") : null;
            if (user == null || !"admin".equalsIgnoreCase(user.getRole())) {
                resp.sendRedirect(req.getContextPath() + "/user/login");
                return;
            }

            String action = req.getParameter("action");
            try (Connection con = getConnection()) {
                switch (action) {
                    case "create" -> createAuthor(req, resp, con);
                    case "update" -> updateAuthor(req, resp, con);
                    case "delete" -> deleteAuthor(req, resp, con);
                    default -> ServletUtils.redirectToErrorPage(req, resp, "Unknown author action: " + action);
                }
            }
        } catch (Exception e) {
            LOGGER.error("AuthorManagementServlet POST error", e);
            ServletUtils.redirectToErrorPage(req, resp, "Error managing author: " + e.getMessage());
        } finally {
            LogContext.removeAction();
            LogContext.removeResource();
        }
    }

    private void createAuthor(HttpServletRequest req, HttpServletResponse resp, Connection con) throws Exception {
        Author author = buildAuthorFromRequest(req, false);
        boolean created = new InsertAuthorDAO(con, author).access().getOutputParam();
        LOGGER.info("Create author [{} {}]: {}", author.getFirstName(), author.getLastName(), created);
        resp.sendRedirect(req.getContextPath() + "/admin/authors");
    }

    private void updateAuthor(HttpServletRequest req, HttpServletResponse resp, Connection con) throws Exception {
        Author author = buildAuthorFromRequest(req, true);
        boolean updated = new UpdateAuthorDAO(con, author).access().getOutputParam();
        LOGGER.info("Update author ID {}: {}", author.getAuthorId(), updated);
        resp.sendRedirect(req.getContextPath() + "/admin/authors");
    }

    private void deleteAuthor(HttpServletRequest req, HttpServletResponse resp, Connection con) throws Exception {
        int id;
        try {
            id = Integer.parseInt(req.getParameter("author_id"));
        } catch (NumberFormatException e) {
            LOGGER.warn("Invalid author_id during deletion.");
            ServletUtils.redirectToErrorPage(req, resp, "Invalid author ID.");
            return;
        }

        boolean deleted = new DeleteAuthorDAO(con, id).access().getOutputParam();
        LOGGER.info("Delete author ID {}: {}", id, deleted);
        resp.sendRedirect(req.getContextPath() + "/admin/authors");
    }

    private Author buildAuthorFromRequest(HttpServletRequest req, boolean withId) throws Exception {
        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");
        String bio = req.getParameter("biography");
        String nationality = req.getParameter("nationality");

        if (firstName == null || lastName == null) {
            throw new IllegalArgumentException("Author first name and last name are required.");
        }

        Author author = new Author(firstName, lastName, bio, nationality);
        if (withId) {
            String idStr = req.getParameter("author_id");
            if (idStr == null || !idStr.matches("\\d+")) {
                throw new IllegalArgumentException("Invalid author ID for update.");
            }
            author.setAuthor_id(Integer.parseInt(idStr));
        }

        return author;
    }
}
