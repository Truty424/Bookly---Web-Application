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
 * DAO to retrieve all reviews.
 */
public class GetAllReviewsDAO extends AbstractDAO<List<Review>> {

    public GetAllReviewsDAO(Connection con) {
        super(con);
    }

    @Override
    protected void doAccess() throws Exception {
        List<Review> reviews = new ArrayList<>();

        try (PreparedStatement stmt = con.prepareStatement(GET_ALL_REVIEWS); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int reviewId = rs.getInt("review_id");
                int userId = rs.getInt("user_id");
                int book_id = rs.getInt("book_id");
                String reviewContent = rs.getString("review_content");
                int plotRating = rs.getInt("plot_rating");
                int styleRating = rs.getInt("style_rating");
                int themeRating = rs.getInt("theme_rating");
                Timestamp reviewDate = rs.getTimestamp("review_date");

                Review review = new Review(reviewId, userId, book_id, reviewContent, plotRating, styleRating, themeRating, reviewDate);
                reviews.add(review);
            }

            this.outputParam = reviews;

        } catch (Exception e) {
            LOGGER.error("Error retrieving all reviews: {}", e.getMessage(), e);
            throw e; // rethrowing so calling service can handle it
        }
    }
}
