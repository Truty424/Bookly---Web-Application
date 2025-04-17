package it.unipd.bookly.dao.review;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import it.unipd.bookly.dao.AbstractDAO;
import static it.unipd.bookly.dao.review.ReviewQueries.UPDATE_REVIEW_LIKES_DISLIKES;

/**
 * DAO to update the number of likes and dislikes for a specific review.
 */
public class UpdateReviewLikesDislikesDAO extends AbstractDAO<Boolean> {

    private final int reviewId;
    private final int numberOfLikes;
    private final int numberOfDislikes;

    public UpdateReviewLikesDislikesDAO(Connection con, int reviewId, int numberOfLikes, int numberOfDislikes) {
        super(con);
        this.reviewId = reviewId;
        this.numberOfLikes = Math.max(0, numberOfLikes);
        this.numberOfDislikes = Math.max(0, numberOfDislikes);
    }

    @Override
    protected void doAccess() throws SQLException {
        try (PreparedStatement stmt = con.prepareStatement(UPDATE_REVIEW_LIKES_DISLIKES)) {
            stmt.setInt(1, numberOfLikes);
            stmt.setInt(2, numberOfDislikes);
            stmt.setInt(3, reviewId);

            int affectedRows = stmt.executeUpdate();
            this.outputParam = affectedRows > 0;

            if (outputParam) {
                LOGGER.info("Successfully updated review ID {}: likes={}, dislikes={}", reviewId, numberOfLikes, numberOfDislikes);
            } else {
                LOGGER.warn("No review found with ID {}. No update performed.", reviewId);
            }

        } catch (SQLException e) {
            LOGGER.error("Error updating likes/dislikes for review ID {}: {}", reviewId, e.getMessage(), e);
            throw e;
        }
    }
}
