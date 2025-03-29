package it.unipd.bookly.dao.review;

import java.sql.Connection;
import java.sql.PreparedStatement;

import it.unipd.bookly.dao.AbstractDAO;
import static it.unipd.bookly.dao.review.ReviewQueries.UPDATE_REVIEW_LIKES_DISLIKES;

/**
 * DAO to update the number of likes and dislikes for a review.
 */
public class UpdateReviewLikesDislikesDAO extends AbstractDAO<Boolean> {

    private final int reviewId;
    private final int numberOfLikes;
    private final int numberOfDislikes;

    public UpdateReviewLikesDislikesDAO(Connection con, int reviewId, int numberOfLikes, int numberOfDislikes) {
        super(con);
        this.reviewId = reviewId;
        this.numberOfLikes = numberOfLikes;
        this.numberOfDislikes = numberOfDislikes;
    }

    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmt = con.prepareStatement(UPDATE_REVIEW_LIKES_DISLIKES)) {
            stmt.setInt(1, numberOfLikes);
            stmt.setInt(2, numberOfDislikes);
            stmt.setInt(3, reviewId);

            int rowsUpdated = stmt.executeUpdate();
            this.outputParam = rowsUpdated > 0;
        }
    }
}
