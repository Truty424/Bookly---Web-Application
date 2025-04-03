package it.unipd.bookly.dao.publisher;

import it.unipd.bookly.Resource.Publisher;
import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static it.unipd.bookly.dao.publisher.PublisherQueries.GET_PUBLISHER_BY_ID;

/**
 * DAO class to retrieve a Publisher by its ID.
 */
public class GetPublisherByIdDAO extends AbstractDAO<Publisher> {

    private final int publisherId;

    /**
     * Constructor.
     *
     * @param con         the DB connection
     * @param publisherId the ID of the publisher to retrieve
     */
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
                    String name = rs.getString("publisher_name");
                    String phone = rs.getString("phone");
                    String address = rs.getString("address");

                    this.outputParam = new Publisher(publisherId, name, phone, address);
                    LOGGER.info("Publisher found with ID {}: {}", publisherId, name);
                } else {
                    this.outputParam = null;
                    LOGGER.warn("No publisher found with ID {}.", publisherId);
                }
            }

        } catch (SQLException ex) {
            LOGGER.error("Error retrieving publisher with ID {}: {}", publisherId, ex.getMessage());
            throw ex;
        }
    }
}
