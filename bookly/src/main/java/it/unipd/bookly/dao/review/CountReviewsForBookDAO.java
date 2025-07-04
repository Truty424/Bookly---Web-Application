package it.unipd.bookly.dao.review;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import it.unipd.bookly.dao.AbstractDAO;

import static it.unipd.bookly.dao.review.ReviewQueries.COUNT_REVIEWS_FOR_BOOK;

/**
 * DAO to count the number of reviews associated with a specific book.
 * This is typically used for displaying the total number of reviews on book detail pages
 * or for statistical/analytics features in the application.
 */
public class CountReviewsForBookDAO extends AbstractDAO<Integer> {

    private final int book_id;

    /**
     * Constructs a DAO to count reviews for a specific book.
     *
     * @param con     the database connection
     * @param book_id  the ID of the book for which to count reviews
     */
    public CountReviewsForBookDAO(Connection con, int book_id) {
        super(con);
        this.book_id = book_id;
    }

    /**
     * Executes the SQL query to count reviews for the book.
     *
     * @throws Exception if any SQL or connection error occurs
     */
    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmt = con.prepareStatement(COUNT_REVIEWS_FOR_BOOK)) {
            stmt.setInt(1, book_id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    outputParam = rs.getInt(1);
                    LOGGER.info("Found {} reviews for book ID {}", outputParam, book_id);
                } else {
                    outputParam = 0;
                    LOGGER.warn("No count result returned for book ID {}", book_id);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error counting reviews for book ID {}: {}", book_id, e.getMessage(), e);
            throw e;
        }
    }
}
