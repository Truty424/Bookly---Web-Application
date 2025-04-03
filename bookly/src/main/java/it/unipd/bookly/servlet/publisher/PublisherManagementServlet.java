package it.unipd.bookly.servlet.publisher;

import it.unipd.bookly.Resource.Publisher;
import it.unipd.bookly.dao.publisher.*;
import it.unipd.bookly.servlet.AbstractDatabaseServlet;
import it.unipd.bookly.LogContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "PublisherManagementServlet", value = "/admin/publishers/*")
public class PublisherManagementServlet extends AbstractDatabaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setResource(req.getRequestURI());
        LogContext.setAction("publisherManagement");

        try {
            List<Publisher> publishers = new GetAllPublishersDAO(getConnection()).access().getOutputParam();
            req.setAttribute("publishers", publishers);
            req.getRequestDispatcher("/jsp/publisher/manage.jsp").forward(req, resp);
        } catch (Exception e) {
            LOGGER.error("Error loading publisher management page: {}", e.getMessage());
            resp.sendRedirect("/html/error.html");
        } finally {
            LogContext.removeAction();
            LogContext.removeResource();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        try {
            switch (action) {
                case "create":
                    createPublisher(req, resp);
                    break;
                case "update":
                    updatePublisher(req, resp);
                    break;
                case "delete":
                    deletePublisher(req, resp);
                    break;
                default:
                    resp.sendRedirect("/html/error.html");
            }
        } catch (Exception e) {
            LOGGER.error("Error handling publisher POST action: {}", e.getMessage());
            resp.sendRedirect("/html/error.html");
        }
    }

    private void createPublisher(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String name = req.getParameter("name");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");

        boolean created = new InsertPublisherDAO(getConnection(), new Publisher(name, phone, address)).access().getOutputParam();
        LOGGER.info("Publisher created: {}", created);

        resp.sendRedirect(req.getContextPath() + "/admin/publishers");
    }

    private void updatePublisher(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        int id = Integer.parseInt(req.getParameter("publisherId"));
        String name = req.getParameter("name");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");

        boolean updated = new UpdatePublisherDAO(getConnection(), id, name, phone, address).access().getOutputParam();
        LOGGER.info("Publisher updated: {}", updated);

        resp.sendRedirect(req.getContextPath() + "/admin/publishers");
    }

    private void deletePublisher(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        int id = Integer.parseInt(req.getParameter("publisherId"));
        boolean deleted = new DeletePublisherDAO(getConnection(), id).access().getOutputParam();
        LOGGER.info("Publisher deleted: {}", deleted);

        resp.sendRedirect(req.getContextPath() + "/admin/publishers");
    }
}
