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
 * DAO to retrieve books by a specific author ID.
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
        boolean previousAutoCommit = con.getAutoCommit();  // Save the original state

        try {
            // Optional: Begin transaction for read-consistency (though not strictly needed for SELECT)
            con.setAutoCommit(false);

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

                        // Optional image handling
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
                                publicationYear, numberOfPages, stockQuantity, averageRate,
                                summary, bookImage
                        );

                        books.add(book);
                    }
                }
            }

            this.outputParam = books;

            // Optionally commit the read-only transaction (noop for SELECT but OK)
            con.commit();

            if (books.isEmpty()) {
                LOGGER.info("No books found for author ID {}", authorId);
            } else {
                LOGGER.info("{} book(s) retrieved for author ID {}", books.size(), authorId);
            }

        } catch (Exception ex) {
            // Rollback only matters if we had write ops (safe to include for pattern)
            try {
                con.rollback();
            } catch (Exception rollbackEx) {
                LOGGER.error("Rollback failed after error: {}", rollbackEx.getMessage(), rollbackEx);
            }
            LOGGER.error("Error retrieving books for author ID {}: {}", authorId, ex.getMessage(), ex);
            throw ex;
        } finally {
            // Always restore the original auto-commit state
            con.setAutoCommit(previousAutoCommit);
        }
    }
}
