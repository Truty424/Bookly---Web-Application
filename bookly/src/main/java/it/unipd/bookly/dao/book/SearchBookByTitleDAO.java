package it.unipd.bookly.dao.book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import it.unipd.bookly.Resource.Book;
import it.unipd.bookly.Resource.Image;
import it.unipd.bookly.dao.AbstractDAO;
import static it.unipd.bookly.dao.book.BookQueries.SEARCH_BOOK_BY_TITLE;

/**
 * DAO to search books by partial or full title. This class provides
 * functionality to search for books in the database based on a search term and
 * return them as a list of {@link Book} objects.
 */
public class SearchBookByTitleDAO extends AbstractDAO<List<Book>> {

    /**
     * The search term used to find books by title.
     */
    private final String searchTerm;

    /**
     * Constructs a DAO to search books by partial or full title.
     *
     * @param con The database connection to use.
     * @param searchTerm The search term used to find books by title.
     */
    public SearchBookByTitleDAO(final Connection con, final String searchTerm) {
        super(con);
        this.searchTerm = searchTerm;
    }

    /**
     * Executes the query to search books by partial or full title. Populates
     * the {@link #outputParam} with a list of {@link Book} objects.
     *
     * @throws Exception If an error occurs during the database operation.
     */
    @Override
    protected void doAccess() throws Exception {
        List<Book> books = new ArrayList<>();

        try (PreparedStatement stmnt = con.prepareStatement(SEARCH_BOOK_BY_TITLE)) {
            stmnt.setString(1, "%" + searchTerm + "%");

            try (ResultSet rs = stmnt.executeQuery()) {
                while (rs.next()) {
                    int book_id = rs.getInt("book_id");
                    String title = rs.getString("title").toLowerCase();
                    String language = rs.getString("language") != null ? rs.getString("language").toLowerCase() : null;
                    String isbn = rs.getString("isbn") != null ? rs.getString("isbn").toLowerCase() : null;
                    double price = rs.getDouble("price");
                    String edition = rs.getString("edition") != null ? rs.getString("edition").toLowerCase() : null;
                    int publication_year = rs.getInt("publication_year");
                    int number_of_pages = rs.getInt("number_of_pages");
                    int stock_quantity = rs.getInt("stock_quantity");
                    double average_rate = rs.getDouble("average_rate");
                    String summary = rs.getString("summary") != null ? rs.getString("summary").toLowerCase() : null;

                    Image bookImage = null;
                    try {
                        byte[] imageData = rs.getBytes("image");
                        String imageType = rs.getString("image_type");
                        if (imageData != null && imageType != null) {
                            bookImage = new Image(imageData, imageType);
                        }
                    } catch (Exception ignored) {
                        LOGGER.debug("No image found for book ID {} in search.", book_id);
                    }

                    Book book = (bookImage == null)
                            ? new Book(book_id, title, language, isbn, price, edition,
                                    publication_year, number_of_pages, stock_quantity, average_rate, summary)
                            : new Book(book_id, title, language, isbn, price, edition,
                                    publication_year, number_of_pages, stock_quantity, average_rate, summary, bookImage);

                    books.add(book);
                }
            }

            this.outputParam = books;
            LOGGER.info("{} books found for search '{}'.", books.size(), searchTerm);

        } catch (Exception ex) {
            LOGGER.error("Error searching books by title '{}': {}", searchTerm, ex.getMessage());
        }
    }
}
