package it.unipd.bookly.servlet.discount;

import it.unipd.bookly.Resource.Discount;
import it.unipd.bookly.dao.discount.DeleteDiscountDAO;
import it.unipd.bookly.dao.discount.GetAllDiscountsDAO;
import it.unipd.bookly.dao.discount.InsertDiscountDAO;
import it.unipd.bookly.servlet.AbstractDatabaseServlet;
import it.unipd.bookly.utilities.ServletUtils;
import it.unipd.bookly.LogContext;

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
            LOGGER.error("Error loading discount management page: {}", e.getMessage(), e);
            ServletUtils.redirectToErrorPage(req, resp, "Error loading discount list.");
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
                case "create" -> handleCreateDiscount(req, resp, con);
                case "delete" -> handleDeleteDiscount(req, resp, con);
                default -> {
                    LOGGER.warn("Unsupported discount action: '{}'", action);
                    ServletUtils.redirectToErrorPage(req, resp, "Unknown discount action.");
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error in discount management: {}", e.getMessage(), e);
            ServletUtils.redirectToErrorPage(req, resp, "Discount management failed.");
        } finally {
            LogContext.removeAction();
            LogContext.removeResource();
        }
    }

    private void handleCreateDiscount(HttpServletRequest req, HttpServletResponse resp, Connection con) throws Exception {
        String code = req.getParameter("code");
        String percentageStr = req.getParameter("percentage");
        String expiryDateStr = req.getParameter("expiredDate");

        if (code == null || percentageStr == null || expiryDateStr == null) {
            ServletUtils.redirectToErrorPage(req, resp, "Missing required discount fields.");
            return;
        }

        double percentage = Double.parseDouble(percentageStr);
        Timestamp expiry = Timestamp.valueOf(expiryDateStr + " 00:00:00");

        Discount discount = new Discount(code, percentage, expiry);
        boolean inserted = new InsertDiscountDAO(con, discount).access().getOutputParam();

        LOGGER.info("Discount '{}' inserted: {}", code, inserted);
        resp.sendRedirect(req.getContextPath() + "/admin/discounts");
    }

    private void handleDeleteDiscount(HttpServletRequest req, HttpServletResponse resp, Connection con) throws Exception {
        String idParam = req.getParameter("discountId");

        if (idParam == null || idParam.isEmpty()) {
            ServletUtils.redirectToErrorPage(req, resp, "Missing discount ID for deletion.");
            return;
        }

        int discountId = Integer.parseInt(idParam);
        boolean deleted = new DeleteDiscountDAO(con, discountId).access().getOutputParam();

        LOGGER.info("Discount ID {} deleted: {}", discountId, deleted);
        resp.sendRedirect(req.getContextPath() + "/admin/discounts");
    }
}
