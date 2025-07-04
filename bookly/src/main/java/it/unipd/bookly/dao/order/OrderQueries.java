package it.unipd.bookly.dao.order;

public final class OrderQueries {

    private OrderQueries() {
    }

    public static final String INSERT_ORDER = """
    INSERT INTO booklySchema.orders 
        (total_price, payment_method, order_date, address, shipment_code, status)
    VALUES (?,?::booklySchema.payment_method, ?, ?, ?, ?::booklySchema.payment_status)
    RETURNING order_id""";

    // Read
    public static final String GET_ORDER_BY_ID
            = "SELECT * FROM booklySchema.orders WHERE order_id = ?";

    public static final String GET_ORDER_WITH_BOOKS =
            """
            SELECT\s
                o.order_id,
                o.payment_method,
                o.total_price,
                o.status,
                o.address,
                o.order_date,
                o.shipment_code,
                b.book_id,
                b.title,
                b.language,
                b.isbn,
                b.price,
                b.edition,
                b.publication_year,
                b.number_of_pages,
                b.stock_quantity,
                b.average_rate,
                b.summary
            FROM booklySchema.orders o
            JOIN booklySchema.order_items oi
              ON o.order_id = oi.order_id
            JOIN booklySchema.books b
              ON b.book_id = oi.book_id
            WHERE o.order_id = ?
           \s""";


    public static final String GET_ALL_ORDERS
            = "SELECT * FROM booklySchema.orders";


    public static final String GET_ORDERS_BY_USER
            = "SELECT o.* FROM booklySchema.orders o "
            + "JOIN booklySchema.shoppingcart s ON o.order_id = s.order_id "
            + "WHERE s.user_id = ?";

    public static final String GET_LATEST_ORDER_FOR_USER
            = "SELECT o.* FROM booklySchema.orders o "
            + "JOIN booklySchema.shoppingcart s ON o.order_id = s.order_id "
            + "WHERE s.user_id = ? ORDER BY o.order_date DESC LIMIT 1";

    // Update
    public static final String UPDATE_ORDER_STATUS
            = "UPDATE booklySchema.orders SET status = ? WHERE order_id = ?";

    public static final String UPDATE_ORDER_PAYMENT_INFO
            = "UPDATE booklySchema.orders SET total_price = ?, payment_method = ?::booklySchema.payment_method, payment_date = ? "
            + "WHERE order_id = ?";

    public static final String CANCEL_ORDER
            = "UPDATE booklySchema.orders SET status = ?::booklySchema.payment_status WHERE order_id = ?";

    public static final String INSERT_ORDER_ITEMS =
    """
    INSERT INTO booklySchema.order_items (order_id, book_id, quantity)
    SELECT ?, book_id, 1
    FROM booklySchema.contains
    WHERE cart_id = ?
    """;

}
