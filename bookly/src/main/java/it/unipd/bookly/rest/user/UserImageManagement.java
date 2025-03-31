package it.unipd.bookly.rest.user;

import it.unipd.bookly.Resource.Message;
import it.unipd.bookly.dao.user.UpdateUserImageDAO;
import it.unipd.bookly.rest.AbstractRestResource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;

/**

REST endpoint to manage user profile images.

Endpoint: POST /api/user/image?id=123 */ public class UserImageRest extends AbstractRestResource {

public UserImageRest(HttpServletRequest req, HttpServletResponse res, Connection con) { super("user-image", req, res, con); }

@Override protected void doServe() throws IOException { String userIdParam = req.getParameter("id");

if (userIdParam == null) {
     Message message = new Message("Missing user ID parameter.", "E400", "User ID is required.");
     res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
     message.toJSON(res.getOutputStream());
     return;
 }

 try {
     int userId = Integer.parseInt(userIdParam);
     InputStream imageStream = req.getInputStream();
     String imageType = req.getContentType();

     if (imageStream.available() == 0) {
         Message message = new Message("No image provided.", "E400", "An image file must be uploaded.");
         res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
         message.toJSON(res.getOutputStream());
         return;
     }

     new UpdateUserImageDAO(con, userId, imageStream, imageType).access();

     Message message = new Message("User image updated successfully.", "200", "Image updated for user ID " + userId);
     res.setStatus(HttpServletResponse.SC_OK);
     message.toJSON(res.getOutputStream());
 } catch (NumberFormatException ex) {
     Message message = new Message("Invalid ID format.", "E401", "User ID must be an integer.");
     res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
     message.toJSON(res.getOutputStream());
 } catch (Exception ex) {
     LOGGER.error("Error updating user image", ex);
     Message message = new Message("Internal server error.", "E500", ex.getMessage());
     res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
     message.toJSON(res.getOutputStream());
 }

} }