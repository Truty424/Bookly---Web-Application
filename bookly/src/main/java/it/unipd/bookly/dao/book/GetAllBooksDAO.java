package it.unipd.bookly.dao.book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import it.unipd.bookly.Resource.Book;
import it.unipd.bookly.Resource.Image;
import it.unipd.bookly.dao.AbstractDAO;
import static it.unipd.bookly.dao.book.BookQueries.GET_ALL_BOOKS;

/**
 * DAO to retrieve all books from the database. This class provides
 * functionality to fetch all book records from the database and return them as
 * a list of {@link Book} objects.
 */
public class GetAllBooksDAO extends AbstractDAO<List<Book>> {

    /**
     * Constructs a DAO to retrieve all books from the database.
     *
     * @param con The database connection to use.
     */
    public GetAllBooksDAO(final Connection con) {
        super(con);
    }

    /**
     * Executes the query to retrieve all books from the database. Populates the
     * {@link #outputParam} with a list of {@link Book} objects.
     *
     * @throws Exception If an error occurs during the database operation.
     */
    @Override
    protected void doAccess() throws Exception {
        List<Book> books = new ArrayList<>();

        try (PreparedStatement stmnt = con.prepareStatement(GET_ALL_BOOKS); ResultSet rs = stmnt.executeQuery()) {

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
                    LOGGER.debug("No image for book {}", book_id);
                }

                Book book = (bookImage == null)
                        ? new Book(book_id, title, language, isbn, price, edition, publication_year,
                                number_of_pages, stock_quantity, average_rate, summary)
                        : new Book(book_id, title, language, isbn, price, edition, publication_year,
                                number_of_pages, stock_quantity, average_rate, summary, bookImage);

                books.add(book);
            }

            this.outputParam = books;

        } catch (Exception e) {
            LOGGER.error("Error retrieving all books: {}", e.getMessage());
            throw e;
        }
    }
}
