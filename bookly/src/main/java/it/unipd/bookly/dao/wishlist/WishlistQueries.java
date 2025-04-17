package it.unipd.bookly.dao.wishlist;

public final class WishlistQueries {

    private WishlistQueries() {
    }

    // --- CREATE ---
    public static final String CREATE_WISHLIST
            = "INSERT INTO booklySchema.wishlists (user_id, created_at) VALUES (?, CURRENT_TIMESTAMP) RETURNING wishlist_id, created_at";

    public static final String ADD_BOOK_TO_WISHLIST
            = "INSERT INTO booklySchema.contains_wishlist (wishlist_id, book_id) VALUES (?, ?) ON CONFLICT DO NOTHING";

    // --- READ ---
    public static final String GET_WISHLIST_BY_USER
            = "SELECT * FROM booklySchema.wishlists WHERE user_id = ?";

    public static final String GET_BOOKS_IN_WISHLIST
            = "SELECT b.*, i.image, i.image_type "
            + "FROM booklySchema.books b "
            + "JOIN booklySchema.contains_wishlist wc ON b.book_id = wc.book_id "
            + "LEFT JOIN booklySchema.book_image i ON b.book_id = i.book_id "
            + "WHERE wc.wishlist_id = ?";

    public static final String IS_BOOK_IN_WISHLIST
            = "SELECT 1 FROM booklySchema.contains_wishlist WHERE wishlist_id = ? AND book_id = ?";

    public static final String COUNT_BOOKS_IN_WISHLIST
            = "SELECT COUNT(*) FROM booklySchema.contains_wishlist WHERE wishlist_id = ?";

    // --- DELETE ---
    public static final String REMOVE_BOOK_FROM_WISHLIST
            = "DELETE FROM booklySchema.contains_wishlist WHERE wishlist_id = ? AND book_id = ?";

    public static final String DELETE_WISHLIST
            = "DELETE FROM booklySchema.wishlists WHERE wishlist_id = ?";

    public static final String CLEAR_WISHLIST
            = "DELETE FROM booklySchema.contains_wishlist WHERE wishlist_id = ?";
}
