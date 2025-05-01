package it.unipd.bookly.dao.book;

import java.sql.Connection;
import java.sql.PreparedStatement;

import it.unipd.bookly.dao.AbstractDAO;

import static it.unipd.bookly.dao.book.BookQueries.UPDATE_BOOK_STOCK;

/**
 * DAO to update only the stock quantity of a book.
 */
public class UpdateBookStockDAO extends AbstractDAO<Boolean> {

    private final int bookId;
    private final int stockQuantity;

    /**
     * Constructor.
     *
     * @param con Database connection
     * @param bookId Book ID to update
     * @param stockQuantity New stock quantity
     */
    public UpdateBookStockDAO(final Connection con, int bookId, int stockQuantity) {
        super(con);
        this.bookId = bookId;
        this.stockQuantity = stockQuantity;
    }

    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmt = con.prepareStatement(UPDATE_BOOK_STOCK)) {
            stmt.setInt(1, stockQuantity);
            stmt.setInt(2, bookId);

            int rowsAffected = stmt.executeUpdate();
            this.outputParam = rowsAffected > 0;

            if (rowsAffected > 0) {
                LOGGER.info("Stock quantity for book ID {} updated to {} ({} row(s) affected).", bookId, stockQuantity, rowsAffected);
            } else {
                LOGGER.warn("No book found with ID {}. Stock update skipped.", bookId);
            }

        } catch (Exception ex) {
            LOGGER.error("Failed to update stock for book ID {}: {}", bookId, ex.getMessage(), ex);
            throw ex;  // Important: propagate exception
        }
    }
}
