package it.unipd.bookly.rest.book;

import java.io.IOException;
import java.sql.Connection;

import it.unipd.bookly.Resource.Message;
import it.unipd.bookly.dao.book.UpdateBookStockDAO;
import it.unipd.bookly.rest.AbstractRestResource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * REST endpoint to update the stock quantity of a book.
 * 
 * Endpoint: PUT /api/book/stock?id=123&quantity=10
 */
public class BookStockRest extends AbstractRestResource {

    public BookStockRest(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super("book-stock", req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        String bookIdParam = req.getParameter("id");
        String quantityParam = req.getParameter("quantity");

        Message message;

        // Validate parameters
        if (bookIdParam == null || quantityParam == null) {
            message = new Message("Missing 'id' or 'quantity' parameter.", "E400", "Both 'id' and 'quantity' query parameters are required.");
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            message.toJSON(res.getOutputStream());
            return;
        }

        try {
            int bookId = Integer.parseInt(bookIdParam);
            int quantity = Integer.parseInt(quantityParam);

            // DAO access
            new UpdateBookStockDAO(con, bookId, quantity).access();

            message = new Message("Book stock updated successfully.", "200", "Stock updated for book ID " + bookId);
            res.setStatus(HttpServletResponse.SC_OK);
            message.toJSON(res.getOutputStream());

        } catch (NumberFormatException ex) {
            message = new Message("Invalid ID or quantity format.", "E401", "Both 'id' and 'quantity' must be integers.");
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            message.toJSON(res.getOutputStream());
        } catch (Exception ex) {
            LOGGER.error("Error updating stock for book", ex);
            message = new Message("Internal server error while updating book stock.", "E500", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            message.toJSON(res.getOutputStream());
        }
    }
}
