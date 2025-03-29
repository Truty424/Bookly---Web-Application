package it.unipd.bookly.dao.review;

import it.unipd.bookly.dao.AbstractDAO;
import it.unipd.bookly.model.Review;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
                    reviews.add(new Review(rs));
                }
            }
        }
        this.outputParam = reviews;
    }
}
