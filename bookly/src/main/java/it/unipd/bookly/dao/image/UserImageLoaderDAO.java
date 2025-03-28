package it.unipd.bookly.dao.image;

import it.unipd.yummycenter.Resource.Image;
import it.unipd.yummycenter.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserImageLoaderDAO extends AbstractDAO<Image> {
    private static final String STATEMENT_LOAD_USER_IMAGE = "SELECT * FROM booklySchema.user_image WHERE username = ?";
    private final String username;

    public UserImageLoaderDAO(final Connection con, String username) {
        super(con);
        this.username = username;
    }

    @Override
    protected void doAccess() throws SQLException {
        try (PreparedStatement pstmt = con.prepareStatement(STATEMENT_LOAD_USER_IMAGE)) {
            pstmt.setString(1, username);
            try(ResultSet rs = pstmt.executeQuery()) {
                if (rs.next())
                    this.outputParam = new Image(rs.getBytes("image"), rs.getString("image_type"));
            }
        }
    }
}
