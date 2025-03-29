package it.unipd.bookly.dao.publisher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import it.unipd.bookly.dao.AbstractDAO;
import static it.unipd.bookly.dao.publisher.PublisherQueries.UPDATE_PUBLISHER;

public class UpdatePublisherDAO extends AbstractDAO<Boolean> {
    private final int publisherId;
    private final String publisherName;
    private final String phone;
    private final String address;

    public UpdatePublisherDAO(final Connection con, final int publisherId, final String publisherName, final String phone, final String address) {
        super(con);
        this.publisherId = publisherId;
        this.publisherName = publisherName;
        this.phone = phone;
        this.address = address;
    }

    @Override
    protected void doAccess() throws SQLException {
        try (PreparedStatement stmt = con.prepareStatement(UPDATE_PUBLISHER)) {
            stmt.setString(1, publisherName);
            stmt.setString(2, phone);
            stmt.setString(3, address);
            stmt.setInt(4, publisherId);
            int rowsAffected = stmt.executeUpdate();
            this.outputParam = rowsAffected > 0;
        }
    }
}