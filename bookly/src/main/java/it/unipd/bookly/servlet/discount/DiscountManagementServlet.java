package it.unipd.bookly.servlet.discount;

import it.unipd.bookly.LogContext;
import it.unipd.bookly.Resource.Discount;
import it.unipd.bookly.dao.discount.DeleteDiscountDAO;
import it.unipd.bookly.dao.discount.GetAllDiscountsDAO;
import it.unipd.bookly.dao.discount.InsertDiscountDAO;
import it.unipd.bookly.servlet.AbstractDatabaseServlet;
import it.unipd.bookly.utilities.ServletUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.List;

@WebServlet(name = "DiscountManagementServlet", value = "/admin/discounts/*")
public class DiscountManagementServlet extends AbstractDatabaseServlet {

    private static final String ACTION_CREATE = "create";
    private static final String ACTION_DELETE = "delete";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setResource(req.getRequestURI());
        LogContext.setAction("discountManagement");

        try (Connection con = getConnection()) {
            List<Discount> discounts = new GetAllDiscountsDAO(con).access().getOutputParam();
            req.setAttribute("discounts", discounts);
            req.getRequestDispatcher("/jsp/admin/manageDiscounts.jsp").forward(req, resp);
        } catch (Exception e) {
            LOGGER.error("Error loading discounts: {}", e.getMessage(), e);
            ServletUtils.redirectToErrorPage(req, resp, "Unable to load discounts.");
        } finally {
            LogContext.removeAction();
            LogContext.removeResource();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setResource(req.getRequestURI());
        LogContext.setAction("discountManagement");

        String action = req.getParameter("action");

        try (Connection con = getConnection()) {
            switch (action != null ? action : "") {
                case ACTION_CREATE -> handleCreateDiscount(req, resp, con);
                case ACTION_DELETE -> handleDeleteDiscount(req, resp, con);
                default -> {
                    LOGGER.warn("Unknown discount action: '{}'", action);
                    ServletUtils.redirectToErrorPage(req, resp, "Invalid discount action: " + action);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error processing discount action '{}': {}", action, e.getMessage(), e);
            ServletUtils.redirectToErrorPage(req, resp, "Error performing discount action.");
        } finally {
            LogContext.removeAction();
            LogContext.removeResource();
        }
    }

    private void handleCreateDiscount(HttpServletRequest req, HttpServletResponse resp, Connection con) throws Exception {
        String code = req.getParameter("code");
        String percentageStr = req.getParameter("percentage");
        String expiryStr = req.getParameter("expiredDate");

        if (code == null || code.isBlank() || percentageStr == null || expiryStr == null) {
            LOGGER.warn("Missing discount creation parameters.");
            ServletUtils.redirectToErrorPage(req, resp, "Please fill all required discount fields.");
            return;
        }

        double percentage;
        try {
            percentage = Double.parseDouble(percentageStr);
        } catch (NumberFormatException e) {
            LOGGER.warn("Invalid percentage value: '{}'", percentageStr);
            ServletUtils.redirectToErrorPage(req, resp, "Percentage must be a valid number.");
            return;
        }

        Timestamp expiry = Timestamp.valueOf(expiryStr + " 00:00:00");

        Discount discount = new Discount(code, percentage, expiry);
        boolean created = new InsertDiscountDAO(con, discount).access().getOutputParam();

        LOGGER.info("Discount '{}' created: {}", code, created);
        resp.sendRedirect(req.getContextPath() + "/admin/discounts");
    }

    private void handleDeleteDiscount(HttpServletRequest req, HttpServletResponse resp, Connection con) throws Exception {
        String idParam = req.getParameter("discountId");

        if (idParam == null || idParam.isBlank()) {
            LOGGER.warn("Missing discountId for deletion.");
            ServletUtils.redirectToErrorPage(req, resp, "Missing discount ID.");
            return;
        }

        int discountId;
        try {
            discountId = Integer.parseInt(idParam);
        } catch (NumberFormatException e) {
            LOGGER.warn("Invalid discount ID format: '{}'", idParam);
            ServletUtils.redirectToErrorPage(req, resp, "Discount ID must be a valid number.");
            return;
        }

        boolean deleted = new DeleteDiscountDAO(con, discountId).access().getOutputParam();
        LOGGER.info("Discount ID {} deleted: {}", discountId, deleted);
        resp.sendRedirect(req.getContextPath() + "/admin/discounts");
    }
}
