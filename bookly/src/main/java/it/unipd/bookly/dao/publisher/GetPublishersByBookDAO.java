package it.unipd.bookly.dao.publisher;

import it.unipd.bookly.Resource.Publisher;
import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static it.unipd.bookly.dao.publisher.PublisherQueries.GET_PUBLISHERS_BY_BOOK;

/**
 * DAO class to retrieve all publishers associated with a given book.
 */
public class GetPublishersByBookDAO extends AbstractDAO<List<Publisher>> {

    private final int bookId;

    /**
     * Constructor.
     *
     * @param con    the database connection
     * @param bookId the book ID to find publishers for
     */
    public GetPublishersByBookDAO(final Connection con, final int bookId) {
        super(con);
        this.bookId = bookId;
    }

    @Override
    protected void doAccess() throws SQLException {
        List<Publisher> publishers = new ArrayList<>();

        try (PreparedStatement stmt = con.prepareStatement(GET_PUBLISHERS_BY_BOOK)) {
            stmt.setInt(1, bookId);

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
                LOGGER.info("{} publisher(s) found for book ID {}. First: {}", publishers.size(), bookId, publishers.get(0).getPublisherName());
            } else {
                LOGGER.info("No publishers found for book ID {}.", bookId);
            }

        } catch (SQLException ex) {
            LOGGER.error("Error retrieving publishers for book ID {}: {}", bookId, ex.getMessage());
            throw ex;
        }
    }
}
