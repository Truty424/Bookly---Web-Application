package it.unipd.bookly.dao.book;

import it.unipd.bookly.Resource.Book;
import it.unipd.bookly.Resource.Image;
import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;

import static it.unipd.bookly.dao.book.BookQueries.INSERT_BOOK;
import static it.unipd.bookly.dao.book.BookQueries.INSERT_BOOK_IMAGE;

/**
 * DAO to insert a new book (with optional image).
 */
public class InsertBookDAO extends AbstractDAO<Boolean> {

    private final Book book;

    public InsertBookDAO(final Connection con, final Book book) {
        super(con);
        this.book = book;
    }

    @Override
    protected void doAccess() throws Exception {
        boolean previousAutoCommit = con.getAutoCommit();
        boolean success = false;

        try {
            con.setAutoCommit(false);  // Begin transaction

            // Insert image if available
            Image image = book.getImage();
            if (image != null) {
                try (PreparedStatement stmtImg = con.prepareStatement(INSERT_BOOK_IMAGE)) {
                    stmtImg.setString(1, book.getTitle());
                    stmtImg.setBytes(2, image.getPhoto());
                    stmtImg.setString(3, image.getPhotoMediaType());
                    int imgRows = stmtImg.executeUpdate();
                    LOGGER.info("Image inserted for book '{}', rows affected: {}", book.getTitle(), imgRows);
                }
            }

            // Insert book metadata
            try (PreparedStatement stmtBook = con.prepareStatement(INSERT_BOOK)) {
                stmtBook.setString(1, book.getTitle());
                stmtBook.setString(2, book.getLanguage());
                stmtBook.setString(3, book.getIsbn());
                stmtBook.setDouble(4, book.getPrice());
                stmtBook.setString(5, book.getEdition());
                stmtBook.setInt(6, book.getPublication_year());
                stmtBook.setInt(7, book.getNumber_of_pages());
                stmtBook.setInt(8, book.getStockQuantity());
                stmtBook.setDouble(9, book.getAverage_rate());
                stmtBook.setString(10, book.getSummary());
                int bookRows = stmtBook.executeUpdate();
                LOGGER.info("Book '{}' inserted into database, rows affected: {}", book.getTitle(), bookRows);
                success = bookRows > 0;
            }

            con.commit();
            LOGGER.info("Transaction committed for book '{}'", book.getTitle());

        } catch (Exception ex) {
            LOGGER.error("Error inserting book '{}': {}", book.getTitle(), ex.getMessage(), ex);
            try {
                con.rollback();
                LOGGER.warn("Transaction rolled back for book '{}'", book.getTitle());
            } catch (Exception rollbackEx) {
                LOGGER.error("Rollback failed: {}", rollbackEx.getMessage(), rollbackEx);
            }
            throw ex;  // Rethrow so caller knows it failed
        } finally {
            con.setAutoCommit(previousAutoCommit);  // Restore previous state
        }

        this.outputParam = success;
    }
}
