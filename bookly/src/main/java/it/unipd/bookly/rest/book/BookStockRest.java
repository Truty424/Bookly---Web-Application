package it.unipd.bookly.rest.book;

import java.io.IOException;
import java.sql.Connection;

import it.unipd.bookly.Resource.Message;
import it.unipd.bookly.dao.book.UpdateBookStockDAO;
import it.unipd.bookly.rest.AbstractRestResource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Handles book stock updates:<br>
 * <code>PUT /api/books/stock?id=123&amp;quantity=10</code>
 */
public class BookStockRest extends AbstractRestResource {

    public BookStockRest(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super("book-stock", req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        res.setContentType("application/json;charset=UTF-8");  // Always set JSON response type

        final String method = req.getMethod();

        if ("PUT".equalsIgnoreCase(method)) {
            handleUpdateStock();
        } else {
            sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Method not allowed", "E405",
                    "Only PUT is supported for stock updates.");
        }
    }

    private void handleUpdateStock() throws IOException {
        final String bookIdParam = req.getParameter("id");
        final String quantityParam = req.getParameter("quantity");

        if (bookIdParam == null || quantityParam == null) {
            sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters", "E400",
                    "Both 'id' and 'quantity' query parameters are required.");
            return;
        }

        try {
            int bookId = Integer.parseInt(bookIdParam);
            int quantity = Integer.parseInt(quantityParam);

            new UpdateBookStockDAO(con, bookId, quantity).access();

            res.setStatus(HttpServletResponse.SC_OK);
            new Message("Book stock updated successfully", "200", "Stock updated for book ID: " + bookId)
                    .toJSON(res.getOutputStream());

            LOGGER.info("Stock for book ID {} successfully updated to {}", bookId, quantity);

        } catch (NumberFormatException e) {
            LOGGER.warn("Invalid input for book stock update: id='{}', quantity='{}'", bookIdParam, quantityParam);
            sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid format", "E401",
                    "'id' and 'quantity' must be valid integers.");
        } catch (Exception e) {
            LOGGER.error("Error updating book stock for ID '{}'", bookIdParam, e);
            sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server error", "E500",
                    "Internal error while updating stock: " + e.getMessage());
        }
    }

    private void sendError(int status, String title, String code, String details) throws IOException {
        res.setStatus(status);
        res.setContentType("application/json;charset=UTF-8");
        new Message(title, code, details).toJSON(res.getOutputStream());
    }
}
