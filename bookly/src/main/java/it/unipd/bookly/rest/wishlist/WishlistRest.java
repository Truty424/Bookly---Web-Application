package it.unipd.bookly.rest.wishlist;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.unipd.bookly.Resource.Message;
import it.unipd.bookly.Resource.Wishlist;
import it.unipd.bookly.dao.wishlist.*;
import it.unipd.bookly.rest.AbstractRestResource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;


/**
 * Handles wishlist operations:
 * - POST   /api/wishlist?userId=123           → create wishlist for user
 * - GET    /api/wishlist/user/{userId}        → get wishlists by user
 * - POST   /api/wishlist/clear/{wishlistId}   → clear wishlist
 */
public class WishlistRest extends AbstractRestResource {

    private final ObjectMapper mapper = new ObjectMapper();

    public WishlistRest(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super("wishlist", req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        final String method = req.getMethod();
        final String path = req.getRequestURI();

        try {
            switch (method) {
                case "POST" -> handleCreateWishlist(); 
                case "GET" -> {
                    if (path.matches(".*/wishlist/user/\\d+")) {
                        handleGetWishlistsByUser(path);
                    } else {
                        unsupported("GET");
                    }
                }
                default -> unsupported(method);
            }

        } catch (Exception e) {
            LOGGER.error("WishlistRest error", e);
            sendError("Server error", "500", e.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void handleCreateWishlist() throws Exception {
        final String userIdParam = req.getParameter("userId");
        if (userIdParam == null) {
            sendError("Missing userId parameter.", "400", "Required to create wishlist", HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        int userId = Integer.parseInt(userIdParam);
        Wishlist wishlist = new CreateWishlistDAO(con, userId).access().getOutputParam();

        res.setStatus(HttpServletResponse.SC_CREATED);
        res.setContentType("application/json;charset=UTF-8");
        mapper.writeValue(res.getOutputStream(), wishlist);
    }

    private void handleGetWishlistsByUser(String path) throws Exception {
        int userId = extractIdFromPath(path);
        Wishlist wishlist = new GetWishlistByUserDAO(con, userId).access().getOutputParam();

        res.setContentType("application/json;charset=UTF-8");
        res.setStatus(HttpServletResponse.SC_OK);
        mapper.writeValue(res.getOutputStream(), wishlist);
    }

    private int extractIdFromPath(String path) {
        return Integer.parseInt(path.substring(path.lastIndexOf("/") + 1));
    }

    private void sendMessage(String title, String code, String data, int status) throws IOException {
        res.setStatus(status);
        new Message(title, code, data).toJSON(res.getOutputStream());
    }

    private void sendError(String title, String code, String details, int status) throws IOException {
        res.setStatus(status);
        new Message(title, code, details).toJSON(res.getOutputStream());
    }

    private void unsupported(String method) throws IOException {
        sendError("Unsupported " + method + " operation", "405", method + " not allowed on this endpoint", HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }
}
