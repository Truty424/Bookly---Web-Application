package it.unipd.bookly.dao.book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import it.unipd.bookly.Resource.Book;
import it.unipd.bookly.Resource.Image;
import it.unipd.bookly.dao.AbstractDAO;

import static it.unipd.bookly.dao.book.BookQueries.GET_TOP_RATED_BOOKS;

/**
 * DAO to retrieve top-rated books with an average rating greater than or equal to a specified threshold.
 * Returns a list of {@link Book} objects, including optional images.
 */
public class GetTopRatedBooksDAO extends AbstractDAO<List<Book>> {

    private final double minRating;

    public GetTopRatedBooksDAO(final Connection con, final double minRating) {
        super(con);
        this.minRating = minRating;
    }

    @Override
    protected void doAccess() throws Exception {
        List<Book> topRatedBooks = new ArrayList<>();

        try (PreparedStatement stmt = con.prepareStatement(GET_TOP_RATED_BOOKS)) {
            stmt.setDouble(1, minRating);

            try (ResultSet rs = stmt.executeQuery()) {
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

                    // Handle optional image from LEFT JOIN
                    Image bookImage = null;
                    byte[] imageData = rs.getBytes("image");
                    String imageType = rs.getString("image_type");
                    if (imageData != null && imageType != null) {
                        bookImage = new Image(imageData, imageType);
                        LOGGER.debug("Image found for book ID {}", bookId);
                    }

                    Book book = new Book(
                            bookId, title, language, isbn, price, edition,
                            publicationYear, numberOfPages, stockQuantity,
                            averageRate, summary, bookImage
                    );

                    topRatedBooks.add(book);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error retrieving top-rated books (minRating {}): {}", minRating, e.getMessage(), e);
            throw e;
        }

        if (topRatedBooks.isEmpty()) {
            LOGGER.info("No top-rated books found with minimum rating {}", minRating);
        } else {
            LOGGER.info("Retrieved {} top-rated book(s) with minimum rating {}", topRatedBooks.size(), minRating);
        }

        this.outputParam = topRatedBooks;
    }
}
