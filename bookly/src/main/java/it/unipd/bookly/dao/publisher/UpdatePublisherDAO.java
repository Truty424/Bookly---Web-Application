package it.unipd.bookly.dao.publisher;

import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static it.unipd.bookly.dao.publisher.PublisherQueries.UPDATE_PUBLISHER;

/**
 * DAO class to update publisher information in the database.
 */
public class UpdatePublisherDAO extends AbstractDAO<Boolean> {

    private final int publisherId;
    private final String publisherName;
    private final String phone;
    private final String address;

    /**
     * Constructor.
     *
     * @param con           the database connection
     * @param publisherId   ID of the publisher to update
     * @param publisherName new name for the publisher
     * @param phone         new phone number
     * @param address       new address
     */
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

            if (rowsAffected > 0) {
                LOGGER.info("Publisher ID {} updated successfully.", publisherId);
            } else {
                LOGGER.warn("No publisher updated. ID {} may not exist.", publisherId);
            }

        } catch (SQLException ex) {
            LOGGER.error("Error updating publisher ID {}: {}", publisherId, ex.getMessage());
            throw ex;
        }
    }
}
