package it.unipd.bookly.dao.review;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import it.unipd.bookly.dao.AbstractDAO;
import static it.unipd.bookly.dao.review.ReviewQueries.COUNT_REVIEWS_FOR_BOOK;

/**
 * DAO to count the number of reviews for a specific book.
 */
public class CountReviewsForBookDAO extends AbstractDAO<Integer> {

    private final int bookId;

    public CountReviewsForBookDAO(Connection con, int bookId) {
        super(con);
        this.bookId = bookId;
    }

    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmt = con.prepareStatement(COUNT_REVIEWS_FOR_BOOK)) {
            stmt.setInt(1, bookId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    this.outputParam = rs.getInt(1);
                } else {
                    this.outputParam = 0;
                }
            }
        }
    }
}
