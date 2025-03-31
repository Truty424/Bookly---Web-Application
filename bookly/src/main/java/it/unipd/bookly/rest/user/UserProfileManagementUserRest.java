package it.unipd.bookly.rest.user;

import it.unipd.bookly.Resource.Message;
import it.unipd.bookly.dao.user.UpdateUserProfileDAO;
import it.unipd.bookly.rest.AbstractRestResource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;

/**

REST endpoint for user profile management.

Endpoint: PUT /api/user/profile?id=123&name=NewName&email=new@example.com */
public class UserProfileManagementRest
extends AbstractRestResource {

public UserProfileManagementRest(HttpServletRequest req, HttpServletResponse res, Connection con) { super("user-profile-management", req, res, con); }

@Override protected void doServe() throws IOException { String userIdParam = req.getParameter("id"); String name = req.getParameter("name"); String email = req.getParameter("email");

if (userIdParam == null  name == null  email == null) {
     Message message = new Message("Missing user profile details.", "E400", "User ID, name, and email are required.");
     res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
     message.toJSON(res.getOutputStream());
     return;
 }

 try {
     int userId = Integer.parseInt(userIdParam);

     new UpdateUserProfileDAO(con, userId, name, email).access();

     Message message = new Message("User profile updated successfully.", "200", "Profile updated for user ID " + userId);
     res.setStatus(HttpServletResponse.SC_OK);
     message.toJSON(res.getOutputStream());
 } catch (NumberFormatException ex) {
     Message message = new Message("Invalid user ID format.", "E401", "User ID must be an integer.");
     res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
     message.toJSON(res.getOutputStream());
 } catch (Exception ex) {
     LOGGER.error("Error updating user profile", ex);
     Message message = new Message("Internal server error.", "E500", ex.getMessage());
     res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
     message.toJSON(res.getOutputStream());
 }

} }