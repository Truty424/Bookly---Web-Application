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

    private final int bookId;

    /**
     * Constructs a DAO to count reviews for a specific book.
     *
     * @param con     the database connection
     * @param bookId  the ID of the book for which to count reviews
     */
    public CountReviewsForBookDAO(Connection con, int bookId) {
        super(con);
        this.bookId = bookId;
    }

    /**
     * Executes the SQL query to count reviews for the book.
     *
     * @throws Exception if any SQL or connection error occurs
     */
    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmt = con.prepareStatement(COUNT_REVIEWS_FOR_BOOK)) {
            stmt.setInt(1, bookId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    outputParam = rs.getInt(1);
                    LOGGER.info("Found {} reviews for book ID {}", outputParam, bookId);
                } else {
                    outputParam = 0;
                    LOGGER.warn("No count result returned for book ID {}", bookId);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error counting reviews for book ID {}: {}", bookId, e.getMessage(), e);
            throw e;
        }
    }
}
