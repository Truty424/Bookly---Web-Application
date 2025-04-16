package it.unipd.bookly.servlet.discount;

import it.unipd.bookly.Resource.Discount;
import it.unipd.bookly.dao.discount.DeleteDiscountDAO;
import it.unipd.bookly.dao.discount.GetAllDiscountsDAO;
import it.unipd.bookly.dao.discount.InsertDiscountDAO;
import it.unipd.bookly.servlet.AbstractDatabaseServlet;
import it.unipd.bookly.LogContext;
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
            ServletUtils.redirectToErrorPage(req, resp, "DiscountManagementServlet error: " + e.getMessage());
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
            switch (action) {
                case "create" -> createDiscount(req, resp, con);
                case "delete" -> deleteDiscount(req, resp, con);
                default -> ServletUtils.redirectToErrorPage(req, resp, "Unknown discount action: " + action);
            }
        } catch (Exception e) {
            LOGGER.error("Error handling discount POST action: {}", e.getMessage(), e);
            ServletUtils.redirectToErrorPage(req, resp, "DiscountManagementServlet error: " + e.getMessage());
        } finally {
            LogContext.removeAction();
            LogContext.removeResource();
        }
    }

    private void createDiscount(HttpServletRequest req, HttpServletResponse resp, Connection con) throws Exception {
        String code = req.getParameter("code");
        double percentage = Double.parseDouble(req.getParameter("percentage"));
        String expiryDateStr = req.getParameter("expiredDate");

        Timestamp expiryDate = Timestamp.valueOf(expiryDateStr + " 00:00:00");
        Discount discount = new Discount(code, percentage, expiryDate);

        boolean created = new InsertDiscountDAO(con, discount).access().getOutputParam();

        LOGGER.info("Discount created: {}", created);
        resp.sendRedirect(req.getContextPath() + "/admin/discounts");
    }

    private void deleteDiscount(HttpServletRequest req, HttpServletResponse resp, Connection con) throws Exception {
        int discountId = Integer.parseInt(req.getParameter("discountId"));
        boolean deleted = new DeleteDiscountDAO(con, discountId).access().getOutputParam();

        LOGGER.info("Discount deleted: {}", deleted);
        resp.sendRedirect(req.getContextPath() + "/admin/discounts");
    }
}
