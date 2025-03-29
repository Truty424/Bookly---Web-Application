package it.unipd.bookly.dao.publisher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import it.unipd.bookly.dao.AbstractDAO;
import static it.unipd.bookly.dao.publisher.PublisherQueries.REMOVE_PUBLISHER_FROM_BOOK;

public class RemovePublisherFromBookDAO extends AbstractDAO<Boolean> {
    private final int bookId;
    private final int publisherId;

    public RemovePublisherFromBookDAO(final Connection con, final int bookId, final int publisherId) {
        super(con);
        this.bookId = bookId;
        this.publisherId = publisherId;
    }

    @Override
    protected void doAccess() throws SQLException {
        try (PreparedStatement stmt = con.prepareStatement(REMOVE_PUBLISHER_FROM_BOOK)) {
            stmt.setInt(1, bookId);
            stmt.setInt(2, publisherId);
            int rowsAffected = stmt.executeUpdate();
            this.outputParam = rowsAffected > 0;
        }
    }
}