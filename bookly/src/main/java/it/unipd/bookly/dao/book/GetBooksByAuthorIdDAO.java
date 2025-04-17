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
 * DAO class to retrieve books by a given author ID. This class provides
 * functionality to fetch all books associated with a specific author from the
 * database and return them as a list of {@link Book} objects.
 */
public class GetBooksByAuthorIdDAO extends AbstractDAO<List<Book>> {

    /**
     * The ID of the author whose books are to be retrieved.
     */
    private final int authorId;

    /**
     * Constructs a DAO to retrieve books by a given author ID.
     *
     * @param con The database connection to use.
     * @param authorId The ID of the author whose books are to be retrieved.
     */
    public GetBooksByAuthorIdDAO(final Connection con, final int authorId) {
        super(con);
        this.authorId = authorId;
    }

    /**
     * Executes the query to retrieve books by a given author ID. Populates the
     * {@link #outputParam} with a list of {@link Book} objects.
     *
     * @throws Exception If an error occurs during the database operation.
     */
    @Override
    protected void doAccess() throws Exception {
        List<Book> books = new ArrayList<>();

        try (PreparedStatement stmnt = con.prepareStatement(GET_BOOKS_BY_AUTHOR_ID)) {
            stmnt.setInt(1, authorId);

            try (ResultSet rs = stmnt.executeQuery()) {
                while (rs.next()) {
                    int book_id = rs.getInt("book_id");
                    String title = rs.getString("title");
                    String language = rs.getString("language");
                    String isbn = rs.getString("isbn");
                    double price = rs.getDouble("price");
                    String edition = rs.getString("edition");
                    int publication_year = rs.getInt("publication_year");
                    int number_of_pages = rs.getInt("number_of_pages");
                    int stock_quantity = rs.getInt("stock_quantity");
                    double average_rate = rs.getDouble("average_rate");
                    String summary = rs.getString("summary");

                    Image bookImage = null;
                    try {
                        byte[] imageData = rs.getBytes("image");
                        String imageType = rs.getString("image_type");
                        if (imageData != null && imageType != null) {
                            bookImage = new Image(imageData, imageType);
                        }
                    } catch (Exception ignored) {
                        LOGGER.debug("Book image not available for book ID {}", book_id);
                    }

                    Book book;
                    if (bookImage == null) {
                        book = new Book(book_id, title, language, isbn, price, edition,
                                publication_year, number_of_pages, stock_quantity, average_rate, summary);
                    } else {
                        book = new Book(book_id, title, language, isbn, price, edition,
                                publication_year, number_of_pages, stock_quantity, average_rate, summary, bookImage);
                    }

                    books.add(book);
                }
            }

            this.outputParam = books;

        } catch (Exception ex) {
            LOGGER.error("Error getting books by author ID {}: {}", authorId, ex.getMessage());
        }
    }
}
