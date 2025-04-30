package it.unipd.bookly.dao.review;

import it.unipd.bookly.dao.AbstractDAO;
import it.unipd.bookly.Resource.Review;

import java.sql.Connection;
import java.sql.PreparedStatement;

import static it.unipd.bookly.dao.review.ReviewQueries.INSERT_REVIEW;

/**
 * DAO to insert a new review.
 */
public class InsertReviewDAO extends AbstractDAO<Boolean> {

    private final Review review;

    public InsertReviewDAO(Connection con, Review review) {
        super(con);
        this.review = review;
    }

    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmt = con.prepareStatement(INSERT_REVIEW)) {
            stmt.setInt(1, review.getUserId());
            stmt.setInt(2, review.getBookId());
            stmt.setString(3, review.getReviewText());
            stmt.setInt(4, review.getRating());

            // Set likes and dislikes only if non-negative, otherwise default to 0
            stmt.setInt(5, Math.max(0, review.getNumberOfLikes()));
            stmt.setInt(6, Math.max(0, review.getNumberOfDislikes()));
            if (review.getParentReviewId() != null) {
                stmt.setInt(7, review.getParentReviewId());
            } else {
                stmt.setNull(7, java.sql.Types.INTEGER);
            }

            int rowsInserted = stmt.executeUpdate();
            this.outputParam = rowsInserted > 0;

            if (outputParam) {
                LOGGER.info("Inserted new review by user {} for book {}", review.getUserId(), review.getBookId());
            } else {
                LOGGER.warn("Insert review failed for user {}, book {}", review.getUserId(), review.getBookId());
            }

        } catch (Exception e) {
            LOGGER.error("Failed to insert review: {}", e.getMessage(), e);
            throw e;
        }
    }
}
