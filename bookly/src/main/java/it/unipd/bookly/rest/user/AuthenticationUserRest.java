// package it.unipd.bookly.rest.user;

// import java.io.IOException;
// import java.sql.Connection;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import it.unipd.bookly.Resource.Message;
// import it.unipd.bookly.dao.user.LoginUserDAO;
// import it.unipd.bookly.rest.AbstractRestResource;
// import it.unipd.bookly.utilities.JWTUtil;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;

// /**
//  * Handles user authentication:
//  * - POST /api/auth
//  */
// public class AuthenticationUserRest extends AbstractRestResource {

//     public AuthenticationUserRest(HttpServletRequest req, HttpServletResponse res, Connection con) {
//         super("user-authentication", req, res, con);
//     }

//     @Override
//     protected void doServe() throws IOException {
//         String method = req.getMethod();
//         String path = req.getRequestURI();

//         try {
//             if (!"POST".equals(method)) {
//                 res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
//                 new Message("Only POST method supported for this endpoint.", "405", "Use POST for user authentication.")
//                     .toJSON(res.getOutputStream());
//                 return;
//             }

//             if (path.endsWith("/auth")) {
//                 handleAuthentication();
//             } else {
//                 res.setStatus(HttpServletResponse.SC_NOT_FOUND);
//                 new Message("Invalid authentication query path.", "404", "Check supported routes.")
//                     .toJSON(res.getOutputStream());
//             }

//         } catch (Exception e) {
//             LOGGER.error("AuthenticationUserRest error: ", e);
//             res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//             new Message("Internal server error.", "E500", e.getMessage()).toJSON(res.getOutputStream());
//         }
//     }

//     private void handleAuthentication() throws Exception {
//         String username = req.getParameter("username");
//         String password = req.getParameter("password");

//         if (username == null || password == null) {
//             res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//             new Message("Missing username or password.", "400", "Username and password are required.")
//                 .toJSON(res.getOutputStream());
//             return;
//         }

//         LoginUserDAO loginDAO = new LoginUserDAO(con);
//         boolean authenticated = loginDAO.authenticateUser(username, password);

//         if (authenticated) {
//             String token = JWTUtil.generateToken(username);
//             res.setStatus(HttpServletResponse.SC_OK);
//             res.setContentType("application/json;charset=UTF-8");
//             new ObjectMapper().writeValue(res.getOutputStream(), new Message("Authentication successful.", "200", token));
//         } else {
//             res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//             new Message("Invalid username or password.", "401", "Authentication failed.")
//                 .toJSON(res.getOutputStream());
//         }
//     }
// }
