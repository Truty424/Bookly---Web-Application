package it.unipd.bookly.dao.review;

import it.unipd.bookly.dao.AbstractDAO;
import it.unipd.bookly.model.Review;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
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
        try (PreparedStatement stmt = con.prepareStatement(GET_ALL_REVIEWS);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                reviews.add(new Review(rs));
            }
        }
        this.outputParam = reviews;
    }
}
