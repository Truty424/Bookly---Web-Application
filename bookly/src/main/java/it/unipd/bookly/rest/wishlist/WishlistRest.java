package it.unipd.bookly.rest.wishlist;

import it.unipd.bookly.Resource.Message;
import it.unipd.bookly.Resource.Wishlist;
import it.unipd.bookly.dao.wishlist.*;
import it.unipd.bookly.rest.AbstractRestResource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;


public class WishlistRest extends AbstractRestResource {

    public WishlistRest(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super("wishlist", req, res, con);
    }

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doServe() throws IOException {
        final String method = req.getMethod();
        final String path = req.getRequestURI();
        Message message = null;

        try {
            if ("POST".equals(method)) {
                handleCreateWishlist();
            } else if ("DELETE".equals(method) && path.matches(".*/wishlist/\\d+")) {
                handleDeleteWishlist(path);
            } else if ("GET".equals(method) && path.matches(".*/wishlist/user/\\d+")) {
                handleGetWishlistsByUser(path);
            } else if ("POST".equals(method) && path.matches(".*/wishlist/clear/\\d+")) {
                handleClearWishlist(path);
            } else {
                res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                message = new Message("Unsupported wishlist operation", "405", "Only POST, DELETE, and GET are supported");
                message.toJSON(res.getOutputStream());
            }

        } catch (Exception e) {
            LOGGER.error("WishlistRest error", e);
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            message = new Message("Server error", "500", e.getMessage());
            message.toJSON(res.getOutputStream());
        }
    }

    private void handleCreateWishlist() throws Exception {
        final String userIdParam = req.getParameter("userId");
        if (userIdParam == null) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            new Message("Missing userId parameter.", "400", "Required to create wishlist").toJSON(res.getOutputStream());
            return;
        }
        int userId = Integer.parseInt(userIdParam);
        Wishlist wishlist = new CreateWishlistDAO(con, userId).access().getOutputParam();
        res.setStatus(HttpServletResponse.SC_CREATED);
        mapper.writeValue(res.getOutputStream(), wishlist);
    }

    private void handleDeleteWishlist(String path) throws Exception {
        int wishlistId = Integer.parseInt(path.substring(path.lastIndexOf("/") + 1));
        Wishlist wishlist = new Wishlist();
        wishlist.setWishlistId(wishlistId);
        new DeleteWishlistDAO(con, wishlist).access();
        res.setStatus(HttpServletResponse.SC_OK);
        new Message("Wishlist deleted successfully.", "200", null).toJSON(res.getOutputStream());
    }

    private void handleGetWishlistsByUser(String path) throws Exception {
        int userId = Integer.parseInt(path.substring(path.lastIndexOf("/") + 1));
        List<Wishlist> wishlists = new GetWishlistByUserDAO(con, userId).access().getOutputParam();
        String wishlistJson = mapper.writeValueAsString(wishlists);

        res.setStatus(HttpServletResponse.SC_OK);
        res.setContentType("application/json;charset=UTF-8");

        new Message("Wishlists fetched successfully.", "200", wishlistJson).toJSON(res.getOutputStream());
    }

    private void handleClearWishlist(String path) throws Exception {
        int wishlistId = Integer.parseInt(path.substring(path.lastIndexOf("/") + 1));
        Wishlist wishlist = new Wishlist();
        wishlist.setWishlistId(wishlistId);
        new ClearWishlistDAO(con, wishlist).access();
        res.setStatus(HttpServletResponse.SC_OK);
        new Message("Wishlist cleared successfully.", "200", null).toJSON(res.getOutputStream());
    }
}
