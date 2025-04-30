package it.unipd.bookly.servlet.publisher;

import it.unipd.bookly.Resource.Publisher;
import it.unipd.bookly.dao.publisher.*;
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

@WebServlet(name = "PublisherManagementServlet", value = "/admin/publishers/*")
public class PublisherManagementServlet extends AbstractDatabaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setResource(req.getRequestURI());
        LogContext.setAction("publisherManagement");

        try (Connection con = getConnection()) {
            List<Publisher> publishers = new GetAllPublishersDAO(con).access().getOutputParam();
            req.setAttribute("publishers", publishers);
            req.getRequestDispatcher("/jsp/admin/managePublishers.jsp").forward(req, resp);
        } catch (Exception e) {
            LOGGER.error("Error loading publisher management page: {}", e.getMessage(), e);
            ServletUtils.redirectToErrorPage(req, resp, "Error loading publishers: " + e.getMessage());
        } finally {
            LogContext.removeAction();
            LogContext.removeResource();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setResource(req.getRequestURI());
        LogContext.setAction("publisherManagement");

        String action = req.getParameter("action");

        try (Connection con = getConnection()) {
            switch (action) {
                case "create" -> createPublisher(req, resp, con);
                case "update" -> updatePublisher(req, resp, con);
                case "delete" -> deletePublisher(req, resp, con);
                default -> ServletUtils.redirectToErrorPage(req, resp, "Unsupported action: " + action);
            }
        } catch (Exception e) {
            LOGGER.error("Error processing publisher action '{}': {}", action, e.getMessage(), e);
            ServletUtils.redirectToErrorPage(req, resp, "Publisher action failed: " + e.getMessage());
        } finally {
            LogContext.removeAction();
            LogContext.removeResource();
        }
    }

    private void createPublisher(HttpServletRequest req, HttpServletResponse resp, Connection con) throws Exception {
        String name = req.getParameter("name");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");

        Publisher newPublisher = new Publisher(name, phone, address);
        boolean created = new InsertPublisherDAO(con, newPublisher).access().getOutputParam();

        LOGGER.info("Publisher created: {}", created);
        resp.sendRedirect(req.getContextPath() + "/admin/publishers");
    }

    private void updatePublisher(HttpServletRequest req, HttpServletResponse resp, Connection con) throws Exception {
        int publisherId = Integer.parseInt(req.getParameter("publisher_id"));
        String name = req.getParameter("name");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");

        Publisher publisher = new Publisher(name, phone, address);
        publisher.setPublisherId(publisherId);

        boolean updated = new UpdatePublisherDAO(con, publisher).access().getOutputParam();

        LOGGER.info("Publisher updated (ID={}): {}", publisherId, updated);
        resp.sendRedirect(req.getContextPath() + "/admin/publishers");
    }

    private void deletePublisher(HttpServletRequest req, HttpServletResponse resp, Connection con) throws Exception {
        int publisherId = Integer.parseInt(req.getParameter("publisher_id"));
        boolean deleted = new DeletePublisherDAO(con, publisherId).access().getOutputParam();

        LOGGER.info("Publisher deleted (ID={}): {}", publisherId, deleted);
        resp.sendRedirect(req.getContextPath() + "/admin/publishers");
    }
}
