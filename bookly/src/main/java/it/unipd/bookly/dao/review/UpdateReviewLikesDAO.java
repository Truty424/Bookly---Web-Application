package it.unipd.bookly.dao.review;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import it.unipd.bookly.dao.AbstractDAO;
import static it.unipd.bookly.dao.review.ReviewQueries.UPDATE_REVIEW_LIKES;

/**
 * DAO to update the number of likes and dislikes for a specific review.
 */
public class UpdateReviewLikesDAO extends AbstractDAO<Boolean> {

    private final int reviewId;
    private final int numberOfLikes;

    public UpdateReviewLikesDAO(Connection con, int reviewId, int numberOfLikes) {
        super(con);
        this.reviewId = reviewId;
        this.numberOfLikes = Math.max(0, numberOfLikes);
    }

    @Override
    protected void doAccess() throws SQLException {
        try (PreparedStatement stmt = con.prepareStatement(UPDATE_REVIEW_LIKES)) {
            stmt.setInt(1, numberOfLikes);

            int affectedRows = stmt.executeUpdate();
            this.outputParam = affectedRows > 0;

            if (outputParam) {
                LOGGER.info("Successfully updated review ID {}: likes={}, dislikes={}", reviewId, numberOfLikes);
            } else {
                LOGGER.warn("No review found with ID {}. No update performed.", reviewId);
            }

        } catch (SQLException e) {
            LOGGER.error("Error updating likes/dislikes for review ID {}: {}", reviewId, e.getMessage(), e);
            throw e;
        }
    }
}
