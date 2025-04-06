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
