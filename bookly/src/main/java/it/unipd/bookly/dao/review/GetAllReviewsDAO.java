package it.unipd.bookly.dao.review;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import it.unipd.bookly.Resource.Review;
import it.unipd.bookly.dao.AbstractDAO;

import static it.unipd.bookly.dao.review.ReviewQueries.GET_ALL_REVIEWS;

/**
 * DAO to retrieve all reviews from the database.
 */
public class GetAllReviewsDAO extends AbstractDAO<List<Review>> {

    public GetAllReviewsDAO(Connection con) {
        super(con);
    }

    @Override
    protected void doAccess() throws Exception {
        List<Review> reviews = new ArrayList<>();

        try (PreparedStatement stmt = con.prepareStatement(GET_ALL_REVIEWS);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int reviewId = rs.getInt("review_id");
                int userId = rs.getInt("user_id");
                int bookId = rs.getInt("book_id");
                String comment = rs.getString("comment");
                int rating = rs.getInt("rating");
                int likes = rs.getInt("number_of_likes");
                int dislikes = rs.getInt("number_of_dislikes");
                Timestamp date = rs.getTimestamp("review_date");

                Review review = new Review(reviewId, userId, bookId, comment, rating, likes, dislikes, date);

                // If JOINs are used in the query, populate username and bookTitle
                try {
                    review.setUsername(rs.getString("username"));
                } catch (Exception ignored) {}

                try {
                    review.setBookTitle(rs.getString("book_title"));
                } catch (Exception ignored) {}

                reviews.add(review);
            }

            this.outputParam = reviews;

        } catch (Exception e) {
            LOGGER.error("Error retrieving all reviews: {}", e.getMessage(), e);
            throw e;
        }
    }
}
