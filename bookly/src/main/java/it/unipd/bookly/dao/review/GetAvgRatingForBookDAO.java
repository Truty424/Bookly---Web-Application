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

    private final int bookId;

    /**
     * Constructs a DAO for retrieving the average rating of a book.
     *
     * @param con     The database connection.
     * @param bookId  The ID of the book.
     */
    public GetAvgRatingForBookDAO(Connection con, int bookId) {
        super(con);
        this.bookId = bookId;
    }

    /**
     * Executes the query to compute the average rating.
     *
     * @throws Exception if any SQL error occurs.
     */
    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmt = con.prepareStatement(GET_AVG_RATING_FOR_BOOK)) {
            stmt.setInt(1, bookId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    double avg = rs.getDouble(1);
                    this.outputParam = rs.wasNull() ? 0.0 : avg;
                    LOGGER.info("Average rating for book ID {} is {}", bookId, outputParam);
                } else {
                    this.outputParam = 0.0;
                    LOGGER.warn("No average rating found for book ID {}", bookId);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Failed to retrieve average rating for book ID {}: {}", bookId, e.getMessage(), e);
            throw e;
        }
    }
}
