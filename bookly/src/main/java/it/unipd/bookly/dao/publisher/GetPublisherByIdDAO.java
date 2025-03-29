package it.unipd.bookly.dao.publisher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import it.unipd.bookly.dao.AbstractDAO;
import static it.unipd.bookly.dao.publisher.PublisherQueries.GET_PUBLISHER_BY_ID;

public class GetPublisherByIdDAO extends AbstractDAO<String> {
    private final int publisherId;

    public GetPublisherByIdDAO(final Connection con, final int publisherId) {
        super(con);
        this.publisherId = publisherId;
    }

    @Override
    protected void doAccess() throws SQLException {
        try (PreparedStatement stmt = con.prepareStatement(GET_PUBLISHER_BY_ID)) {
            stmt.setInt(1, publisherId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    this.outputParam = rs.getString("publisher_name");
                }
            }
        }
    }
}