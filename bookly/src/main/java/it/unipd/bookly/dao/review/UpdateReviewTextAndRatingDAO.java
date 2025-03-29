package it.unipd.bookly.dao.review;

import java.sql.Connection;
import java.sql.PreparedStatement;

import it.unipd.bookly.dao.AbstractDAO;
import static it.unipd.bookly.dao.review.ReviewQueries.UPDATE_REVIEW_TEXT_AND_RATING;

/**
 * DAO to update a review's text and rating.
 */
public class UpdateReviewTextAndRatingDAO extends AbstractDAO<Boolean> {

    private final int reviewId;
    private final String reviewText;
    private final int rating;

    public UpdateReviewTextAndRatingDAO(Connection con, int reviewId, String reviewText, int rating) {
        super(con);
        this.reviewId = reviewId;
        this.reviewText = reviewText;
        this.rating = rating;
    }

    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmt = con.prepareStatement(UPDATE_REVIEW_TEXT_AND_RATING)) {
            stmt.setString(1, reviewText);
            stmt.setInt(2, rating);
            stmt.setInt(3, reviewId);

            int rowsUpdated = stmt.executeUpdate();
            this.outputParam = rowsUpdated > 0;
        }
    }
}
