package it.unipd.bookly.dao.review;

import it.unipd.bookly.Resource.Review;
import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static it.unipd.bookly.dao.review.ReviewQueries.GET_TOP_REVIEWS_FOR_BOOK;

/**
 * DAO to retrieve the top reviews for a specific book.
 */
public class GetTopReviewsForBookDAO extends AbstractDAO<List<Review>> {

    private final int bookId;
    private final int limit;

    public GetTopReviewsForBookDAO(Connection con, int bookId, int limit) {
        super(con);
        this.bookId = bookId;
        this.limit = limit;
    }

    @Override
    protected void doAccess() throws Exception {
        List<Review> reviews = new ArrayList<>();

        try (PreparedStatement stmt = con.prepareStatement(GET_TOP_REVIEWS_FOR_BOOK)) {
            stmt.setInt(1, bookId);
            stmt.setInt(2, limit);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int reviewId = rs.getInt("review_id");
                    int userId = rs.getInt("user_id");
                    String comment = rs.getString("comment");
                    int rating = rs.getInt("rating");
                    int likes = rs.getInt("number_of_likes");
                    int dislikes = rs.getInt("number_of_dislikes");
                    Timestamp reviewDate = rs.getTimestamp("review_date");

                    Review review = new Review(
                            reviewId,
                            userId,
                            bookId,
                            comment,
                            rating,
                            likes,
                            dislikes,
                            reviewDate
                    );

                    reviews.add(review);
                }
            }

        } catch (Exception e) {
            LOGGER.error("Failed to retrieve top reviews for book ID {}: {}", bookId, e.getMessage(), e);
            throw e;
        }

        this.outputParam = reviews;
    }
}
