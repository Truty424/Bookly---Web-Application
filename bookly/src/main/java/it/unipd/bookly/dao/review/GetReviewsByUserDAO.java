package it.unipd.bookly.dao.review;

import it.unipd.bookly.dao.AbstractDAO;
import it.unipd.bookly.model.Review;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import static it.unipd.bookly.dao.review.ReviewQueries.GET_REVIEWS_BY_USER;

/**
 * DAO to retrieve reviews written by a specific user.
 */
public class GetReviewsByUserDAO extends AbstractDAO<List<Review>> {

    private final int userId;

    public GetReviewsByUserDAO(Connection con, int userId) {
        super(con);
        this.userId = userId;
    }

    @Override
    protected void doAccess() throws Exception {
        List<Review> reviews = new ArrayList<>();
        try (PreparedStatement stmt = con.prepareStatement(GET_REVIEWS_BY_USER)) {
            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    reviews.add(new Review(rs));
                }
            }
        }
        this.outputParam = reviews;
    }
}
