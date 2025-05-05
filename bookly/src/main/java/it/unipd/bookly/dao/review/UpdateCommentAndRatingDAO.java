package it.unipd.bookly.dao.review;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import it.unipd.bookly.dao.AbstractDAO;
import static it.unipd.bookly.dao.review.ReviewQueries.UPDATE_COMMENT_AND_RATING;

/**
 * DAO to update the review text and rating for a specific review.
 */
public class UpdateCommentAndRatingDAO extends AbstractDAO<Boolean> {

    private final int reviewId;
    private final String reviewText;
    private final int rating;

    public UpdateCommentAndRatingDAO(Connection con, int reviewId, String reviewText, int rating) {
        super(con);
        this.reviewId = reviewId;
        this.reviewText = reviewText != null ? reviewText.trim() : "";
        this.rating = Math.max(1, Math.min(5, rating)); // Enforce 1–5 rating range
    }

    @Override
    protected void doAccess() throws SQLException {
        if (reviewText.isEmpty()) {
            LOGGER.warn("Review update failed: empty text for review ID {}", reviewId);
            this.outputParam = false;
            return;
        }

        try (PreparedStatement stmt = con.prepareStatement(UPDATE_COMMENT_AND_RATING)) {
            stmt.setString(1, reviewText);
            stmt.setInt(2, rating);
            stmt.setInt(3, reviewId);

            int affectedRows = stmt.executeUpdate();
            this.outputParam = affectedRows > 0;

            if (outputParam) {
                LOGGER.info("Successfully updated review ID {}: new rating = {}, new text length = {}", reviewId, rating, reviewText.length());
            } else {
                LOGGER.warn("No review found with ID {}. No update made.", reviewId);
            }

        } catch (SQLException e) {
            LOGGER.error("Failed to update review ID {}: {}", reviewId, e.getMessage(), e);
            throw e;
        }
    }
}
