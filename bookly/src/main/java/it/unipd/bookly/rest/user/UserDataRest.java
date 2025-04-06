package it.unipd.bookly.rest.user;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unipd.bookly.Resource.Message;
import it.unipd.bookly.dao.user.GetUserByIdDAO;
import it.unipd.bookly.dao.user.UpdateUserDAO;
import it.unipd.bookly.rest.AbstractRestResource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Handles user data retrieval and updates:
 * - GET /api/user/{id}
 * - PUT /api/user/{id}
 */
public class UserDataRest extends AbstractRestResource {

    public UserDataRest(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super("user-data", req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        String method = req.getMethod();
        String path = req.getRequestURI();

        try {
            if ("GET".equals(method) && path.matches(".*/user/\\d+$")) {
                handleGetById(path);
            } else if ("PUT".equals(method) && path.matches(".*/user/\\d+$")) {
                handleUpdate(path);
            } else {
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                new Message("Invalid user query path.", "404", "Check supported routes.")
                    .toJSON(res.getOutputStream());
            }

        } catch (Exception e) {
            LOGGER.error("UserDataRest error: ", e);
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            new Message("Internal server error.", "E500", e.getMessage()).toJSON(res.getOutputStream());
        }
    }

    private void handleGetById(String path) throws Exception {
        int userId = Integer.parseInt(path.substring(path.lastIndexOf("/") + 1));
        GetUserByIdDAO userDAO = new GetUserByIdDAO(con, userId);
        User user = userDAO.getUserById();

        if (user == null) {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            new Message("User not found.", "404", "No user with ID " + userId).toJSON(res.getOutputStream());
        } else {
            res.setStatus(HttpServletResponse.SC_OK);
            res.setContentType("application/json;charset=UTF-8");
            new ObjectMapper().writeValue(res.getOutputStream(), user);
        }
    }

    private void handleUpdate(String path) throws Exception {
        int userId = Integer.parseInt(path.substring(path.lastIndexOf("/") + 1));
        String newEmail = req.getParameter("email");
        String newPhone = req.getParameter("phone");

        UpdateUserDAO updateUserDAO = new UpdateUserDAO(con, userId, newEmail, newPhone);
        boolean updated = updateUserDAO.updateUser();

        if (updated) {
            res.setStatus(HttpServletResponse.SC_OK);
            new Message("User updated successfully.", "200", "User data updated").toJSON(res.getOutputStream());
        } else {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            new Message("User not found.", "404", "No user with ID " + userId).toJSON(res.getOutputStream());
        }
    }
}
