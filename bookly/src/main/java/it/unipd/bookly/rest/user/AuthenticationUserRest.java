package it.unipd.bookly.rest.user;

import it.unipd.bookly.Resource.Message;
import it.unipd.bookly.dao.user.AuthenticateUserDAO;
import it.unipd.bookly.rest.AbstractRestResource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;

/**
 * REST endpoint for user authentication.
 * Endpoint: POST /api/user/authenticate
 */
public class UserAuthenticationRest extends AbstractRestResource {

    public UserAuthenticationRest(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super("user-authentication", req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        if (username == null || password == null) {
            Message message = new Message("Missing credentials.", "E400", "Username and password are required.");
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            message.toJSON(res.getOutputStream());
            return;
        }

        try {
            boolean isAuthenticated = new AuthenticateUserDAO(con, username, password).access();
            if (isAuthenticated) {
                Message message = new Message("Authentication successful.", "200", "User authenticated.");
                res.setStatus(HttpServletResponse.SC_OK);
                message.toJSON(res.getOutputStream());
            } else {
                Message message = new Message("Invalid credentials.", "E401", "Incorrect username or password.");
                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                message.toJSON(res.getOutputStream());
            }
        } catch (Exception ex) {
            LOGGER.error("Error during authentication", ex);
            Message message = new Message("Internal server error.", "E500", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            message.toJSON(res.getOutputStream());
        }
    }
}