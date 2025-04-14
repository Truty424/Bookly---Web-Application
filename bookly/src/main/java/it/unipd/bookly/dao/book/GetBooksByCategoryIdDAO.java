package it.unipd.bookly.dao.book;

import it.unipd.bookly.Resource.Book;
import it.unipd.bookly.Resource.Image;
import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static it.unipd.bookly.dao.book.BookQueries.GET_BOOKS_BY_CATEGORY_ID;

/**
 * DAO to retrieve books by category ID.
 */
public class GetBooksByCategoryIdDAO extends AbstractDAO<List<Book>> {

    private final int category_id;

    public GetBooksByCategoryIdDAO(final Connection con, final int category_id) {
        super(con);
        this.category_id = category_id;
    }

    @Override
    protected void doAccess() throws Exception {
        List<Book> books = new ArrayList<>();

        try (PreparedStatement stmnt = con.prepareStatement(GET_BOOKS_BY_CATEGORY_ID)) {
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
                        byte[] imageData = rs.getBytes("book_pic");
                        String imageType = rs.getString("book_pic_type");
                        if (imageData != null && imageType != null) {
                            bookImage = new Image(imageData, imageType);
                        }
                    } catch (Exception ignored) {
                        LOGGER.debug("No image available for book ID {}", book_id);
                    }

                    Book book;
                    if (bookImage == null) {
                        book = new Book(book_id, title, language, isbn, price, edition,
                                publication_year, number_of_pages, stock_quantity, average_rate, summary);
                    } else {
                        book = new Book(book_id,title, language, isbn, price, edition,
                                publication_year, number_of_pages, stock_quantity, average_rate, summary, bookImage);
                    }

                    books.add(book);
                }
            }

            this.outputParam = books;

        } catch (Exception e) {
            LOGGER.error("Error retrieving books by category ID {}: {}", category_id, e.getMessage());
        }
    }
}
