package it.unipd.bookly.dao.publisher;

import it.unipd.bookly.Resource.Publisher;
import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static it.unipd.bookly.dao.publisher.PublisherQueries.GET_ALL_PUBLISHERS;

/**
 * DAO class to retrieve all publishers from the database.
 */
public class GetAllPublishersDAO extends AbstractDAO<List<Publisher>> {

    /**
     * Constructor.
     *
     * @param con the database connection to use.
     */
    public GetAllPublishersDAO(final Connection con) {
        super(con);
    }

    @Override
    protected void doAccess() throws Exception {
        List<Publisher> publishers = new ArrayList<>();

        try (PreparedStatement stmt = con.prepareStatement(GET_ALL_PUBLISHERS);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int publisherId = rs.getInt("publisher_id");
                String name = rs.getString("publisher_name");
                String phone = rs.getString("phone");
                String address = rs.getString("address");

                publishers.add(new Publisher(publisherId, name, phone, address));
            }

            this.outputParam = publishers;

            if (!publishers.isEmpty()) {
                LOGGER.info("First publisher loaded: {}", publishers.get(0).getPublisherName());
            } else {
                LOGGER.info("No publishers found in the database.");
            }

        } catch (Exception ex) {
            LOGGER.error("Error retrieving publishers: {}", ex.getMessage());
            throw ex;
        }
    }
}
