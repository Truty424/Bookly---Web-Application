package it.unipd.bookly.dao.review;

import it.unipd.bookly.dao.AbstractDAO;
import it.unipd.bookly.model.Review;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import static it.unipd.bookly.dao.review.ReviewQueries.GET_REVIEW_BY_ID;

/**
 * DAO to retrieve a review by its ID.
 */
public class GetReviewByIdDAO extends AbstractDAO<Review> {

    private final int reviewId;

    public GetReviewByIdDAO(Connection con, int reviewId) {
        super(con);
        this.reviewId = reviewId;
    }

    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmt = con.prepareStatement(GET_REVIEW_BY_ID)) {
            stmt.setInt(1, reviewId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    this.outputParam = new Review(rs);
                }
            }
        }
    }
}
