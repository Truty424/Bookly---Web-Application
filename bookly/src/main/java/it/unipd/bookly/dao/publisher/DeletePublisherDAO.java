package it.unipd.bookly.dao.publisher;

import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static it.unipd.bookly.dao.publisher.PublisherQueries.DELETE_PUBLISHER;

/**
 * DAO class to delete a publisher from the database by ID.
 */
public class DeletePublisherDAO extends AbstractDAO<Boolean> {

    private final int publisherId;

    /**
     * Constructor to delete a specific publisher.
     *
     * @param con the database connection
     * @param publisherId the ID of the publisher to delete
     */
    public DeletePublisherDAO(final Connection con, final int publisherId) {
        super(con);
        this.publisherId = publisherId;
    }

    @Override
    protected void doAccess() throws SQLException {
        boolean autoCommitState = con.getAutoCommit();
        con.setAutoCommit(false); // Start transaction

        try {
            // Step 1: Delete from published_by
            try (PreparedStatement stmt = con.prepareStatement(
                    "DELETE FROM booklySchema.published_by WHERE publisher_id = ?")) {
                stmt.setInt(1, publisherId);
                stmt.executeUpdate();
            }

            // Step 2: Delete from publishers
            try (PreparedStatement stmt = con.prepareStatement(DELETE_PUBLISHER)) {
                stmt.setInt(1, publisherId);
                int rowsAffected = stmt.executeUpdate();
                this.outputParam = rowsAffected > 0;

                if (rowsAffected > 0) {
                    LOGGER.info("Publisher with ID {} deleted successfully.", publisherId);
                } else {
                    LOGGER.warn("No publisher found with ID {}. Nothing was deleted.", publisherId);
                }
            }

            con.commit(); // All good

        } catch (SQLException ex) {
            con.rollback(); // Undo partial changes
            LOGGER.error("Error deleting publisher with ID {}: {}", publisherId, ex.getMessage());
            throw ex;
        } finally {
            con.setAutoCommit(autoCommitState); // Restore original state
        }
    }
}
