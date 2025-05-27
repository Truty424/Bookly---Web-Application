package it.unipd.bookly.dao.wishlist;

public class WishlistQueries {
    public static final String CREATE_WISHLIST =
        "INSERT INTO booklySchema.wishlists (user_id) VALUES (?) RETURNING wishlist_id";

    public static final String GET_WISHLIST_BY_USER =
        "SELECT wishlist_id FROM booklySchema.wishlists WHERE user_id = ?";

    public static final String ADD_BOOK_TO_WISHLIST =
        "INSERT INTO booklySchema.contains_wishlist (wishlist_id, book_id) VALUES (?, ?)";

    public static final String REMOVE_BOOK_FROM_WISHLIST =
        "DELETE FROM booklySchema.contains_wishlist WHERE wishlist_id = ? AND book_id = ?";

    public static final String IS_BOOK_IN_WISHLIST =
        "SELECT EXISTS (SELECT 1 FROM booklySchema.contains_wishlist WHERE wishlist_id = ? AND book_id = ?)";

    public static final String GET_BOOKS_IN_WISHLIST =
        "SELECT b.* FROM booklySchema.books b " +
        "JOIN booklySchema.contains_wishlist cw ON b.book_id = cw.book_id " +
        "WHERE cw.wishlist_id = ?";

    public static final String COUNT_BOOKS_IN_WISHLIST =
        "SELECT COUNT(*) FROM booklySchema.contains_wishlist WHERE wishlist_id = ?";

}