package it.unipd.bookly.dao.book;

import it.unipd.bookly.Resource.Book;
import it.unipd.bookly.Resource.Image;
import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static it.unipd.bookly.dao.book.BookQueries.SEARCH_BOOK_BY_TITLE;

/**
 * DAO to search books by partial or full title.
 */
public class SearchBookByTitleDAO extends AbstractDAO<List<Book>> {

    private final String searchTerm;

    public SearchBookByTitleDAO(final Connection con, final String searchTerm) {
        super(con);
        this.searchTerm = searchTerm;
    }

    @Override
    protected void doAccess() throws Exception {
        List<Book> books = new ArrayList<>();

        try (PreparedStatement stmt = con.prepareStatement(SEARCH_BOOK_BY_TITLE)) {
            stmt.setString(1, "%" + searchTerm.trim().toLowerCase() + "%");

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

                    // Handle optional image via LEFT JOIN
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
                            publicationYear, numberOfPages, stockQuantity,
                            averageRate, summary, bookImage
                    );

                    books.add(book);
                }
            }

            this.outputParam = books;

            if (books.isEmpty()) {
                LOGGER.info("No books found for search term '{}'.", searchTerm);
            } else {
                LOGGER.info("Found {} book(s) for search term '{}'.", books.size(), searchTerm);
            }

        } catch (Exception ex) {
            LOGGER.error("Error searching books by title '{}': {}", searchTerm, ex.getMessage(), ex);
            throw ex;  // Propagate to signal error
        }
    }
}
