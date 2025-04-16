package it.unipd.bookly.Resource;

import java.sql.Timestamp;
import java.util.List;

/**
 * Represents a user's wishlist in the Bookly system.
 * A wishlist contains a list of books that the user has added for future reference.
 */
public class Wishlist {

    private int wishlistId;
    private int userId;
    private Timestamp createdAt;

    private List<Book> books;

    /**
     * Default constructor for Wishlist.
     */
    public Wishlist() {
        // Default constructor
    }

    /**
     * Constructs a Wishlist with all attributes.
     *
     * @param wishlistId The ID of the wishlist.
     * @param userId     The ID of the user who owns the wishlist.
     * @param createdAt  The timestamp when the wishlist was created.
     */
    public Wishlist(int wishlistId, int userId, Timestamp createdAt) {
        this.wishlistId = wishlistId;
        this.userId = userId;
        this.createdAt = createdAt;
    }

    /**
     * Constructs a Wishlist for a specific user.
     *
     * @param userId The ID of the user who owns the wishlist.
     */
    public Wishlist(int userId) {
        this.userId = userId;
        this.createdAt = new Timestamp(System.currentTimeMillis());
    }

    /**
     * Gets the ID of the wishlist.
     *
     * @return The wishlist ID.
     */
    public int getWishlistId() {
        return wishlistId;
    }

    /**
     * Gets the ID of the user who owns the wishlist.
     *
     * @return The user ID.
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Gets the timestamp when the wishlist was created.
     *
     * @return The creation timestamp.
     */
    public Timestamp getCreatedAt() {
        return createdAt;
    }

    /**
     * Gets the list of books in the wishlist.
     *
     * @return The list of books.
     */
    public List<Book> getBooks() {
        return books;
    }

    /**
     * Sets the ID of the wishlist.
     *
     * @param wishlistId The wishlist ID.
     */
    public void setWishlistId(int wishlistId) {
        this.wishlistId = wishlistId;
    }

    /**
     * Sets the ID of the user who owns the wishlist.
     *
     * @param userId The user ID.
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Sets the timestamp when the wishlist was created.
     *
     * @param createdAt The creation timestamp.
     */
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Sets the list of books in the wishlist.
     *
     * @param books The list of books.
     */
    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
