package it.unipd.bookly.dao.publisher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.unipd.bookly.Resource.Publisher;
import it.unipd.bookly.dao.AbstractDAO;
import static it.unipd.bookly.dao.publisher.PublisherQueries.GET_PUBLISHERS_BY_BOOK;

/**
 * DAO class to retrieve all publishers associated with a given book.
 */
public class GetPublishersByBookDAO extends AbstractDAO<List<Publisher>> {

    private final int book_id;

    /**
     * Constructor.
     *
     * @param con the database connection
     * @param book_id the book ID to find publishers for
     */
    public GetPublishersByBookDAO(final Connection con, final int book_id) {
        super(con);
        this.book_id = book_id;
    }

    @Override
    protected void doAccess() throws SQLException {
        List<Publisher> publishers = new ArrayList<>();

        try (PreparedStatement stmt = con.prepareStatement(GET_PUBLISHERS_BY_BOOK)) {
            stmt.setInt(1, book_id);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Publisher publisher = new Publisher(
                            rs.getInt("publisher_id"),
                            rs.getString("publisher_name"),
                            rs.getString("phone"),
                            rs.getString("address")
                    );
                    publishers.add(publisher);
                }
            }

            this.outputParam = publishers;

            if (!publishers.isEmpty()) {
                LOGGER.info("{} publisher(s) found for book ID {}. First: {}", publishers.size(), book_id, publishers.get(0).getPublisherName());
            } else {
                LOGGER.info("No publishers found for book ID {}.", book_id);
            }

        } catch (SQLException ex) {
            LOGGER.error("Error retrieving publishers for book ID {}: {}", book_id, ex.getMessage());
            throw ex;
        }
    }
}
