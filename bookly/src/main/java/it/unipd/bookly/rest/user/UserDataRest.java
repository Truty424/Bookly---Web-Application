// package it.unipd.bookly.rest.user;

// import it.unipd.bookly.Resource.Cart;
// import it.unipd.bookly.Resource.Order;
// import it.unipd.bookly.Resource.Wishlist;
// import it.unipd.bookly.Resource.Message;
// import it.unipd.bookly.dao.user.GetUserCartDAO;
// import it.unipd.bookly.dao.user.GetUserOrdersDAO;
// import it.unipd.bookly.dao.user.GetUserWishlistsDAO;
// import it.unipd.bookly.rest.AbstractRestResource;

// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;

// import java.io.IOException;
// import java.sql.Connection;
// import java.util.List;

// public class UserDataRest extends AbstractRestResource {

//     public UserDataRest(HttpServletRequest req, HttpServletResponse res, Connection con) {
//         super("user-data", req, res, con);
//     }

//     @Override
//     protected void doServe() throws IOException {
//         final String method = req.getMethod();
//         final String path = req.getRequestURI();

//         try {
//             if (!"GET".equals(method)) {
//                 res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
//                 new Message("Only GET method is supported.", "405", "Use GET for retrieving user-related data.")
//                     .toJSON(res.getOutputStream());
//                 return;
//             }

//             if (path.matches(".*/user/\\d+/cart")) {
//                 handleGetCart();
//             } else if (path.matches(".*/user/\\d+/orders")) {
//                 handleGetOrders();
//             } else if (path.matches(".*/user/\\d+/wishlists")) {
//                 handleGetWishlists();
//             } else {
//                 res.setStatus(HttpServletResponse.SC_NOT_FOUND);
//                 new Message("Invalid path.", "404", "Supported: /user/{id}/cart, /orders, /wishlists")
//                     .toJSON(res.getOutputStream());
//             }

//         } catch (Exception e) {
//             LOGGER.error("Error handling user data request", e);
//             res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//             new Message("Internal server error", "E500", e.getMessage()).toJSON(res.getOutputStream());
//         }
//     }

//     private void handleGetCart() throws Exception {
//         int userId = extractUserId("cart");
//         Cart cart = new GetUserCartDAO(con, userId).access().getOutputParam();

//         res.setContentType("application/json;charset=UTF-8");
//         res.setStatus(HttpServletResponse.SC_OK);
//         cart.toJSON(res.getOutputStream());
//     }

//     private void handleGetOrders() throws Exception {
//         int userId = extractUserId("orders");
//         List<Order> orders = new GetUserOrdersDAO(con, userId).access().getOutputParam();

//         res.setContentType("application/json;charset=UTF-8");
//         res.setStatus(HttpServletResponse.SC_OK);
//         for (Order order : orders) {
//             order.toJSON(res.getOutputStream());
//         }
//     }

//     private void handleGetWishlists() throws Exception {
//         int userId = extractUserId("wishlists");
//         List<Wishlist> wishlists = new GetUserWishlistsDAO(con, userId).access().getOutputParam();

//         res.setContentType("application/json;charset=UTF-8");
//         res.setStatus(HttpServletResponse.SC_OK);
//         for (Wishlist wishlist : wishlists) {
//             wishlist.toJSON(res.getOutputStream());
//         }
//     }

//     private int extractUserId(String resourceName) throws Exception {
//         String path = req.getRequestURI();
//         int start = path.indexOf("/user/") + 6;
//         int end = path.indexOf("/" + resourceName);
//         return Integer.parseInt(path.substring(start, end));
//     }
// }
