package it.unipd.bookly.dao.publisher;

import it.unipd.bookly.Resource.Publisher;
import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static it.unipd.bookly.dao.publisher.PublisherQueries.INSERT_PUBLISHER;

/**
 * DAO class to insert a new publisher into the database.
 */
public class InsertPublisherDAO extends AbstractDAO<Boolean> {

    private final Publisher publisher;

    /**
     * Constructs a DAO to insert a publisher.
     *
     * @param con the database connection.
     * @param publisher the publisher object containing name, phone, and
     * address.
     */
    public InsertPublisherDAO(final Connection con, final Publisher publisher) {
        super(con);
        this.publisher = publisher;
    }

    @Override
    protected void doAccess() throws SQLException {
        try (PreparedStatement stmt = con.prepareStatement(INSERT_PUBLISHER, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, publisher.getPublisherName());
            stmt.setString(2, publisher.getPhone());
            stmt.setString(3, publisher.getAddress());

            int rowsAffected = stmt.executeUpdate();
            this.outputParam = rowsAffected > 0;

            if (rowsAffected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        int generatedId = rs.getInt(1);
                        publisher.setPublisherId(generatedId);  // ✅ Set the ID
                        LOGGER.info("Publisher '{}' inserted with ID {}.", publisher.getPublisherName(), generatedId);
                    } else {
                        LOGGER.warn("Publisher inserted but no ID returned.");
                    }
                }
            } else {
                LOGGER.warn("No rows inserted for publisher '{}'.", publisher.getPublisherName());
            }

        } catch (SQLException ex) {
            LOGGER.error("Error inserting publisher '{}': {}", publisher.getPublisherName(), ex.getMessage());
            throw ex;
        }
    }
}
