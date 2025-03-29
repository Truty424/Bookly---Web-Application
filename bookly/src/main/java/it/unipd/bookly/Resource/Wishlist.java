package it.unipd.bookly.Resource;

import java.sql.Timestamp;
import java.util.List;

/**
 * Represents a user's wishlist in the Bookly system.
 */
public class Wishlist {

    private int wishlistId;
    private int userId;
    private Timestamp createdAt;

    private List<Book> books;

    public Wishlist() {
        // Default constructor
    }

    public Wishlist(int wishlistId, int userId, Timestamp createdAt) {
        this.wishlistId = wishlistId;
        this.userId = userId;
        this.createdAt = createdAt;
    }

    public Wishlist(int userId) {
        this.userId = userId;
        this.createdAt = new Timestamp(System.currentTimeMillis());
    }

    // --- Getters ---
    public int getWishlistId() {
        return wishlistId;
    }

    public int getUserId() {
        return userId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public List<Book> getBooks() {
        return books;
    }

    // --- Setters ---
    public void setWishlistId(int wishlistId) {
        this.wishlistId = wishlistId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
