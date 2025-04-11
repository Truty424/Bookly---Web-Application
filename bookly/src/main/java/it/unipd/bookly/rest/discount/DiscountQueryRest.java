package it.unipd.bookly.rest.discount;

import com.fasterxml.jackson.databind.ObjectMapper;
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
 * @Handles:
 * - GET /api/discount/query?code=CODE → Retrieves a discount by its code.
 * - GET /api/discount/query/valid?code=CODE → Retrieves only if the discount is valid (i.e., not expired).
 * 
 * Query Parameter:
 * - code: Required. The discount code to search.
 * 
 * Only the GET method is supported. All others return 405.
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

        try {
            switch (method) {
                case "GET" -> {
                    if (code == null || code.isBlank()) {
                        sendError("Missing discount code parameter.", "E400", "Query parameter 'code' is required.", HttpServletResponse.SC_BAD_REQUEST);
                    } else if (path.contains("/valid")) {
                        handleGetValidDiscount(code);
                    } else {
                        handleGetDiscount(code);
                    }
                }
                default -> sendError("Only GET method is supported.", "405", "Use GET for discount queries.", HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            }
        } catch (Exception e) {
            LOGGER.error("Error in DiscountQueryRest", e);
            sendError("Internal error in discount query.", "E500", e.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void handleGetDiscount(String code) throws Exception {
        Discount discount = new GetDiscountByCodeDAO(con, code).access().getOutputParam();

        if (discount == null) {
            sendError("Discount code not found.", "E404", "No discount found for code: " + code, HttpServletResponse.SC_NOT_FOUND);
        } else {
            sendJsonResponse(discount, "Discount retrieved.", "200");
        }
    }

    private void handleGetValidDiscount(String code) throws Exception {
        Discount discount = new GetValidDiscountByCodeDAO(con, code).access().getOutputParam();

        if (discount == null) {
            sendError("No valid discount found.", "E404", "Code may be expired or invalid: " + code, HttpServletResponse.SC_NOT_FOUND);
        } else {
            sendJsonResponse(discount, "Valid discount retrieved.", "200");
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
