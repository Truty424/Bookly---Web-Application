package it.unipd.bookly.dao.book;

import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;

import static it.unipd.bookly.dao.book.BookQueries.UPDATE_BOOK;

/**
 * DAO to update book information.
 */
public class UpdateBookDAO extends AbstractDAO<Void> {

    private final int book_id;
    private final String title;
    private final String language;
    private final String isbn;
    private final double price;
    private final String edition;
    private final int publication_year;
    private final int number_of_pages;
    private final int stock_quantity;
    private final double average_rate;
    private final String summary;

    /**
     * Constructor for book update.
     *
     * @param con                DB connection
     * @param book_id            Book ID to update
     * @param title              Updated title
     * @param language           Updated language
     * @param isbn               Updated ISBN
     * @param price              Updated price
     * @param edition            Updated edition
     * @param publication_year   Updated publication year
     * @param number_of_pages    Updated page count
     * @param stock_quantity     Updated stock
     * @param average_rate       Updated average rating
     * @param summary            Updated summary
     */
    public UpdateBookDAO(final Connection con, int book_id, String title, String language, String isbn,
                         double price, String edition, int publication_year, int number_of_pages,
                         int stock_quantity, double average_rate, String summary) {
        super(con);
        this.book_id = book_id;
        this.title = title;
        this.language = language;
        this.isbn = isbn;
        this.price = price;
        this.edition = edition;
        this.publication_year = publication_year;
        this.number_of_pages = number_of_pages;
        this.stock_quantity = stock_quantity;
        this.average_rate = average_rate;
        this.summary = summary;
    }

    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmnt = con.prepareStatement(UPDATE_BOOK)) {
            stmnt.setString(1, title);
            stmnt.setString(2, language);
            stmnt.setString(3, isbn);
            stmnt.setDouble(4, price);
            stmnt.setString(5, edition);
            stmnt.setInt(6, publication_year);
            stmnt.setInt(7, number_of_pages);
            stmnt.setInt(8, stock_quantity);
            stmnt.setDouble(9, average_rate);
            stmnt.setString(10, summary);
            stmnt.setInt(11, book_id);

            stmnt.execute();
            LOGGER.info("Book with ID {} successfully updated.", book_id);
        } catch (Exception ex) {
            LOGGER.error("Error updating book with ID {}: {}", book_id, ex.getMessage());
        }
    }
}
