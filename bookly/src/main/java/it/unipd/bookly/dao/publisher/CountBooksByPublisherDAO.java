package it.unipd.bookly.dao.publisher;

import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static it.unipd.bookly.dao.publisher.PublisherQueries.COUNT_BOOKS_BY_PUBLISHER;

/**
 * DAO class to count how many books are associated with a specific publisher.
 */
public class CountBooksByPublisherDAO extends AbstractDAO<Integer> {

    private final int publisherId;

    /**
     * Constructor.
     *
     * @param con         the database connection
     * @param publisherId the ID of the publisher
     */
    public CountBooksByPublisherDAO(final Connection con, final int publisherId) {
        super(con);
        this.publisherId = publisherId;
    }

    @Override
    protected void doAccess() throws SQLException {
        try (PreparedStatement stmt = con.prepareStatement(COUNT_BOOKS_BY_PUBLISHER)) {
            stmt.setInt(1, publisherId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    this.outputParam = rs.getInt(1);
                    LOGGER.info("Publisher ID {} has {} book(s).", publisherId, outputParam);
                } else {
                    this.outputParam = 0;
                    LOGGER.warn("No books found for publisher ID {}.", publisherId);
                }
            }

        } catch (SQLException ex) {
            LOGGER.error("Error counting books for publisher ID {}: {}", publisherId, ex.getMessage());
            throw ex;
        }
    }
}
