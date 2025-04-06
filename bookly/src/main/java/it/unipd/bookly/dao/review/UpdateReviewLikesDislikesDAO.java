package it.unipd.bookly.dao.review;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import it.unipd.bookly.dao.AbstractDAO;
import static it.unipd.bookly.dao.review.ReviewQueries.UPDATE_REVIEW_LIKES_DISLIKES;

/**
 * DAO responsible for updating the number of likes and dislikes
 * associated with a specific review.
 */
public class UpdateReviewLikesDislikesDAO extends AbstractDAO<Boolean> {

    private final int reviewId;
    private final int numberOfLikes;
    private final int numberOfDislikes;

    /**
     * Constructs the DAO to update likes/dislikes for a review.
     *
     * @param con               Database connection
     * @param reviewId          ID of the review to update
     * @param numberOfLikes     New like count
     * @param numberOfDislikes  New dislike count
     */
    public UpdateReviewLikesDislikesDAO(Connection con, int reviewId, int numberOfLikes, int numberOfDislikes) {
        super(con);
        this.reviewId = reviewId;
        this.numberOfLikes = numberOfLikes;
        this.numberOfDislikes = numberOfDislikes;
    }

    @Override
    protected void doAccess() throws SQLException {
        try (PreparedStatement stmt = con.prepareStatement(UPDATE_REVIEW_LIKES_DISLIKES)) {
            stmt.setInt(1, numberOfLikes);
            stmt.setInt(2, numberOfDislikes);
            stmt.setInt(3, reviewId);

            int affectedRows = stmt.executeUpdate();
            this.outputParam = affectedRows > 0;

            LOGGER.info("Updated review ID {} with likes = {}, dislikes = {}. Success = {}",
                    reviewId, numberOfLikes, numberOfDislikes, outputParam);
        } catch (SQLException e) {
            LOGGER.error("Failed to update likes/dislikes for review ID {}: {}", reviewId, e.getMessage());
            throw e; // Let the caller handle or log this
        }
    }
}
