package it.unipd.bookly.dao.wishlist;

public final class WishlistQueries {

    private WishlistQueries() {}

    // --- CREATE ---
    public static final String CREATE_WISHLIST =
            "INSERT INTO booklySchema.wishlists (user_id) VALUES (?) RETURNING wishlist_id";

    public static final String ADD_BOOK_TO_WISHLIST =
            "INSERT INTO booklySchema.contains_wishlist (wishlist_id, book_id) VALUES (?, ?)";

    // --- READ ---
    public static final String GET_WISHLIST_BY_USER =
            "SELECT * FROM booklySchema.wishlists WHERE user_id = ?";

    public static final String GET_BOOKS_IN_WISHLIST =
            "SELECT b.book_id, b.title, b.price FROM booklySchema.books b " +
            "JOIN booklySchema.contains_wishlist cw ON b.book_id = cw.book_id " +
            "WHERE cw.wishlist_id = ?";

    public static final String IS_BOOK_IN_WISHLIST =
            "SELECT 1 FROM booklySchema.contains_wishlist WHERE wishlist_id = ? AND book_id = ?";

    public static final String COUNT_BOOKS_IN_WISHLIST =
            "SELECT COUNT(*) FROM booklySchema.contains_wishlist WHERE wishlist_id = ?";

    // --- DELETE ---
    public static final String REMOVE_BOOK_FROM_WISHLIST =
            "DELETE FROM booklySchema.contains_wishlist WHERE wishlist_id = ? AND book_id = ?";

    public static final String DELETE_WISHLIST =
            "DELETE FROM booklySchema.wishlists WHERE wishlist_id = ?";

    public static final String CLEAR_WISHLIST =
            "DELETE FROM booklySchema.contains_wishlist WHERE wishlist_id = ?";
}
