package it.unipd.bookly.dao.wishlist;

import it.unipd.bookly.Resource.Book;
import it.unipd.bookly.Resource.Image;
import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static it.unipd.bookly.dao.wishlist.WishlistQueries.GET_WISHLIST_BOOKS_BY_USER;

/**
 * DAO class to retrieve all books in the wishlist of a specific user.
 */
public class GetWishlistByUserDAO extends AbstractDAO<List<Book>> {

    private final int userId;

    /**
     * Constructor.
     *
     * @param con    the database connection
     * @param userId the ID of the user whose wishlist should be retrieved
     */
    public GetWishlistByUserDAO(final Connection con, final int userId) {
        super(con);
        this.userId = userId;
    }

    @Override
    protected void doAccess() throws Exception {
        final List<Book> wishlistBooks = new ArrayList<>();

        try (PreparedStatement stmnt = con.prepareStatement(GET_WISHLIST_BOOKS_BY_USER)) {
            stmnt.setInt(1, userId);

            try (ResultSet rs = stmnt.executeQuery()) {
                while (rs.next()) {
                    int bookId = rs.getInt("book_id");
                    String title = rs.getString("title");
                    String language = rs.getString("language");
                    String isbn = rs.getString("isbn");
                    double price = rs.getDouble("price");
                    String edition = rs.getString("edition");
                    int publicationYear = rs.getInt("publication_year");
                    int numberOfPages = rs.getInt("number_of_pages");
                    int stockQuantity = rs.getInt("stock_quantity");
                    double averageRate = rs.getDouble("average_rate");
                    String summary = rs.getString("summary");

                    // Optional image
                    byte[] imageBytes = rs.getBytes("image");
                    String imageType = rs.getString("image_type");
                    Image image = null;
                    if (imageBytes != null && imageType != null) {
                        image = new Image(imageBytes, imageType);
                    }

                    Book book = (image == null)
                        ? new Book(bookId, title, language, isbn, price, edition, publicationYear, numberOfPages, stockQuantity, averageRate, summary)
                        : new Book(bookId, title, language, isbn, price, edition, publicationYear, numberOfPages, stockQuantity, averageRate, summary, image);

                    wishlistBooks.add(book);
                }
            }

            this.outputParam = wishlistBooks;
            LOGGER.info("Retrieved {} book(s) in wishlist for user ID {}.", wishlistBooks.size(), userId);

        } catch (Exception ex) {
            LOGGER.error("Failed to retrieve wishlist books for user ID {}: {}", userId, ex.getMessage());
            throw ex;
        }
    }
}
