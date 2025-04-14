package it.unipd.bookly.dao.publisher;

import it.unipd.bookly.Resource.Publisher;
import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static it.unipd.bookly.dao.publisher.PublisherQueries.UPDATE_PUBLISHER;

/**
 * DAO class to update publisher information in the database.
 */
public class UpdatePublisherDAO extends AbstractDAO<Boolean> {

    private static final Logger LOGGER = LogManager.getLogger(UpdatePublisherDAO.class);

    private final Publisher publisher;

    /**
     * Constructor.
     *
     * @param con       The database connection
     * @param publisher The Publisher object containing updated values
     */
    public UpdatePublisherDAO(final Connection con, final Publisher publisher) {
        super(con);
        this.publisher = publisher;
    }

    /**
     * Executes the update query using values from the Publisher object.
     */
    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmt = con.prepareStatement(UPDATE_PUBLISHER)) {
            stmt.setInt(1, publisher.getPublisherId());
            stmt.setString(2, publisher.getPublisherName());
            stmt.setString(3, publisher.getPhone());
            stmt.setString(4, publisher.getAddress());

            int affectedRows = stmt.executeUpdate();
            this.outputParam = affectedRows > 0;

            if (this.outputParam) {
                LOGGER.info("Publisher ID {} updated successfully.", publisher.getPublisherId());
            } else {
                LOGGER.warn("No publisher updated. ID {} may not exist.", publisher.getPublisherId());
            }
        }
    }
}
