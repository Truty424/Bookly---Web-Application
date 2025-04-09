package it.unipd.bookly.dao.user;

import it.unipd.bookly.Resource.Image;
import it.unipd.bookly.Resource.User;
import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static it.unipd.bookly.dao.user.UserQueries.AUTHENTICATE_USER;

/**
 * DAO to authenticate a user using email and password.
 */
public class AuthenticateUserDAO extends AbstractDAO<User> {

    private final String email;
    private final String password;

    public AuthenticateUserDAO(Connection con, String email, String password) {
        super(con);
        this.email = email;
        this.password = password;
    }

    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmt = con.prepareStatement(AUTHENTICATE_USER)) {
            stmt.setString(1, email);
            stmt.setString(2, password); // Note: Password must already be hashed

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Assuming profile image is handled as byte[] and contentType
                    byte[] imageData = rs.getBytes("image_data");
                    String contentType = rs.getString("image_content_type");

                    Image image = (imageData != null && contentType != null)
                            ? new Image(imageData, contentType)
                            : null;

                    this.outputParam = new User(
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getString("role"),
                        image
                    );
                } else {
                    this.outputParam = null; // User not found
                }
            }
        }
    }
}
