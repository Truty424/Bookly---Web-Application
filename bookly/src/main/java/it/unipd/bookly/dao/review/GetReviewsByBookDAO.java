package it.unipd.bookly.dao.review;

import it.unipd.bookly.Resource.Review;
import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static it.unipd.bookly.dao.review.ReviewQueries.GET_REVIEWS_BY_BOOK;

/**
 * DAO to retrieve reviews for a specific book.
 */
public class GetReviewsByBookDAO extends AbstractDAO<List<Review>> {

    private final int bookId;

    public GetReviewsByBookDAO(Connection con, int bookId) {
        super(con);
        this.bookId = bookId;
    }

    @Override
    protected void doAccess() throws Exception {
        List<Review> reviews = new ArrayList<>();

        try (PreparedStatement stmt = con.prepareStatement(GET_REVIEWS_BY_BOOK)) {
            stmt.setInt(1, bookId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int reviewId = rs.getInt("review_id");
                    int userId = rs.getInt("user_id");
                    String reviewContent = rs.getString("review_content");
                    int plotRating = rs.getInt("plot_rating");
                    int styleRating = rs.getInt("style_rating");
                    int themeRating = rs.getInt("theme_rating");
                    Timestamp reviewDate = rs.getTimestamp("review_date");

                    Review review = new Review(
                        reviewId,
                        userId,
                        bookId,
                        reviewContent,
                        plotRating,
                        styleRating,
                        themeRating,
                        reviewDate
                    );

                    reviews.add(review);
                }
            }

        } catch (Exception e) {
            LOGGER.error("Failed to fetch reviews for book ID {}: {}", bookId, e.getMessage(), e);
            throw e;
        }

        this.outputParam = reviews;
    }
}
