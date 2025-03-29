package it.unipd.bookly.dao.publisher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import it.unipd.bookly.dao.AbstractDAO;
import static it.unipd.bookly.dao.publisher.PublisherQueries.COUNT_BOOKS_BY_PUBLISHER;

public class CountBooksByPublisherDAO extends AbstractDAO<Integer> {
    private final int publisherId;

    public CountBooksByPublisherDAO(final Connection con, final int publisherId) {
        super(con);
        this.publisherId = publisherId;
    }

    @Override
    protected void doAccess() throws SQLException {
        try (PreparedStatement stmt = con.prepareStatement(COUNT_BOOKS_BY_PUBLISHER)) {
            stmt.setInt(1, publisherId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    this.outputParam = rs.getInt(1);
                }
            }
        }
    }
}