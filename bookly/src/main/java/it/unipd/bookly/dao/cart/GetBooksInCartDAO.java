package it.unipd.bookly.dao.cart;

import it.unipd.bookly.Resource.Book;
import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static it.unipd.bookly.dao.cart.CartQueries.GET_BOOKS_IN_CART;

/**
 * DAO class to retrieve all books in a specific cart.
 */
public class GetBooksInCartDAO extends AbstractDAO<List<Book>> {

    private final int cartId;


    public GetBooksInCartDAO(final Connection con, final int cartId) {
        super(con);
        this.cartId = cartId;
    }

    @Override
    protected void doAccess() throws Exception {
        List<Book> books = new ArrayList<>();

        try (PreparedStatement stmnt = con.prepareStatement(GET_BOOKS_IN_CART)) {
            stmnt.setInt(1, cartId);

            try (ResultSet rs = stmnt.executeQuery()) {
                while (rs.next()) {
                    Book book = new Book(
                            rs.getInt("book_id"),
                            rs.getString("title"),
                            rs.getString("language"),
                            rs.getString("isbn"),
                            rs.getDouble("price"),
                            rs.getString("edition"),
                            rs.getInt("publication_year"),
                            rs.getInt("number_of_pages"),
                            rs.getInt("stock_quantity"),
                            rs.getDouble("average_rate"),
                            rs.getString("summary")
                    );
                    books.add(book);
                }
            }

            this.outputParam = books;
            LOGGER.info("{} book(s) retrieved from cart ID {}.", books.size(), cartId);

        } catch (Exception ex) {
            LOGGER.error("Error retrieving books from cart {}: {}", cartId, ex.getMessage());
            throw ex;
        }
    }
}
