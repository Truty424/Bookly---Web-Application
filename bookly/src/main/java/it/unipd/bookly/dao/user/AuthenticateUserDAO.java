package it.unipd.bookly.dao.user;

import it.unipd.bookly.dao.AbstractDAO;
import it.unipd.bookly.Resource.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import static it.unipd.bookly.dao.user.UserQueries.AUTHENTICATE_USER;

/**
 * DAO for authenticating a user.
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
        try (PreparedStatement stmnt = con.prepareStatement(AUTHENTICATE_USER)) {
            stmnt.setString(1, email);
            stmnt.setString(2, password); // Direct password comparison
            try (ResultSet rs = stmnt.executeQuery()) {
                if (rs.next()) {
                    outputParam = new User(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("role")
                    );
                }
            }
        }
    }
}