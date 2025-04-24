package it.unipd.bookly.rest.discount;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.unipd.bookly.Resource.Discount;
import it.unipd.bookly.Resource.Message;
import it.unipd.bookly.dao.discount.*;
import it.unipd.bookly.rest.AbstractRestResource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

/**
 * Handles: - GET /api/discount/all → Retrieve all discounts. - GET
 * /api/discount/active → Retrieve active (non-expired) discounts. - GET
 * /api/discount/code?code=... → Retrieve a discount by code. - DELETE
 * /api/discount?id=123 → Delete a discount by ID.
 */
public class DiscountRest extends AbstractRestResource {

    public DiscountRest(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super("discount", req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        final String method = req.getMethod();
        final String path = req.getRequestURI();
        final String codeParam = req.getParameter("code");
        final String idParam = req.getParameter("id");

        try {
            switch (method) {
                case "GET" -> {
                    if (path.endsWith("/all")) {
                        handleGetAllDiscounts();
                    } else if (path.endsWith("/active")) {
                        handleGetActiveDiscounts();
                    } else if (path.contains("/code") && codeParam != null) {
                        handleGetDiscountByCode(codeParam);
                    } else {
                        sendError("Invalid GET endpoint.", "E400", "Expected /all, /active, or /code?code=...", HttpServletResponse.SC_BAD_REQUEST);
                    }
                }
                case "DELETE" -> {
                    if (idParam != null) {
                        handleDeleteDiscount(idParam);
                    } else {
                        sendError("Missing discount ID.", "E400", "You must specify 'id' to delete a discount.", HttpServletResponse.SC_BAD_REQUEST);
                    }
                }
                default ->
                    sendError("Unsupported method.", "405", "Only GET and DELETE methods are supported.", HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            }
        } catch (Exception e) {
            LOGGER.error("DiscountRest - Unexpected error", e);
            sendError("Internal server error", "E500", e.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void handleGetAllDiscounts() throws Exception {
        List<Discount> discounts = new GetAllDiscountsDAO(con).access().getOutputParam();
        sendJsonResponse(discounts, "All discounts retrieved.", "200");
    }

    private void handleGetActiveDiscounts() throws Exception {
        List<Discount> discounts = new GetAllActiveDiscountsDAO(con).access().getOutputParam();
        sendJsonResponse(discounts, "Active discounts retrieved.", "200");
    }

    private void handleGetDiscountByCode(String code) throws Exception {
        Discount discount = new GetDiscountByCodeDAO(con, code).access().getOutputParam();
        if (discount == null) {
            sendError("Discount not found", "E404", "No discount found for code: " + code, HttpServletResponse.SC_NOT_FOUND);
        } else {
            sendJsonResponse(discount, "Discount retrieved.", "200");
        }
    }

    private void handleDeleteDiscount(String idParam) throws Exception {
        try {
            int id = Integer.parseInt(idParam);
            boolean success = new DeleteDiscountDAO(con, id).access().getOutputParam();

            if (success) {
                new Message("Discount deleted successfully.", "200", null).toJSON(res.getOutputStream());
                res.setStatus(HttpServletResponse.SC_OK);
            } else {
                sendError("Discount not found.", "E404", "No discount with ID " + id, HttpServletResponse.SC_NOT_FOUND);
            }

        } catch (NumberFormatException ex) {
            sendError("Invalid ID format.", "E400", "Discount ID must be an integer.", HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void sendJsonResponse(Object data, String msg, String code) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(data);
        res.setContentType("application/json;charset=UTF-8");
        res.setStatus(HttpServletResponse.SC_OK);
        new Message(msg, code, json).toJSON(res.getOutputStream());
    }

    private void sendError(String title, String code, String detail, int status) throws IOException {
        res.setStatus(status);
        new Message(title, code, detail).toJSON(res.getOutputStream());
    }
}
