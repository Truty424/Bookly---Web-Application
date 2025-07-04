package it.unipd.bookly.dao.review;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import it.unipd.bookly.Resource.Review;
import it.unipd.bookly.dao.AbstractDAO;
import static it.unipd.bookly.dao.review.ReviewQueries.GET_REVIEWS_BY_USER;

/**
 * DAO to retrieve reviews written by a specific user.
 */
public class GetReviewsByUserDAO extends AbstractDAO<List<Review>> {

    private final int userId;

    public GetReviewsByUserDAO(Connection con, int userId) {
        super(con);
        this.userId = userId;
    }

    @Override
    protected void doAccess() throws Exception {
        List<Review> reviews = new ArrayList<>();

        try (PreparedStatement stmt = con.prepareStatement(GET_REVIEWS_BY_USER)) {
            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int reviewId = rs.getInt("review_id");
                    int bookId = rs.getInt("book_id");
                    String comment = rs.getString("comment");
                    int rating = rs.getInt("rating");
                    int likes = rs.getInt("number_of_likes");
                    int dislikes = rs.getInt("number_of_dislikes");
                    Timestamp reviewDate = rs.getTimestamp("review_date");
                    Integer parentReviewId = rs.getObject("parent_review_id") != null
    ? rs.getInt("parent_review_id")
    : null;

                    Review review = new Review(
                        reviewId,
                        userId,
                        bookId,
                        comment,
                        rating,
                        likes,
                        dislikes,
                        reviewDate,
                        parentReviewId
                    );

                    reviews.add(review);
                }
            }

        } catch (Exception e) {
            LOGGER.error("Failed to fetch reviews for user ID {}: {}", userId, e.getMessage(), e);
            throw e;
        }

        this.outputParam = reviews;
    }
}
