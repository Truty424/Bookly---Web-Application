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

    public DeleteReviewDAO(Connection con, int reviewId) {
        super(con);
        this.reviewId = reviewId;
    }

    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmt = con.prepareStatement(DELETE_REVIEW)) {
            stmt.setInt(1, reviewId);
            int rowsAffected = stmt.executeUpdate();
            this.outputParam = rowsAffected > 0;
        }
    }
}

