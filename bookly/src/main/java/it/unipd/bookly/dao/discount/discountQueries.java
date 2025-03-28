package it.unipd.bookly.dao.discount;

public final class DiscountQueries {

    private DiscountQueries() {}

    // --- CREATE ---
    public static final String INSERT_DISCOUNT =
            "INSERT INTO booklySchema.discounts (code, discount_percentage, expired_date) VALUES (?, ?, ?)";

    // --- READ ---
    public static final String GET_DISCOUNT_BY_ID =
            "SELECT * FROM booklySchema.discounts WHERE discount_id = ?";

    public static final String GET_DISCOUNT_BY_CODE =
            "SELECT * FROM booklySchema.discounts WHERE code = ?";

    public static final String GET_VALID_DISCOUNT_BY_CODE =
            "SELECT * FROM booklySchema.discounts " +
            "WHERE code = ? AND expired_date >= CURRENT_DATE";

    public static final String GET_ALL_DISCOUNTS =
            "SELECT * FROM booklySchema.discounts ORDER BY expired_date DESC";

    public static final String GET_ALL_ACTIVE_DISCOUNTS =
            "SELECT * FROM booklySchema.discounts WHERE expired_date >= CURRENT_DATE ORDER BY expired_date ASC";

    public static final String COUNT_DISCOUNT_USAGE =
            "SELECT COUNT(*) FROM booklySchema.shoppingcart WHERE discount_id = ?";

    // --- UPDATE ---
    public static final String UPDATE_DISCOUNT =
            "UPDATE booklySchema.discounts SET code = ?, discount_percentage = ?, expired_date = ? WHERE discount_id = ?";

    // --- DELETE ---
    public static final String DELETE_DISCOUNT =
            "DELETE FROM booklySchema.discounts WHERE discount_id = ?";
}
