package it.unipd.bookly.dao.review;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import it.unipd.bookly.dao.AbstractDAO;
import static it.unipd.bookly.dao.review.ReviewQueries.GET_AVG_RATING_FOR_BOOK;

/**
 * DAO to get the average rating of a book.
 */
public class GetAvgRatingForBookDAO extends AbstractDAO<Double> {

    private final int book_id;

    public GetAvgRatingForBookDAO(Connection con, int book_id) {
        super(con);
        this.book_id = book_id;
    }

    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmt = con.prepareStatement(GET_AVG_RATING_FOR_BOOK)) {
            stmt.setInt(1, book_id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    this.outputParam = rs.getDouble(1);
                } else {
                    this.outputParam = 0.0;
                }
            }
        }
    }
}
