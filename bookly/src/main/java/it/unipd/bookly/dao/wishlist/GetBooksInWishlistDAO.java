package it.unipd.bookly.dao.wishlist;

import it.unipd.bookly.Resource.Book;
import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static it.unipd.bookly.dao.wishlist.WishlistQueries.GET_BOOKS_IN_WISHLIST;

/**
 * DAO class to retrieve all books in a specific wishlist.
 */
public class GetBooksInWishlistDAO extends AbstractDAO<List<Book>> {

    private final int wishlistId;

    /**
     * Constructor to create the DAO instance.
     *
     * @param con        the database connection.
     * @param wishlistId the ID of the wishlist to query.
     */
    public GetBooksInWishlistDAO(final Connection con, final int wishlistId) {
        super(con);
        this.wishlistId = wishlistId;
    }

    @Override
    protected void doAccess() throws Exception {
        List<Book> books = new ArrayList<>();
        try (PreparedStatement stmnt = con.prepareStatement(GET_BOOKS_IN_WISHLIST)) {
            stmnt.setInt(1, wishlistId);

            try (ResultSet rs = stmnt.executeQuery()) {
                while (rs.next()) {
                    Book book = new Book(
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
                    );
                    books.add(book);
                }
            }

            this.outputParam = books;
            LOGGER.info("{} books retrieved from wishlist ID {}.", books.size(), wishlistId);

        } catch (Exception ex) {
            LOGGER.error("Failed to retrieve books from wishlist ID {}: {}", wishlistId, ex.getMessage());
        }
    }
}
