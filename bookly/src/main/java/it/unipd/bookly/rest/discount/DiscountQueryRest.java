package it.unipd.bookly.rest.discount;

import it.unipd.bookly.Resource.Discount;
import it.unipd.bookly.Resource.Message;
import it.unipd.bookly.dao.discount.GetDiscountByCodeDAO;
import it.unipd.bookly.dao.discount.GetValidDiscountByCodeDAO;
import it.unipd.bookly.rest.AbstractRestResource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;

/**
 * Handles query-based discount retrieval endpoints:
 * - GET /api/discount/query?code=CODE
 * - GET /api/discount/query/valid?code=CODE
 */
public class DiscountQueryRest extends AbstractRestResource {

    public DiscountQueryRest(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super("discount-query", req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        final String method = req.getMethod();
        final String path = req.getRequestURI();
        final String code = req.getParameter("code");
        Message message = null;

        try {
            if (!"GET".equals(method)) {
                res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                message = new Message("Only GET method is supported.", "405", "Use GET for discount queries.");
                message.toJSON(res.getOutputStream());
                return;
            }

            if (code == null || code.isBlank()) {
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                message = new Message("Missing discount code parameter.", "E400", "Query parameter 'code' is required.");
                message.toJSON(res.getOutputStream());
                return;
            }

            if (path.contains("/valid")) {
                handleGetValidDiscount(code);
            } else {
                handleGetDiscount(code);
            }

        } catch (Exception e) {
            LOGGER.error("Error in DiscountQueryRest", e);
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            message = new Message("Internal error in discount query.", "E500", e.getMessage());
            message.toJSON(res.getOutputStream());
        }
    }

    private void handleGetDiscount(String code) throws Exception {
        Discount discount = new GetDiscountByCodeDAO(con, code).access().getOutputParam();

        if (discount == null) {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            new Message("Discount code not found.", "E404", "No discount found for code: " + code)
                    .toJSON(res.getOutputStream());
        } else {
            res.setContentType("application/json;charset=UTF-8");
            discount.toJSON(res.getOutputStream());
        }
    }

    private void handleGetValidDiscount(String code) throws Exception {
        Discount discount = new GetValidDiscountByCodeDAO(con, code).access().getOutputParam();

        if (discount == null) {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            new Message("No valid discount found.", "E404", "Code may be expired or invalid: " + code)
                    .toJSON(res.getOutputStream());
        } else {
            res.setContentType("application/json;charset=UTF-8");
            discount.toJSON(res.getOutputStream());
        }
    }
}