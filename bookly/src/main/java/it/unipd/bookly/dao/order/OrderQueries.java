package it.unipd.bookly.dao.order;

public final class OrderQueries  {

    private OrderQueries() {
    }

    // Create
    public static final String INSERT_ORDER =
            "INSERT INTO booklySchema.orders (total_price, payment_method, payment_date, status) " +
            "VALUES (?, ?, ?, ?) RETURNING order_id";

    // Read
    public static final String GET_ORDER_BY_ID =
            "SELECT * FROM booklySchema.orders WHERE order_id = ?";

    public static final String GET_ALL_ORDERS =
            "SELECT * FROM booklySchema.orders";

    public static final String GET_ORDERS_BY_USER =
            "SELECT o.* FROM booklySchema.orders o " +
            "JOIN booklySchema.shoppingcart s ON o.order_id = s.order_id " +
            "WHERE s.user_id = ? ORDER BY o.payment_date DESC";

    public static final String GET_ORDERS_BY_STATUS =
            "SELECT * FROM booklySchema.orders WHERE status = ?";

    public static final String GET_LATEST_ORDER_FOR_USER =
            "SELECT o.* FROM booklySchema.orders o " +
            "JOIN booklySchema.shoppingcart s ON o.order_id = s.order_id " +
            "WHERE s.user_id = ? ORDER BY o.payment_date DESC LIMIT 1";

    // Update
    public static final String UPDATE_ORDER_STATUS =
            "UPDATE booklySchema.orders SET status = ? WHERE order_id = ?";

    public static final String UPDATE_ORDER_PAYMENT_INFO =
            "UPDATE booklySchema.orders SET total_price = ?, payment_method = ?, payment_date = ? " +
            "WHERE order_id = ?";

    // "Soft delete" (optional): Cancel instead of delete
    public static final String CANCEL_ORDER =
            "UPDATE booklySchema.orders SET status = 'cancelled' WHERE order_id = ?";
}
