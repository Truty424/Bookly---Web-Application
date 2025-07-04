package it.unipd.bookly.dao.cart;

public final class CartQueries {

    private CartQueries() {
    }

    // --- CREATE ---
    public static final String CREATE_CART_FOR_USER
            = "INSERT INTO booklySchema.shoppingcart (user_id, shipment_method, total_price, quantity) "
            + "VALUES (?, ?::booklySchema.payment_method, 0.0, 0) "
            + "RETURNING cart_id";
    public static final String ADD_BOOK_TO_CART
            = "INSERT INTO booklySchema.contains (book_id, cart_id) VALUES (?, ?)";

    // --- READ ---
    public static final String GET_CART_BY_USER_ID
            = "SELECT * FROM booklySchema.shoppingcart WHERE user_id = ? AND order_id IS NULL";

    public static final String GET_BOOKS_IN_CART
            = "SELECT b.* FROM booklySchema.books b "
            + "JOIN booklySchema.contains c ON b.book_id = c.book_id "
            + "WHERE c.cart_id = ?";

    public static final String GET_CART_DETAILS
            = "SELECT b.book_id, b.title, b.price, c.quantity "
            + "FROM booklySchema.books b "
            + "JOIN booklySchema.contains c ON b.book_id = c.book_id "
            + "WHERE c.cart_id = ?";


    // --- UPDATE ---
    public static final String UPDATE_CART_QUANTITY
            = "UPDATE booklySchema.shoppingcart SET quantity = ? WHERE cart_id = ?";

    public static final String APPLY_DISCOUNT_TO_CART
            = "UPDATE booklySchema.shoppingcart SET discount_id = ? WHERE cart_id = ?";

    public static final String LINK_ORDER_TO_CART
            = "UPDATE booklySchema.shoppingcart SET order_id = ? WHERE cart_id = ?";

    // --- DELETE ---
    public static final String REMOVE_BOOK_FROM_CART
            = "DELETE FROM booklySchema.contains WHERE book_id = ? AND cart_id = ?";

    public static final String CLEAR_CART
            = "DELETE FROM booklySchema.contains WHERE cart_id = ?";

    public static final String DELETE_CART_BY_USER_ID
            = "DELETE FROM booklySchema.shoppingcart WHERE user_id = ?";

    public static final String GET_CART_TOTAL =
            "SELECT SUM(b.price) AS total_price " +
                    "FROM booklySchema.contains c " +
                    "JOIN booklySchema.books b ON c.book_id = b.book_id " +
                    "WHERE c.cart_id = ?";

    public static final String APPLY_DISCOUNT
            = "UPDATE booklySchema.shoppingcart SET discount_id = ? WHERE cart_id = ?";

    public static final String UPDATE_CART_TOTAL_WITH_DISCOUNT =
            "UPDATE booklySchema.shoppingcart SET total_price = ?, discount_id = ? WHERE cart_id = ?";


}
