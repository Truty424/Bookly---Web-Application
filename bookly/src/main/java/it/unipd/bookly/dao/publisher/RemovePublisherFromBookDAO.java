package it.unipd.bookly.dao.publisher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import it.unipd.bookly.dao.AbstractDAO;
import static it.unipd.bookly.dao.publisher.PublisherQueries.REMOVE_PUBLISHER_FROM_BOOK;

/**
 * DAO class to remove a publisher-book association from the database.
 */
public class RemovePublisherFromBookDAO extends AbstractDAO<Boolean> {

    private final int book_id;
    private final int publisherId;

    /**
     * Constructor.
     *
     * @param con the database connection
     * @param book_id the book ID
     * @param publisherId the publisher ID
     */
    public RemovePublisherFromBookDAO(final Connection con, final int book_id, final int publisherId) {
        super(con);
        this.book_id = book_id;
        this.publisherId = publisherId;
    }

    @Override
    protected void doAccess() throws SQLException {
        try (PreparedStatement stmt = con.prepareStatement(REMOVE_PUBLISHER_FROM_BOOK)) {
            stmt.setInt(1, book_id);
            stmt.setInt(2, publisherId);

            int rowsAffected = stmt.executeUpdate();
            this.outputParam = rowsAffected > 0;

            if (outputParam) {
                LOGGER.info("Publisher {} removed from book {}.", publisherId, book_id);
            } else {
                LOGGER.warn("No publisher-book link found to remove for publisher {} and book {}.", publisherId, book_id);
            }

        } catch (SQLException ex) {
            LOGGER.error("Error removing publisher {} from book {}: {}", publisherId, book_id, ex.getMessage());
            throw ex;
        }
    }
}
