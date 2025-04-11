package it.unipd.bookly.dao.book;

import java.sql.Connection;
import java.sql.PreparedStatement;

import it.unipd.bookly.dao.AbstractDAO;
import static it.unipd.bookly.dao.book.BookQueries.UPDATE_BOOK_STOCK;

/**
 * DAO to update only the stock quantity of a book.
 */
public class UpdateBookStockDAO extends AbstractDAO<Void> {

    private final int book_id;
    private final int stockQuantity;

    /**
     * Constructor.
     *
     * @param con Database connection
     * @param book_id Book ID to update
     * @param stockQuantity New stock quantity
     */
    public UpdateBookStockDAO(final Connection con, int book_id, int stockQuantity) {
        super(con);
        this.book_id = book_id;
        this.stockQuantity = stockQuantity;
    }

    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmnt = con.prepareStatement(UPDATE_BOOK_STOCK)) {
            stmnt.setInt(1, stockQuantity);
            stmnt.setInt(2, book_id);
            stmnt.execute();

            LOGGER.info("Stock quantity for book ID {} updated to {}.", book_id, stockQuantity);
        } catch (Exception ex) {
            LOGGER.error("Failed to update stock for book ID {}: {}", book_id, ex.getMessage());
        }
    }
}
