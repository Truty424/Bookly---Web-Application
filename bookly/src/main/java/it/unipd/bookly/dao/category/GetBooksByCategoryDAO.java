package it.unipd.bookly.dao.category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import it.unipd.bookly.Resource.Book;
import it.unipd.bookly.Resource.Image;
import it.unipd.bookly.dao.AbstractDAO;
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

        try (PreparedStatement stmnt = con.prepareStatement(GET_BOOKS_BY_CATEGORY)) {
            stmnt.setInt(1, category_id);

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
                        LOGGER.debug("No image found for book ID {} in category {}", rs.getInt("book_id"), category_id);
                    }
                    Book book = (bookImage == null)
                            ? new Book(book_id, title, language, isbn, price, edition, publication_year,
                                    number_of_pages, stock_quantity, average_rate, summary)
                            : new Book(book_id, title, language, isbn, price, edition, publication_year,
                                    number_of_pages, stock_quantity, average_rate, summary, bookImage);

                    books.add(book);
                }
            }

            this.outputParam = books;
            LOGGER.info("{} book(s) retrieved for category ID {}.", books.size(), category_id);

        } catch (Exception ex) {
            LOGGER.error("Error retrieving books for category ID {}: {}", category_id, ex.getMessage());
            throw ex;
        }
    }
}
