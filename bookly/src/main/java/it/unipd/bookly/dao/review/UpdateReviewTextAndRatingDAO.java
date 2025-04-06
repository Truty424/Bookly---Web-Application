package it.unipd.bookly.dao.review;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import it.unipd.bookly.dao.AbstractDAO;

import static it.unipd.bookly.dao.review.ReviewQueries.UPDATE_REVIEW_TEXT_AND_RATING;

/**
 * DAO responsible for updating the text and rating of a review.
 */
public class UpdateReviewTextAndRatingDAO extends AbstractDAO<Boolean> {

    private final int reviewId;
    private final String reviewText;
    private final int rating;

    /**
     * Constructs DAO to update review content.
     *
     * @param con        Database connection
     * @param reviewId   ID of the review to update
     * @param reviewText New review text
     * @param rating     New rating value
     */
    public UpdateReviewTextAndRatingDAO(Connection con, int reviewId, String reviewText, int rating) {
        super(con);
        this.reviewId = reviewId;
        this.reviewText = reviewText;
        this.rating = rating;
    }

    @Override
    protected void doAccess() throws SQLException {
        try (PreparedStatement stmt = con.prepareStatement(UPDATE_REVIEW_TEXT_AND_RATING)) {
            stmt.setString(1, reviewText);
            stmt.setInt(2, rating);
            stmt.setInt(3, reviewId);

            int affectedRows = stmt.executeUpdate();
            this.outputParam = affectedRows > 0;

            LOGGER.info("Updated review ID {} with new text and rating: {} stars. Success = {}",
                    reviewId, rating, outputParam);
        } catch (SQLException e) {
            LOGGER.error("Failed to update review ID {}: {}", reviewId, e.getMessage());
            throw e;
        }
    }
}
