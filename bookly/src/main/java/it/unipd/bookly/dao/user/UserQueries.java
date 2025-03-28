package it.unipd.bookly.dao.user;

public final class UserQueries {

    private UserQueries() {
    }

    public static final String LOGIN_USER =
            "SELECT user_id, username, password, first_name, last_name, email, phone, address, role " +
            "FROM booklySchema.users WHERE username = ? AND password = md5(?)";

    public static final String REGISTER_USER =
            "INSERT INTO booklySchema.users (username, password, first_name, last_name, email, phone, address, role) " +
            "VALUES (?, md5(?), ?, ?, ?, ?, ?, ?)";

    public static final String UPDATE_USER =
            "UPDATE booklySchema.users SET first_name = ?, last_name = ?, password = md5(?), email = ?, phone = ?, address = ? " +
            "WHERE username = ?";

    public static final String GET_USER_BY_USERNAME =
            "SELECT * FROM booklySchema.users WHERE username = ?";

    public static final String GET_USER_BY_ID =
            "SELECT * FROM booklySchema.users WHERE user_id = ?";

    public static final String GET_USER_IMAGE =
            "SELECT * FROM booklySchema.user_image WHERE user_id = ?";

    public static final String INSERT_USER_IMAGE =
            "INSERT INTO booklySchema.user_image (user_id, image, image_type) VALUES (?, ?, ?)";

    public static final String UPDATE_USER_IMAGE_IF_EXISTS =
            "INSERT INTO booklySchema.user_image (user_id, image, image_type) VALUES (?, ?, ?) " +
            "ON CONFLICT (user_id) DO UPDATE SET image = EXCLUDED.image, image_type = EXCLUDED.image_type";

    public static final String GET_USER_ORDERS =
            "SELECT * FROM booklySchema.orders WHERE order_id IN " +
            "(SELECT order_id FROM booklySchema.shoppingcart WHERE user_id = ?)";

    public static final String GET_USER_WISHLISTS =
            "SELECT * FROM booklySchema.wishlists WHERE user_id = ?";

    public static final String GET_USER_CART =
            "SELECT * FROM booklySchema.shoppingcart WHERE user_id = ?";

    public static final String CHANGE_USER_PASSWORD =
            "UPDATE booklySchema.users SET password = md5(?) WHERE user_id = ?";
}
