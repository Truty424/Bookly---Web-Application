package it.unipd.bookly.dao.book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import it.unipd.bookly.Resource.Book;
import it.unipd.bookly.Resource.Image;
import it.unipd.bookly.dao.AbstractDAO;
import static it.unipd.bookly.dao.book.BookQueries.GET_BOOK_BY_ID;

/**
 * DAO to retrieve a book by its ID from the database.
 */
public class GetBookByIdDAO extends AbstractDAO<Book> {

    private final int bookId;

    public GetBookByIdDAO(final Connection con, final int bookId) {
        super(con);
        this.bookId = bookId;
    }

    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmt = con.prepareStatement(GET_BOOK_BY_ID)) {
            stmt.setInt(1, bookId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Basic book fields
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

                    // Handle optional image (LEFT JOIN)
                    byte[] imageData = rs.getBytes("image");
                    String imageType = rs.getString("image_type");
                    Image bookImage = null;

                    if (imageData != null && imageType != null) {
                        bookImage = new Image(imageData, imageType);
                        LOGGER.debug("Image found for book ID {}", bookId);
                    } else {
                        LOGGER.debug("No image found for book ID {}", bookId);
                    }

                    this.outputParam = new Book(
                            bookId, title, language, isbn, price, edition,
                            publicationYear, numberOfPages, stockQuantity, averageRate,
                            summary, bookImage
                    );

                    LOGGER.info("Book ID {} retrieved successfully.", bookId);
                } else {
                    LOGGER.warn("No book found with ID {}", bookId);
                    this.outputParam = null;
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error retrieving book ID {}: {}", bookId, e.getMessage(), e);
            throw e;
        }
    }
}
