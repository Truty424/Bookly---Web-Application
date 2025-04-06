package it.unipd.bookly.rest.discount;

import it.unipd.bookly.Resource.Discount;
import it.unipd.bookly.Resource.Message;
import it.unipd.bookly.dao.discount.*;
import it.unipd.bookly.rest.AbstractRestResource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Handles discount-related REST endpoints:
 * - GET    /api/discount/all
 * - GET    /api/discount/active
 * - GET    /api/discount/code?code=SPRING10
 * - DELETE /api/discount?id=123
 */
public class DiscountRest extends AbstractRestResource {

    public DiscountRest(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super("discount", req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        final String method = req.getMethod();
        final String path = req.getRequestURI();
        final String queryCode = req.getParameter("code");
        final String idParam = req.getParameter("id");
        Message message = null;

        try {
            if ("GET".equals(method)) {
                if (path.endsWith("/all")) {
                    handleGetAllDiscounts();
                } else if (path.endsWith("/active")) {
                    handleGetActiveDiscounts();
                } else if (path.contains("/code") && queryCode != null) {
                    handleGetDiscountByCode(queryCode);
                } else {
                    res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    message = new Message("Invalid GET endpoint for discount.", "E400", "Specify /all, /active or /code?code=XXX");
                    message.toJSON(res.getOutputStream());
                }

            } else if ("DELETE".equals(method) && idParam != null) {
                handleDeleteDiscount(idParam);
            } else {
                res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                message = new Message("Unsupported HTTP method for discount.", "405", "Only GET and DELETE are supported.");
                message.toJSON(res.getOutputStream());
            }

        } catch (Exception e) {
            LOGGER.error("DiscountRest - Error processing request", e);
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            message = new Message("Internal error in DiscountRest", "E500", e.getMessage());
            message.toJSON(res.getOutputStream());
        }
    }

    private void handleGetAllDiscounts() throws Exception {
        List<Discount> discounts = new GetAllDiscountsDAO(con).access().getOutputParam();
        res.setContentType("application/json;charset=UTF-8");
        ObjectMapper mapper = new ObjectMapper();
        String discountjson = mapper.writeValueAsString(discounts);
        Message message = new Message("All discounts retrieved.", "200", discountjson);
        message.toJSON(res.getOutputStream());
    }

    private void handleGetActiveDiscounts() throws Exception {
        List<Discount> discounts = new GetAllActiveDiscountsDAO(con).access().getOutputParam();
        ObjectMapper mapper = new ObjectMapper();
        String discountjson = mapper.writeValueAsString(discounts);
        res.setContentType("application/json;charset=UTF-8");
        Message message = new Message("Active discounts retrieved.", "200", discountjson);
        message.toJSON(res.getOutputStream());
    }

    private void handleGetDiscountByCode(String code) throws Exception {
        Discount discount = new GetDiscountByCodeDAO(con, code).access().getOutputParam();
        
        if (discount == null) {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            Message message = new Message("No discount found for code: " + code, "E404", "Discount not found.");
            message.toJSON(res.getOutputStream());
            return;
        }

        res.setContentType("application/json;charset=UTF-8");
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(res.getOutputStream(), discount); // serialize the discount as JSON
    }

    private void handleDeleteDiscount(String idParam) throws Exception {
        try {
            int discountId = Integer.parseInt(idParam);
            boolean success = new DeleteDiscountDAO(con, discountId).access().getOutputParam();
            if (success) {
                res.setStatus(HttpServletResponse.SC_OK);
                new Message("Discount deleted successfully.", "200", null).toJSON(res.getOutputStream());
            } else {
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                new Message("No discount found to delete.", "E404", null).toJSON(res.getOutputStream());
            }
        } catch (NumberFormatException ex) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            new Message("Invalid discount ID format.", "E400", "ID must be numeric.").toJSON(res.getOutputStream());
        }
    }
}
