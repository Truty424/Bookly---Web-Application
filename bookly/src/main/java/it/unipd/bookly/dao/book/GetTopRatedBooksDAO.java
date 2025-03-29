package it.unipd.bookly.dao.book;

import it.unipd.bookly.model.Book;
import it.unipd.bookly.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {
    public List<Book> getTopRatedBooks(double minRating) {
        List<Book> books = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(BookQueries.GET_TOP_RATED_BOOKS)) {
            stmt.setDouble(1, minRating);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    books.add(mapResultSetToBook(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }
}