package it.unipd.bookly.dao.book;

import it.unipd.bookly.model.Book;
import it.unipd.bookly.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {
        private Book mapResultSetToBook(ResultSet rs) throws SQLException {
        return new Book(
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
    }
}