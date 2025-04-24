package it.unipd.bookly.rest.book;

import java.io.IOException;
import java.sql.Connection;

import it.unipd.bookly.Resource.Message;
import it.unipd.bookly.dao.book.UpdateBookStockDAO;
import it.unipd.bookly.rest.AbstractRestResource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Handles book stock updates:
 * PUT /api/book/stock?id=123&amp;quantity=10
 */
public class BookStockRest extends AbstractRestResource {

    public BookStockRest(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super("book-stock", req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        final String method = req.getMethod();

        switch (method) {
            case "PUT" -> handleUpdateStock();
            default -> {
                res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                new Message("Method not allowed", "E405", "Only PUT is supported for stock updates.")
                        .toJSON(res.getOutputStream());
            }
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

        } catch (NumberFormatException e) {
            sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid format", "E401",
                    "'id' and 'quantity' must be valid integers.");
        } catch (Exception e) {
            LOGGER.error("Error updating book stock", e);
            sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server error", "E500",
                    "Internal error while updating stock: " + e.getMessage());
        }
    }

    private void sendError(int status, String title, String code, String details) throws IOException {
        res.setStatus(status);
        new Message(title, code, details).toJSON(res.getOutputStream());
    }
}
