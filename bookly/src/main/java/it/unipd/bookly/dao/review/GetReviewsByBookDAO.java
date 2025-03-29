package it.unipd.bookly.dao.review;

import it.unipd.bookly.dao.AbstractDAO;
import it.unipd.bookly.model.Review;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
                    reviews.add(new Review(rs));
                }
            }
        }
        this.outputParam = reviews;
    }
}
