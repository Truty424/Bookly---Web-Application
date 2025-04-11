package it.unipd.bookly.dao.review;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import it.unipd.bookly.dao.AbstractDAO;
import static it.unipd.bookly.dao.review.ReviewQueries.COUNT_REVIEWS_FOR_BOOK;

/**
 * DAO to count how many reviews are associated with a specific book (product).
 * Useful for displaying number of reviews on book detail pages or analytics.
 */
public class CountReviewsForBookDAO extends AbstractDAO<Integer> {

    private final int book_id;

    /**
     * Constructs the DAO with a DB connection and target book ID.
     *
     * @param con Active database connection
     * @param book_id ID of the book to count reviews for
     */
    public CountReviewsForBookDAO(Connection con, int book_id) {
        super(con);
        this.book_id = book_id;
    }

    /**
     * Executes the SQL query to count the number of reviews for the book.
     */
    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmt = con.prepareStatement(COUNT_REVIEWS_FOR_BOOK)) {
            stmt.setInt(1, book_id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    this.outputParam = rs.getInt(1);
                } else {
                    this.outputParam = 0;
                }
            }
        } catch (Exception e) {
            LOGGER.error("Failed to count reviews for book ID {}: {}", book_id, e.getMessage());
            throw e;
        }
    }
}
