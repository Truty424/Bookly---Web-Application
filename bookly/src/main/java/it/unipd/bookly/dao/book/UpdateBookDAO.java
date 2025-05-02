package it.unipd.bookly.dao.book;

import it.unipd.bookly.Resource.Image;
import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;

import static it.unipd.bookly.dao.book.BookQueries.UPDATE_BOOK;

/**
 * DAO to update book information, including optional image update.
 */
public class UpdateBookDAO extends AbstractDAO<Boolean> {

    private final int bookId;
    private final String title;
    private final String language;
    private final String isbn;
    private final double price;
    private final String edition;
    private final int publicationYear;
    private final int numberOfPages;
    private final int stockQuantity;
    private final double averageRate;
    private final String summary;
    private final Image image;  // Optional image to update

    /**
     * Constructor for book update.
     *
     * @param con DB connection
     * @param bookId Book ID to update
     * @param title Updated title
     * @param language Updated language
     * @param isbn Updated ISBN
     * @param price Updated price
     * @param edition Updated edition
     * @param publicationYear Updated publication year
     * @param numberOfPages Updated page count
     * @param stockQuantity Updated stock
     * @param averageRate Updated average rating
     * @param summary Updated summary
     * @param image Optional image (can be null if no update to image is needed)
     */
    public UpdateBookDAO(final Connection con, int bookId, String title, String language, String isbn,
                         double price, String edition, int publicationYear, int numberOfPages,
                         int stockQuantity, double averageRate, String summary, Image image) {
        super(con);
        this.bookId = bookId;
        this.title = title;
        this.language = language;
        this.isbn = isbn;
        this.price = price;
        this.edition = edition;
        this.publicationYear = publicationYear;
        this.numberOfPages = numberOfPages;
        this.stockQuantity = stockQuantity;
        this.averageRate = averageRate;
        this.summary = summary;
        this.image = image;
    }

    @Override
    protected void doAccess() throws Exception {
        boolean previousAutoCommit = con.getAutoCommit();
        con.setAutoCommit(false);  // Ensure atomicity

        try {
            // Update main book info
            try (PreparedStatement stmt = con.prepareStatement(UPDATE_BOOK)) {
                stmt.setString(1, title);
                stmt.setString(2, language);
                stmt.setString(3, isbn);
                stmt.setDouble(4, price);
                stmt.setString(5, edition);
                stmt.setInt(6, publicationYear);
                stmt.setInt(7, numberOfPages);
                stmt.setInt(8, stockQuantity);
                stmt.setDouble(9, averageRate);
                stmt.setString(10, summary);
                stmt.setInt(11, bookId);

                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    LOGGER.info("Book with ID {} successfully updated ({} row(s) affected).", bookId, rowsAffected);
                } else {
                    LOGGER.warn("No book found with ID {}. No rows were updated.", bookId);
                }
            }

            con.commit();
            this.outputParam = true;

        } catch (Exception ex) {
            LOGGER.error("Error updating book with ID {}: {}", bookId, ex.getMessage(), ex);
            try {
                con.rollback();
                LOGGER.warn("Transaction rolled back for book ID {}", bookId);
            } catch (Exception rollbackEx) {
                LOGGER.error("Rollback failed after error: {}", rollbackEx.getMessage(), rollbackEx);
            }
            throw ex;  // Re-throw to propagate error
        } finally {
            con.setAutoCommit(previousAutoCommit);  // Restore state
        }
    }
}
