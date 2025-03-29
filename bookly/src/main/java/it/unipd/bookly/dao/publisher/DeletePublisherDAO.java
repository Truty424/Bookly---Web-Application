package it.unipd.bookly.dao.publisher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import it.unipd.bookly.dao.AbstractDAO;
import static it.unipd.bookly.dao.publisher.PublisherQueries.DELETE_PUBLISHER;

public class DeletePublisherDAO extends AbstractDAO<Boolean> {
    private final int publisherId;

    public DeletePublisherDAO(final Connection con, final int publisherId) {
        super(con);
        this.publisherId = publisherId;
    }

    @Override
    protected void doAccess() throws SQLException {
        try (PreparedStatement stmt = con.prepareStatement(DELETE_PUBLISHER)) {
            stmt.setInt(1, publisherId);
            int rowsAffected = stmt.executeUpdate();
            this.outputParam = rowsAffected > 0;
        }
    }
}