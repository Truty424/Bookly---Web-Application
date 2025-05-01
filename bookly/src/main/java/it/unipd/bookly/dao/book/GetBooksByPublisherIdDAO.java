package it.unipd.bookly.dao.book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import it.unipd.bookly.Resource.Book;
import it.unipd.bookly.Resource.Image;
import it.unipd.bookly.dao.AbstractDAO;

import static it.unipd.bookly.dao.book.BookQueries.GET_BOOKS_BY_PUBLISHER_ID;

/**
 * DAO to retrieve books by a specific publisher ID.
 * Fetches all books associated with a specific publisher from the database and returns them as a list of {@link Book} objects.
 */
public class GetBooksByPublisherIdDAO extends AbstractDAO<List<Book>> {

    private final int publisherId;

    public GetBooksByPublisherIdDAO(final Connection con, final int publisherId) {
        super(con);
        this.publisherId = publisherId;
    }

    @Override
    protected void doAccess() throws Exception {
        List<Book> books = new ArrayList<>();
        boolean previousAutoCommit = con.getAutoCommit();  // Save current state

        try {
            con.setAutoCommit(false);  // Optional: standardize pattern even for SELECTs

            try (PreparedStatement stmt = con.prepareStatement(GET_BOOKS_BY_PUBLISHER_ID)) {
                stmt.setInt(1, publisherId);

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

                        // Handle optional image
                        byte[] imageData = rs.getBytes("image");
                        String imageType = rs.getString("image_type");
                        Image bookImage = (imageData != null && imageType != null)
                                ? new Image(imageData, imageType)
                                : null;

                        if (bookImage != null) {
                            LOGGER.debug("Image found for book ID {}", bookId);
                        }

                        Book book = new Book(
                                bookId, title, language, isbn, price, edition,
                                publicationYear, numberOfPages, stockQuantity, averageRate, summary, bookImage
                        );

                        books.add(book);
                    }
                }
            }

            con.commit();  // No-op if autocommit was true

            if (books.isEmpty()) {
                LOGGER.info("No books found for publisher ID {}", publisherId);
            } else {
                LOGGER.info("Retrieved {} book(s) for publisher ID {}", books.size(), publisherId);
            }

            this.outputParam = books;

        } catch (Exception e) {
            LOGGER.error("Error retrieving books by publisher ID {}: {}", publisherId, e.getMessage(), e);
            try {
                con.rollback();
            } catch (Exception rollbackEx) {
                LOGGER.error("Rollback failed: {}", rollbackEx.getMessage(), rollbackEx);
            }
            throw e;  // Re-throw to let caller handle
        } finally {
            con.setAutoCommit(previousAutoCommit);  // Restore
        }
    }
}
