package it.unipd.bookly.dao.book;

import java.sql.Connection;
import java.sql.PreparedStatement;

import it.unipd.bookly.dao.AbstractDAO;

import static it.unipd.bookly.dao.book.BookQueries.UPDATE_BOOK;

/**
 * DAO to update book information.
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
     */
    public UpdateBookDAO(final Connection con, int bookId, String title, String language, String isbn,
                         double price, String edition, int publicationYear, int numberOfPages,
                         int stockQuantity, double averageRate, String summary) {
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
    }

    @Override
    protected void doAccess() throws Exception {
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
            this.outputParam = rowsAffected > 0;

            if (rowsAffected > 0) {
                LOGGER.info("Book with ID {} successfully updated ({} row(s) affected).", bookId, rowsAffected);
            } else {
                LOGGER.warn("No book found with ID {}. No rows were updated.", bookId);
            }

        } catch (Exception ex) {
            LOGGER.error("Error updating book with ID {}: {}", bookId, ex.getMessage(), ex);
            throw ex;  // Important: propagate the error
        }
    }
}
