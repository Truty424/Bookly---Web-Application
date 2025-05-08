package it.unipd.bookly.dao.review;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import it.unipd.bookly.dao.AbstractDAO;
import static it.unipd.bookly.dao.review.ReviewQueries.UPDATE_REVIEW_DISLIKES;

/**
 * DAO to update the number of likes and dislikes for a specific review.
 */
public class UpdateReviewDisLikesDAO extends AbstractDAO<Boolean> {

    private final int reviewId;
    private final int numberOfDisLikes;

    public UpdateReviewDisLikesDAO(Connection con, int reviewId, int numberOfDisLikes) {
        super(con);
        this.reviewId = reviewId;
        this.numberOfDisLikes = Math.max(0, numberOfDisLikes);
    }

    @Override
    protected void doAccess() throws SQLException {
        try (PreparedStatement stmt = con.prepareStatement(UPDATE_REVIEW_DISLIKES)) {

            int affectedRows = stmt.executeUpdate();
            this.outputParam = affectedRows > 0;

            if (outputParam) {
                LOGGER.info("Successfully updated review ID {}: likes={}, dislikes={}", reviewId, numberOfDisLikes);
            } else {
                LOGGER.warn("No review found with ID {}. No update performed.", reviewId);
            }

        } catch (SQLException e) {
            LOGGER.error("Error updating likes/dislikes for review ID {}: {}", reviewId, e.getMessage(), e);
            throw e;
        }
    }
}
