package it.unipd.bookly.dao.publisher;

import it.unipd.bookly.dao.AbstractDAO;
import it.unipd.bookly.resource.Publisher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static it.unipd.bookly.publisher.PublisherQueries.GET_ALL_PUBLISHERS;

public class GetAllPublishersDAO extends AbstractDAO<List<Publisher>> {

    /**
     * Creates a new DAO to retrieve all publishers from the database.
     *
     * @param con the database connection to use.
     */
    public GetAllPublishersDAO(final Connection con) {
        super(con);
    }

    @Override
    protected void doAccess() throws Exception {
        List<Publisher> publisher = new ArrayList<>();

        try (PreparedStatement stmnt = con.prepareStatement(GET_ALL_PUBLISHERS)) {
            try (ResultSet rs = stmnt.executeQuery()) {
                while (rs.next()) {
                    int publisherId = rs.getInt("publisher_id");
                    String name = rs.getString("publisher_name");
                    String phone = rs.getString("phone");
                    String address = rs.getString("address");

                    publisher.add(new Publisher(publisherId, name, phone, address));
                }
            }

            this.outputParam = publishers;

            if (!publishers.isEmpty()) {
                LOGGER.info("First Publisher loaded: {} {}", publishers.get(0).getname());
            } else {
                LOGGER.info("No publishers found in the database.");
            }

        } catch (Exception ex) {
            LOGGER.error("Error retrieving publishers: {}", ex.getMessage());
        }
    }
}
