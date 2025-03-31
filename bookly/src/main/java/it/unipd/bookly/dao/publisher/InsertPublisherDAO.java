package it.unipd.bookly.dao.publisher;

import it.unipd.bookly.Resource.Publisher;
import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static it.unipd.bookly.dao.publisher.PublisherQueries.INSERT_PUBLISHER;

/**
 * DAO class to insert a new publisher into the database.
 */
public class InsertPublisherDAO extends AbstractDAO<Boolean> {

    private final Publisher publisher;

    /**
     * Constructs a DAO to insert a new publisher.
     *
     * @param con       the database connection.
     * @param publisher the publisher object to insert.
     */
    public InsertPublisherDAO(final Connection con, final Publisher publisher) {
        super(con);
        this.publisher = publisher;
    }

    @Override
    protected void doAccess() throws SQLException {
        try (PreparedStatement stmt = con.prepareStatement(INSERT_PUBLISHER)) {
            stmt.setString(1, publisher.getPublisherName());
            stmt.setString(2, publisher.getPhone());
            stmt.setString(3, publisher.getAddress());

            int rowsAffected = stmt.executeUpdate();
            this.outputParam = rowsAffected > 0;

            if (outputParam) {
                LOGGER.info("Publisher '{}' inserted successfully.", publisher.getPublisherName());
            } else {
                LOGGER.warn("Insert returned no affected rows for publisher '{}'.", publisher.getPublisherName());
            }

        } catch (SQLException ex) {
            LOGGER.error("Error inserting publisher '{}': {}", publisher.getPublisherName(), ex.getMessage());
            throw ex;
        }
    }
}
