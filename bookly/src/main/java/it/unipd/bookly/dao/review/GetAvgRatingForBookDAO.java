package it.unipd.bookly.dao.review;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import it.unipd.bookly.dao.AbstractDAO;

import static it.unipd.bookly.dao.review.ReviewQueries.GET_AVG_RATING_FOR_BOOK;

/**
 * DAO to compute the average rating of a specific book.
 */
public class GetAvgRatingForBookDAO extends AbstractDAO<Double> {

    private final int book_id;

    /**
     * Constructs a DAO for retrieving the average rating of a book.
     *
     * @param con     The database connection.
     * @param book_id  The ID of the book.
     */
    public GetAvgRatingForBookDAO(Connection con, int book_id) {
        super(con);
        this.book_id = book_id;
    }

    /**
     * Executes the query to compute the average rating.
     *
     * @throws Exception if any SQL error occurs.
     */
    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmt = con.prepareStatement(GET_AVG_RATING_FOR_BOOK)) {
            stmt.setInt(1, book_id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    double avg = rs.getDouble(1);
                    this.outputParam = rs.wasNull() ? 0.0 : avg;
                    LOGGER.info("Average rating for book ID {} is {}", book_id, outputParam);
                } else {
                    this.outputParam = 0.0;
                    LOGGER.warn("No average rating found for book ID {}", book_id);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Failed to retrieve average rating for book ID {}: {}", book_id, e.getMessage(), e);
            throw e;
        }
    }
}
