package it.unipd.bookly.dao.book;

import it.unipd.bookly.model.Book;
import it.unipd.bookly.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {
        public boolean updateBook(Book book) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(BookQueries.UPDATE_BOOK)) {

            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getLanguage());
            stmt.setString(3, book.getIsbn());
            stmt.setDouble(4, book.getPrice());
            stmt.setString(5, book.getEdition());
            stmt.setInt(6, book.getPublicationYear());
            stmt.setInt(7, book.getNumberOfPages());
            stmt.setInt(8, book.getStockQuantity());
            stmt.setDouble(9, book.getAverageRate());
            stmt.setString(10, book.getSummary());
            stmt.setInt(11, book.getBookId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}