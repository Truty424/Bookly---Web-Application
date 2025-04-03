package it.unipd.bookly.dao.book;

import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;

import static it.unipd.bookly.dao.book.BookQueries.UPDATE_BOOK_STOCK;

/**
 * DAO to update only the stock quantity of a book.
 */
public class UpdateBookStockDAO extends AbstractDAO<Void> {

    private final int bookId;
    private final int stockQuantity;

    /**
     * Constructor.
     *
     * @param con           Database connection
     * @param bookId        Book ID to update
     * @param stockQuantity New stock quantity
     */
    public UpdateBookStockDAO(final Connection con, int bookId, int stockQuantity) {
        super(con);
        this.bookId = bookId;
        this.stockQuantity = stockQuantity;
    }

    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmnt = con.prepareStatement(UPDATE_BOOK_STOCK)) {
            stmnt.setInt(1, stockQuantity);
            stmnt.setInt(2, bookId);
            stmnt.execute();

            LOGGER.info("Stock quantity for book ID {} updated to {}.", bookId, stockQuantity);
        } catch (Exception ex) {
            LOGGER.error("Failed to update stock for book ID {}: {}", bookId, ex.getMessage());
        }
    }
}
