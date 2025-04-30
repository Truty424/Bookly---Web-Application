package it.unipd.bookly.dao.category;

import it.unipd.bookly.Resource.Book;
import it.unipd.bookly.Resource.Image;
import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static it.unipd.bookly.dao.category.CategoryQueries.GET_BOOKS_BY_CATEGORY;

/**
 * DAO class to retrieve all books that belong to a given category.
 */
public class GetBooksByCategoryDAO extends AbstractDAO<List<Book>> {

    private final int category_id;

    public GetBooksByCategoryDAO(final Connection con, final int category_id) {
        super(con);
        this.category_id = category_id;
    }

    @Override
    protected void doAccess() throws Exception {
        List<Book> books = new ArrayList<>();

        try (PreparedStatement stmt = con.prepareStatement(GET_BOOKS_BY_CATEGORY)) {
            stmt.setInt(1, category_id);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int bookId = rs.getInt("book_id");
                    String title = rs.getString("title");
                    String language = rs.getString("language");
                    String isbn = rs.getString("isbn");
                    double price = rs.getDouble("price");
                    String edition = rs.getString("edition");
                    int publicationYear = rs.getInt("publication_year");
                    int pages = rs.getInt("number_of_pages");
                    int stock = rs.getInt("stock_quantity");
                    double averageRate = rs.getDouble("average_rate");
                    String summary = rs.getString("summary");

                    Image image = null;
                    byte[] imageData = rs.getBytes("image");
                    String imageType = rs.getString("image_type");
                    if (imageData != null && imageType != null) {
                        image = new Image(imageData, imageType);
                    }

                    Book book = (image == null)
                        ? new Book(bookId, title, language, isbn, price, edition, publicationYear, pages, stock, averageRate, summary)
                        : new Book(bookId, title, language, isbn, price, edition, publicationYear, pages, stock, averageRate, summary, image);

                    books.add(book);
                }
            }

            if (books.isEmpty()) {
                LOGGER.warn("No books found for category ID {}", category_id);
            } else {
                LOGGER.info("{} book(s) retrieved for category ID {}.", books.size(), category_id);
            }

            this.outputParam = books;

        } catch (Exception ex) {
            LOGGER.error("Error retrieving books for category ID {}: {}", category_id, ex.getMessage());
            throw ex;
        }
    }
}
