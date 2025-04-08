// package it.unipd.bookly.rest.user;

// import java.io.IOException;
// import java.sql.Connection;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import it.unipd.bookly.Resource.Message;
// import it.unipd.bookly.dao.user.UpdateUserDAO;
// import it.unipd.bookly.rest.AbstractRestResource;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;

// /**
//  * Handles user profile updates:
//  * - PUT /api/user/profile/{id}
//  */
// public class UserProfileManagementRest extends AbstractRestResource {

//     public UserProfileManagementRest(HttpServletRequest req, HttpServletResponse res, Connection con) {
//         super("user-profile-management", req, res, con);
//     }

//     @Override
//     protected void doServe() throws IOException {
//         String method = req.getMethod();
//         String path = req.getRequestURI();

//         try {
//             if ("PUT".equals(method) && path.matches(".*/user/profile/\\d+$")) {
//                 handleUpdateProfile(path);
//             } else {
//                 res.setStatus(HttpServletResponse.SC_NOT_FOUND);
//                 new Message("Invalid user profile management path.", "404", "Check supported routes.")
//                     .toJSON(res.getOutputStream());
//             }

//         } catch (Exception e) {
//             LOGGER.error("UserProfileManagementRest error: ", e);
//             res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//             new Message("Internal server error.", "E500", e.getMessage()).toJSON(res.getOutputStream());
//         }
//     }

//     private void handleUpdate(String path) throws Exception {
//         int userId = Integer.parseInt(path.substring(path.lastIndexOf("/") + 1));
//         String newName = req.getParameter("name");
//         String newEmail = req.getParameter("email");

//         UpdateUserDAO updateUserDAO = new UpdateUserDAO(con, userId, newName, newEmail);
//         boolean updated = updateUserDAO.update();

//         if (updated) {
//             res.setStatus(HttpServletResponse.SC_OK);
//             new Message("Profile updated successfully.", "200", "User profile updated").toJSON(res.getOutputStream());
//         } else {
//             res.setStatus(HttpServletResponse.SC_NOT_FOUND);
//             new Message("User not found.", "404", "No user with ID " + userId).toJSON(res.getOutputStream());
//         }
//     }
// }
