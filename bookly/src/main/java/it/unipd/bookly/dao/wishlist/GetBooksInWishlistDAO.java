package it.unipd.bookly.dao.wishlist;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import it.unipd.bookly.Resource.Book;
import it.unipd.bookly.Resource.Image;
import it.unipd.bookly.dao.AbstractDAO;
import static it.unipd.bookly.dao.wishlist.WishlistQueries.GET_BOOKS_IN_WISHLIST;

/**
 * DAO to retrieve all books saved in a given wishlist.
 */
public class GetBooksInWishlistDAO extends AbstractDAO<List<Book>> {

    private final int wishlistId;

    /**
     * Constructor.
     *
     * @param con the database connection
     * @param wishlistId the wishlist ID to query
     */
    public GetBooksInWishlistDAO(final Connection con, final int wishlistId) {
        super(con);
        this.wishlistId = wishlistId;
    }

    @Override
    protected void doAccess() throws Exception {
        final List<Book> books = new ArrayList<>();

        try (PreparedStatement stmnt = con.prepareStatement(GET_BOOKS_IN_WISHLIST)) {
            stmnt.setInt(1, wishlistId);

            try (ResultSet rs = stmnt.executeQuery()) {
                while (rs.next()) {
                    final int book_id = rs.getInt("book_id");
                    final String title = rs.getString("title");
                    final String language = rs.getString("language");
                    final String isbn = rs.getString("isbn");
                    final double price = rs.getDouble("price");
                    final String edition = rs.getString("edition");
                    final int publicationYear = rs.getInt("publication_year");
                    final int numberOfPages = rs.getInt("number_of_pages");
                    final int stockQuantity = rs.getInt("stock_quantity");
                    final double averageRate = rs.getDouble("average_rate");
                    final String summary = rs.getString("summary");

                    Image bookImage = null;
                    try {
                        byte[] imageData = rs.getBytes("book_pic");
                        String imageType = rs.getString("book_pic_type");
                        if (imageData != null && imageType != null) {
                            bookImage = new Image(imageData, imageType);
                        }
                    } catch (Exception ignored) {
                        LOGGER.debug("Optional image not found for book ID {} (wishlist ID {}).", book_id, wishlistId);
                    }

                    Book book = (bookImage == null)
                            ? new Book(book_id, title, language, isbn, price, edition,
                                    publicationYear, numberOfPages, stockQuantity, averageRate, summary)
                            : new Book(title, language, isbn, price, edition,
                                    publicationYear, numberOfPages, stockQuantity, averageRate, summary, bookImage);

                    books.add(book);
                }
            }

            this.outputParam = books;
            LOGGER.info("{} book(s) retrieved from wishlist ID {}.", books.size(), wishlistId);

        } catch (Exception ex) {
            LOGGER.error("Failed to retrieve books from wishlist ID {}: {}", wishlistId, ex.getMessage());
            throw ex;
        }
    }
}
