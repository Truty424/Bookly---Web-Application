package it.unipd.bookly.dao.review;

import java.sql.Connection;
import java.sql.PreparedStatement;

import it.unipd.bookly.dao.AbstractDAO;

import static it.unipd.bookly.dao.review.ReviewQueries.DELETE_REVIEW;

/**
 * DAO to delete a review by its ID.
 */
public class DeleteReviewDAO extends AbstractDAO<Boolean> {

    private final int reviewId;

    /**
     * Constructs a DAO for deleting a specific review.
     *
     * @param con      Database connection
     * @param reviewId ID of the review to be deleted
     */
    public DeleteReviewDAO(Connection con, int reviewId) {
        super(con);
        this.reviewId = reviewId;
    }

    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmt = con.prepareStatement(DELETE_REVIEW)) {
            stmt.setInt(1, reviewId);

            int affectedRows = stmt.executeUpdate();
            this.outputParam = affectedRows > 0;

            if (affectedRows > 0) {
                LOGGER.info("Successfully deleted review with ID {}", reviewId);
            } else {
                LOGGER.warn("No review found with ID {}. Nothing was deleted.", reviewId);
            }

        } catch (Exception e) {
            LOGGER.error("Failed to delete review with ID {}: {}", reviewId, e.getMessage(), e);
            throw e;
        }
    }
}
