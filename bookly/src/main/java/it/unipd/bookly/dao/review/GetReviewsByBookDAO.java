package it.unipd.bookly.dao.review;

import it.unipd.bookly.Resource.Review;
import it.unipd.bookly.dao.AbstractDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static it.unipd.bookly.dao.review.ReviewQueries.GET_REVIEWS_BY_BOOK;

/**
 * DAO to retrieve all reviews for a specific book.
 */
public class GetReviewsByBookDAO extends AbstractDAO<List<Review>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetReviewsByBookDAO.class);

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
                    Review review = new Review();

                    review.setReviewId(rs.getInt("review_id"));
                    review.setUserId(rs.getInt("user_id"));
                    review.setbookId(rs.getInt("book_id"));
                    review.setReviewText(rs.getString("comment"));
                    review.setRating(rs.getInt("rating"));
                    review.setNumberOfLikes(rs.getInt("number_of_likes"));
                    review.setNumberOfDislikes(rs.getInt("number_of_dislikes"));
                    review.setReviewDate(rs.getTimestamp("review_date"));
                    review.setUsername(rs.getString("username"));

                    // Optional: safely check for parent_review_id if it exists in the ResultSet
                    try {
                        Object parent = rs.getObject("parent_review_id");
                        if (parent != null) {
                            review.setParentReviewId(rs.getInt("parent_review_id"));
                        }
                    } catch (Exception ignored) {
                        // Field not selected or doesn't exist — safe to skip
                    }

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
