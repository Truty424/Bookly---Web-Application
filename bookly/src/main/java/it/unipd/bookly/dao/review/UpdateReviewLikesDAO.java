package it.unipd.bookly.dao.review;

import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static it.unipd.bookly.dao.review.ReviewQueries.UPDATE_REVIEW_LIKES;

/**
 * DAO to increment the number of likes for a specific review.
 */
public class UpdateReviewLikesDAO extends AbstractDAO<Boolean> {

    private final int reviewId;

    public UpdateReviewLikesDAO(Connection con, int reviewId) {
        super(con);
        this.reviewId = reviewId;
    }

    @Override
    protected void doAccess() throws Exception {
        boolean originalAutoCommit = con.getAutoCommit();
        con.setAutoCommit(false); // Begin transaction

        String getLikesQuery = "SELECT number_of_likes FROM booklySchema.reviews WHERE review_id = ?";

        try {
            int currentLikes = 0;

            try (PreparedStatement getStmt = con.prepareStatement(getLikesQuery)) {
                getStmt.setInt(1, reviewId);
                try (ResultSet rs = getStmt.executeQuery()) {
                    if (rs.next()) {
                        currentLikes = rs.getInt("number_of_likes");
                    } else {
                        LOGGER.warn("No review found with ID {}. Cannot update likes.", reviewId);
                        this.outputParam = false;
                        con.rollback();
                        return;
                    }
                }
            }

            int updatedLikes = currentLikes + 1;

            try (PreparedStatement updateStmt = con.prepareStatement(UPDATE_REVIEW_LIKES)) {
                updateStmt.setInt(1, updatedLikes);
                updateStmt.setInt(2, reviewId);

                int rows = updateStmt.executeUpdate();
                this.outputParam = rows > 0;

                if (outputParam) {
                    LOGGER.info("Successfully incremented likes for review ID {}: {} → {}", reviewId, currentLikes, updatedLikes);
                } else {
                    LOGGER.warn("Failed to update likes for review ID {}.", reviewId);
                }
            }

            con.commit();
        } catch (SQLException ex) {
            con.rollback();
            LOGGER.error("Transaction failed for updating likes (review ID {}): {}", reviewId, ex.getMessage(), ex);
            throw ex;
        } finally {
            con.setAutoCommit(originalAutoCommit);
        }
    }
}
