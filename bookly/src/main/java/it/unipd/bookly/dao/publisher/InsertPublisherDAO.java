package it.unipd.bookly.dao.publisher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import it.unipd.bookly.dao.AbstractDAO;
import static it.unipd.bookly.dao.publisher.PublisherQueries.INSERT_PUBLISHER;

/**
 * DAO class to insert a new publisher into the database.
 */
public class InsertPublisherDAO extends AbstractDAO<Boolean> {
    private final String publisherName;
    private final String phone;
    private final String address;

    /**
     * Constructs a DAO to insert a publisher.
     *
     * @param con           the database connection.
     * @param publisherName the name of the publisher.
     * @param phone         the phone number of the publisher.
     * @param address       the address of the publisher.
     */
    public InsertPublisherDAO(final Connection con, final String publisherName, final String phone, final String address) {
        super(con);
        this.publisherName = publisherName;
        this.phone = phone;
        this.address = address;
    }

    @Override
    protected void doAccess() throws SQLException {
        try (PreparedStatement stmt = con.prepareStatement(INSERT_PUBLISHER)) {
            stmt.setString(1, publisherName);
            stmt.setString(2, phone);
            stmt.setString(3, address);
            int rowsAffected = stmt.executeUpdate();
            this.outputParam = rowsAffected > 0;
        } catch (SQLException ex) {
            LOGGER.error("Error inserting publisher {}: {}", publisherName, ex.getMessage());
            throw ex;
        }
    }
}