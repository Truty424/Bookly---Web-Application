package it.unipd.bookly.dao.publisher;

import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static it.unipd.bookly.dao.publisher.PublisherQueries.REMOVE_PUBLISHER_FROM_BOOK;

/**
 * DAO class to remove a publisher-book association from the database.
 */
public class RemovePublisherFromBookDAO extends AbstractDAO<Boolean> {

    private final int bookId;
    private final int publisherId;

    /**
     * Constructor.
     *
     * @param con         the database connection
     * @param bookId      the book ID
     * @param publisherId the publisher ID
     */
    public RemovePublisherFromBookDAO(final Connection con, final int bookId, final int publisherId) {
        super(con);
        this.bookId = bookId;
        this.publisherId = publisherId;
    }

    @Override
    protected void doAccess() throws SQLException {
        try (PreparedStatement stmt = con.prepareStatement(REMOVE_PUBLISHER_FROM_BOOK)) {
            stmt.setInt(1, bookId);
            stmt.setInt(2, publisherId);

            int rowsAffected = stmt.executeUpdate();
            this.outputParam = rowsAffected > 0;

            if (outputParam) {
                LOGGER.info("Publisher {} removed from book {}.", publisherId, bookId);
            } else {
                LOGGER.warn("No publisher-book link found to remove for publisher {} and book {}.", publisherId, bookId);
            }

        } catch (SQLException ex) {
            LOGGER.error("Error removing publisher {} from book {}: {}", publisherId, bookId, ex.getMessage());
            throw ex;
        }
    }
}
