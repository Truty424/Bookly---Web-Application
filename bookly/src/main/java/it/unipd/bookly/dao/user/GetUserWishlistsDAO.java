package it.unipd.bookly.dao.wishlist;

import it.unipd.bookly.model.Book;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GetUserWishlistsDAO {
    private static final String GET_USER_WISHLIST =
        "SELECT b.book_id, b.title, b.author, b.price " +
        "FROM booklySchema.books b " +
        "JOIN booklySchema.contains_wishlist cw ON b.book_id = cw.book_id " +
        "JOIN booklySchema.wishlists w ON cw.wishlist_id = w.wishlist_id " +
        "WHERE w.user_id = ?";

    public List<Book> getUserWishlist(Connection connection, int userId) throws SQLException {
        List<Book> wishlist = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(GET_USER_WISHLIST)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    wishlist.add(new Book(
                        rs.getInt("book_id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getBigDecimal("price")
                    ));
                }
            }
        }
        return wishlist;
    }
}