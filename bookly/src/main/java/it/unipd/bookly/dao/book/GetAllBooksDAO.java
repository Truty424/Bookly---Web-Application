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
 * DAO to retrieve all books from the database, including optional images.
 */
public class GetAllBooksDAO extends AbstractDAO<List<Book>> {

    public GetAllBooksDAO(final Connection con) {
        super(con);
    }

    @Override
    protected void doAccess() throws Exception {
        List<Book> books = new ArrayList<>();

        try (PreparedStatement stmt = con.prepareStatement(GET_ALL_BOOKS);
             ResultSet rs = stmt.executeQuery()) {

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

                // Handle optional book image (via LEFT JOIN)
                byte[] imageData = rs.getBytes("image");
                String imageType = rs.getString("image_type");
                Image bookImage = null;

                if (imageData != null && imageType != null) {
                    bookImage = new Image(imageData, imageType);
                } else {
                    LOGGER.debug("No image found for book ID {}", bookId);
                }

                // Create the Book object with or without image
                Book book = new Book(
                        bookId, title, language, isbn, price, edition, publicationYear,
                        numberOfPages, stockQuantity, averageRate, summary, bookImage
                );

                books.add(book);
            }

            this.outputParam = books;
            LOGGER.info("{} books successfully retrieved from the database.", books.size());

        } catch (Exception e) {
            LOGGER.error("Failed to retrieve books: {}", e.getMessage(), e);
            throw e;
        }
    }
}
