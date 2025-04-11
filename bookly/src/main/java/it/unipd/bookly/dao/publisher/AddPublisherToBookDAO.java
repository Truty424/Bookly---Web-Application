package it.unipd.bookly.dao.publisher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import it.unipd.bookly.dao.AbstractDAO;
import static it.unipd.bookly.dao.publisher.PublisherQueries.ADD_PUBLISHER_TO_BOOK;

/**
 * DAO class to associate a publisher with a book in the database.
 */
public class AddPublisherToBookDAO extends AbstractDAO<Boolean> {

    private final int book_id;
    private final int publisherId;

    /**
     * Constructor to link a publisher to a book.
     *
     * @param con the database connection
     * @param book_id the ID of the book
     * @param publisherId the ID of the publisher
     */
    public AddPublisherToBookDAO(final Connection con, final int book_id, final int publisherId) {
        super(con);
        this.book_id = book_id;
        this.publisherId = publisherId;
    }

    @Override
    protected void doAccess() throws SQLException {
        try (PreparedStatement stmt = con.prepareStatement(ADD_PUBLISHER_TO_BOOK)) {
            stmt.setInt(1, book_id);
            stmt.setInt(2, publisherId);

            int rowsAffected = stmt.executeUpdate();
            this.outputParam = rowsAffected > 0;

            if (outputParam) {
                LOGGER.info("Publisher {} successfully assigned to book {}.", publisherId, book_id);
            } else {
                LOGGER.warn("No rows affected when assigning publisher {} to book {}.", publisherId, book_id);
            }

        } catch (SQLException ex) {
            LOGGER.error("Error assigning publisher {} to book {}: {}", publisherId, book_id, ex.getMessage());
            throw ex;
        }
    }
}
