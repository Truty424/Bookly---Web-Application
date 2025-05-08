package it.unipd.bookly.dao.review;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import it.unipd.bookly.Resource.Review;
import it.unipd.bookly.dao.AbstractDAO;

import static it.unipd.bookly.dao.review.ReviewQueries.GET_REVIEWS_BY_BOOK;

/**
 * DAO to retrieve all reviews associated with a specific book.
 */
public class GetReviewsByBookDAO extends AbstractDAO<List<Review>> {

    private final int book_id;

    public GetReviewsByBookDAO(Connection con, int book_id) {
        super(con);
        this.book_id = book_id;
    }

    @Override
    protected void doAccess() throws Exception {
        List<Review> reviews = new ArrayList<>();

        try (PreparedStatement stmt = con.prepareStatement(GET_REVIEWS_BY_BOOK)) {
            stmt.setInt(1, book_id);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int reviewId = rs.getInt("review_id");
                    int userId = rs.getInt("user_id");
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
                            book_id,
                            comment,
                            rating,
                            likes,
                            dislikes,
                            reviewDate,
                            parentReviewId
                    );

                    review.setUsername(rs.getString("username")); // <-- Add this line

                    reviews.add(review);
                }
            }

        } catch (Exception e) {
            LOGGER.error("Failed to fetch reviews for book ID {}: {}", book_id, e.getMessage(), e);
            throw e;
        }

        this.outputParam = reviews;
    }
}
