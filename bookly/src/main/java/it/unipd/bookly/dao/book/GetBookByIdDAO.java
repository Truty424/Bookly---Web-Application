package it.unipd.bookly.dao.book;

import it.unipd.bookly.Resource.Book;
import it.unipd.bookly.Resource.Image;
import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static it.unipd.bookly.dao.book.BookQueries.GET_BOOK_BY_ID;

public class GetBookByIdDAO extends AbstractDAO<Book> {

    private final int book_id;

    public GetBookByIdDAO(final Connection con, final int book_id) {
        super(con);
        this.book_id = book_id;
    }

    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmnt = con.prepareStatement(GET_BOOK_BY_ID)) {
            stmnt.setInt(1, book_id);

            try (ResultSet rs = stmnt.executeQuery()) {
                if (rs.next()) {
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
                        LOGGER.debug("No image for book {}", book_id);
                    }

                    if (bookImage == null) {
                        this.outputParam = new Book(book_id, title, language, isbn, price, edition,
                                publication_year, number_of_pages, stock_quantity, average_rate, summary);
                    } else {
                        this.outputParam = new Book(book_id, title, language, isbn, price, edition,
                                publication_year, number_of_pages, stock_quantity, average_rate, summary, bookImage);
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error retrieving book {}: {}", book_id, e.getMessage());
        }
    }
}
