package it.unipd.bookly.dao.review;

import it.unipd.bookly.Resource.Review;
import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

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
                    String content = rs.getString("review_content");
                    int plotRating = rs.getInt("plot_rating");
                    int styleRating = rs.getInt("style_rating");
                    int themeRating = rs.getInt("theme_rating");
                    Timestamp reviewDate = rs.getTimestamp("review_date");

                    Review review = new Review(
                            reviewId,
                            userId,
                            bookId,
                            content,
                            plotRating,
                            styleRating,
                            themeRating,
                            reviewDate
                    );

                    this.outputParam = review;
                } else {
                    this.outputParam = null; // optional: null if not found
                }
            }

        } catch (Exception e) {
            LOGGER.error("Error fetching review with ID {}: {}", reviewId, e.getMessage(), e);
            throw e;
        }
    }
}
