package it.unipd.bookly.dao.book;

import it.unipd.bookly.Resource.Book;
import it.unipd.bookly.Resource.Image;
import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static it.unipd.bookly.dao.book.BookQueries.GET_TOP_RATED_BOOKS;

/**
 * DAO to retrieve top-rated books with average_rate >= threshold.
 */
public class GetTopRatedBooksDAO extends AbstractDAO<List<Book>> {

    private final double minRating;

    public GetTopRatedBooksDAO(final Connection con, final double minRating) {
        super(con);
        this.minRating = minRating;
    }

    @Override
    protected void doAccess() throws Exception {
        List<Book> topRatedBooks = new ArrayList<>();

        try (PreparedStatement stmnt = con.prepareStatement(GET_TOP_RATED_BOOKS)) {
            stmnt.setDouble(1, minRating);

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
                        byte[] imageData = rs.getBytes("book_pic");
                        String imageType = rs.getString("book_pic_type");
                        if (imageData != null && imageType != null) {
                            bookImage = new Image(imageData, imageType);
                        }
                    } catch (Exception ignored) {
                        LOGGER.debug("No image found for book ID {}", book_id);
                    }

                    Book book = (bookImage == null)
                            ? new Book(book_id, title, language, isbn, price, edition,
                            publication_year, number_of_pages, stock_quantity, average_rate, summary)
                            : new Book(book_id,title, language, isbn, price, edition,
                            publication_year, number_of_pages, stock_quantity, average_rate, summary, bookImage);

                    topRatedBooks.add(book);
                }
            }

            this.outputParam = topRatedBooks;

        } catch (Exception e) {
            LOGGER.error("Error retrieving top-rated books with min rating {}: {}", minRating, e.getMessage());
        }
    }
}
