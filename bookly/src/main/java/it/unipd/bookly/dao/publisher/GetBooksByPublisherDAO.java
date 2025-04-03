package it.unipd.bookly.dao.publisher;

import it.unipd.bookly.Resource.Book;
import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static it.unipd.bookly.dao.publisher.PublisherQueries.GET_BOOKS_BY_PUBLISHER;

/**
 * DAO class to retrieve all book titles associated with a given publisher.
 */
public class GetBooksByPublisherDAO extends AbstractDAO<List<Book>> {

    private final int publisherId;

    /**
     * Constructor.
     *
     * @param con         the database connection
     * @param publisherId the publisher ID to look up books for
     */
    public GetBooksByPublisherDAO(final Connection con, final int publisherId) {
        super(con);
        this.publisherId = publisherId;
    }

    @Override
    protected void doAccess() throws SQLException {
        List<Book> books = new ArrayList<>();

        try (PreparedStatement stmt = con.prepareStatement(GET_BOOKS_BY_PUBLISHER)) {
            stmt.setInt(1, publisherId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    books.add(new Book(
                        rs.getInt("book_id"),
                        rs.getString("title"),
                        rs.getString("language"),
                        rs.getString("isbn"),
                        rs.getDouble("price"),
                        rs.getString("edition"),
                        rs.getInt("publication_year"),
                        rs.getInt("number_of_pages"),
                        rs.getInt("stock_quantity"),
                        rs.getDouble("average_rate"),
                        rs.getString("summary")
                    ));
                }
            }

            this.outputParam = books;

            if (!books.isEmpty()) {
                LOGGER.info("{} book(s) found for publisher ID {}. First: '{}'", books.size(), publisherId, books.get(0));
            } else {
                LOGGER.info("No books found for publisher ID {}.", publisherId);
            }

        } catch (SQLException ex) {
            LOGGER.error("Error retrieving books for publisher ID {}: {}", publisherId, ex.getMessage());
            throw ex;
        }
    }
}
