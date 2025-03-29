package it.unipd.bookly.dao.publisher;

import it.unipd.bookly.dao.AbstractDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import static it.unipd.bookly.dao.publisher.PublisherQueries.GET_PUBLISHERS_BY_BOOK;

public class GetPublishersByBookDAO extends AbstractDAO<List<String>> {
    private final int bookId;

    public GetPublishersByBookDAO(final Connection con, final int bookId) {
        super(con);
        this.bookId = bookId;
    }

    @Override
    protected void doAccess() throws SQLException {
        List<String> publishers = new ArrayList<>();
        try (PreparedStatement stmt = con.prepareStatement(GET_PUBLISHERS_BY_BOOK)) {
            stmt.setInt(1, bookId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    publishers.add(rs.getString("publisher_name"));
                }
            }
        }
        this.outputParam = publishers;
    }
}