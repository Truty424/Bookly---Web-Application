package it.unipd.bookly.dao.publisher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.unipd.bookly.dao.AbstractDAO;
import static it.unipd.bookly.dao.publisher.PublisherQueries.GET_BOOKS_BY_PUBLISHER;

public class GetBooksByPublisherDAO extends AbstractDAO<List<String>> {
    private final int publisherId;

    public GetBooksByPublisherDAO(final Connection con, final int publisherId) {
        super(con);
        this.publisherId = publisherId;
    }

    @Override
    protected void doAccess() throws SQLException {
        List<String> books = new ArrayList<>();
        try (PreparedStatement stmt = con.prepareStatement(GET_BOOKS_BY_PUBLISHER)) {
            stmt.setInt(1, publisherId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    books.add(rs.getString("title"));
                }
            }
        }
        this.outputParam = books;
    }
}