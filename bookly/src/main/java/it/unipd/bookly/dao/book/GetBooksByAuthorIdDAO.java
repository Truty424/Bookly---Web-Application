package it.unipd.bookly.dao.book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import it.unipd.bookly.Resource.Book;
import it.unipd.bookly.Resource.Image;
import it.unipd.bookly.dao.AbstractDAO;

import static it.unipd.bookly.dao.book.BookQueries.GET_BOOKS_BY_AUTHOR_ID;

/**
 * DAO to retrieve books by a specific author ID, including optional images.
 */
public class GetBooksByAuthorIdDAO extends AbstractDAO<List<Book>> {

    private final int authorId;

    public GetBooksByAuthorIdDAO(final Connection con, final int authorId) {
        super(con);
        this.authorId = authorId;
    }

    @Override
    protected void doAccess() throws Exception {
        List<Book> books = new ArrayList<>();

        try (PreparedStatement stmt = con.prepareStatement(GET_BOOKS_BY_AUTHOR_ID)) {
            stmt.setInt(1, authorId);

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

                    // Handle optional image (from LEFT JOIN)
                    Image bookImage = null;
                    try {
                        byte[] imageData = rs.getBytes("image");
                        String imageType = rs.getString("image_type");
                        if (imageData != null && imageType != null) {
                            bookImage = new Image(imageData, imageType);
                            LOGGER.debug("Image found for book ID {}", bookId);
                        }
                    } catch (Exception imgEx) {
                        LOGGER.debug("No image available or error loading image for book ID {}", bookId);
                    }

                    Book book = new Book(
                            bookId, title, language, isbn, price, edition,
                            publicationYear, numberOfPages, stockQuantity, averageRate,
                            summary, bookImage
                    );

                    books.add(book);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error retrieving books for author ID {}: {}", authorId, e.getMessage(), e);
            throw e;
        }

        if (books.isEmpty()) {
            LOGGER.info("No books found for author ID {}", authorId);
        } else {
            LOGGER.info("{} book(s) retrieved for author ID {}", books.size(), authorId);
        }

        this.outputParam = books;
    }
}
