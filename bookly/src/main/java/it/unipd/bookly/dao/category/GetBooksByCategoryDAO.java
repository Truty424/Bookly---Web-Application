package it.unipd.bookly.dao.category;

import it.unipd.bookly.Resource.Book;
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

    private final int categoryId;


    public GetBooksByCategoryDAO(final Connection con, final int categoryId) {
        super(con);
        this.categoryId = categoryId;
    }

    @Override
    protected void doAccess() throws Exception {
        List<Book> books = new ArrayList<>();

        try (PreparedStatement stmnt = con.prepareStatement(GET_BOOKS_BY_CATEGORY)) {
            stmnt.setInt(1, categoryId);

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
            LOGGER.info("{} book(s) retrieved for category ID {}.", books.size(), categoryId);

        } catch (Exception ex) {
            LOGGER.error("Error retrieving books for category ID {}: {}", categoryId, ex.getMessage());
            throw ex;
        }
    }
}
