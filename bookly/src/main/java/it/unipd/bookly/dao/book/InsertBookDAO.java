package it.unipd.bookly.dao.book;

import it.unipd.bookly.Resource.Book;
import it.unipd.bookly.Resource.Image;
import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;

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

            int generatedBookId;

            // Step 1: Insert book metadata and retrieve generated book_id
            try (PreparedStatement stmtBook = con.prepareStatement(INSERT_BOOK, Statement.RETURN_GENERATED_KEYS)) {
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

                if (bookRows == 0) {
                    throw new Exception("Inserting book failed: no rows affected.");
                }

                try (ResultSet generatedKeys = stmtBook.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        generatedBookId = generatedKeys.getInt(1);
                        book.setBookId(generatedBookId);
                        LOGGER.info("Generated book_id for '{}': {}", book.getTitle(), generatedBookId);
                    } else {
                        throw new Exception("Inserting book failed: no ID obtained.");
                    }
                }
            }

            // Step 2: Insert image if available
            Image image = book.getImage();
            if (image != null) {
                try (PreparedStatement stmtImg = con.prepareStatement(INSERT_BOOK_IMAGE)) {
                    stmtImg.setInt(1, generatedBookId);
                    stmtImg.setBytes(2, image.getPhoto());
                    stmtImg.setString(3, image.getPhotoMediaType());
                    int imgRows = stmtImg.executeUpdate();
                    LOGGER.info("Image inserted for book '{}', rows affected: {}", book.getTitle(), imgRows);
                }
            } else {
                LOGGER.debug("No image provided for book '{}'. Skipping image insert.", book.getTitle());
            }

            con.commit();
            LOGGER.info("Transaction committed successfully for book '{}'", book.getTitle());
            success = true;

        } catch (Exception ex) {
            LOGGER.error("Error inserting book '{}': {}", book.getTitle(), ex.getMessage(), ex);
            try {
                con.rollback();
                LOGGER.warn("Transaction rolled back for book '{}'", book.getTitle());
            } catch (Exception rollbackEx) {
                LOGGER.error("Rollback failed: {}", rollbackEx.getMessage(), rollbackEx);
            }
            throw ex;  // Re-throw so caller knows it failed
        } finally {
            con.setAutoCommit(previousAutoCommit);  // Always restore previous state
        }

        this.outputParam = success;
    }
}
