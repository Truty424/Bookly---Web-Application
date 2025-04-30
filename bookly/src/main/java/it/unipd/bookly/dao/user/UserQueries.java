package it.unipd.bookly.dao.user;

public final class UserQueries {

    private UserQueries() {
    }
    public static final String LOGIN_USER
            = "SELECT user_id, username, password, firstName, lastName, email, phone, address, role "
            + "FROM booklySchema.users WHERE username = ? AND password = md5(?)";

    public static final String REGISTER_USER
            = "INSERT INTO booklySchema.users (username, password, firstName, lastName, email, phone, address, role) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?::booklySchema.user_role)";

public static final String UPDATE_USER =
    "UPDATE booklySchema.users SET username = ?, firstName = ?, lastName = ?, email = ?, phone = ?, address = ?, role = ?::booklySchema.user_role WHERE user_id = ?";
    public static final String GET_USERS_BY_USERNAME = "SELECT * FROM booklySchema.users WHERE username = ?";

    public static final String UPDATE_USER_IMAGE_IF_EXISTS = "INSERT INTO booklySchema.user_image (user_id, image, image_type) VALUES (?, ?, ?) ON CONFLICT (user_id) DO UPDATE SET image = EXCLUDED.image, image_type = EXCLUDED.image_type;";

    public static final String CHANGE_USER_PASSWORD = "UPDATE booklySchema.users SET password = md5(?) WHERE user_id = ?";


    public static final String GET_USER_IMAGE = "SELECT image, image_type FROM booklySchema.user_image WHERE user_id = ?";

    public static final String AUTHENTICATE_USER = "SELECT user_id, username, email, password, role FROM booklySchema.users WHERE user_id = ?";
}
