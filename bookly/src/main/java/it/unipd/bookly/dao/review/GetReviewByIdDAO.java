package it.unipd.bookly.dao.review;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import it.unipd.bookly.Resource.Review;
import it.unipd.bookly.dao.AbstractDAO;

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
                    int userId = rs.getInt("user_id");
                    int bookId = rs.getInt("book_id");
                    String reviewText = rs.getString("comment");
                    int rating = rs.getInt("rating");
                    int likes = rs.getInt("number_of_likes");
                    int dislikes = rs.getInt("number_of_dislikes");
                    Timestamp reviewDate = rs.getTimestamp("review_date");

                    Review review = new Review(
                        reviewId,
                        userId,
                        bookId,
                        reviewText,
                        rating,
                        likes,
                        dislikes,
                        reviewDate
                    );

                    this.outputParam = review;
                } else {
                    this.outputParam = null;
                    LOGGER.warn("No review found with ID {}", reviewId);
                }
            }

        } catch (Exception e) {
            LOGGER.error("Error fetching review with ID {}: {}", reviewId, e.getMessage(), e);
            throw e;
        }
    }
}
