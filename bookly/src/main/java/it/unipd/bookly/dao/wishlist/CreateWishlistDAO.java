package it.unipd.bookly.dao.wishlist;

import it.unipd.bookly.Resource.Wishlist;
import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static it.unipd.bookly.dao.wishlist.WishlistQueries.CREATE_WISHLIST;

public class CreateWishlistDAO extends AbstractDAO<Wishlist> {

    private final int userId;

    public CreateWishlistDAO(Connection con, int userId) {
        super(con);
        this.userId = userId;
    }

    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmt = con.prepareStatement(CREATE_WISHLIST)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Wishlist wishlist = new Wishlist();
                    wishlist.setWishlistId(rs.getInt("wishlist_id"));
                    outputParam = wishlist;
                } else {
                    throw new Exception("Failed to create wishlist.");
                }
            }
        }
    }
}
