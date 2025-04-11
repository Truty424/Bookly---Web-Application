package it.unipd.bookly.dao.image;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import it.unipd.bookly.Resource.Image;
import it.unipd.bookly.dao.AbstractDAO;

public class BookImageLoaderDAO extends AbstractDAO<Image> {

    private static final String STATEMENT_LOAD_RECIPE_IMAGE = "SELECT * FROM booklySchema.book_image WHERE book_id = ?";
    private final int book_id;

    public BookImageLoaderDAO(final Connection con, int book_id) {
        super(con);
        this.book_id = book_id;
    }

    @Override
    protected void doAccess() throws SQLException {
        try (PreparedStatement pstmt = con.prepareStatement(STATEMENT_LOAD_RECIPE_IMAGE)) {
            pstmt.setInt(1, book_id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    this.outputParam = new Image(rs.getBytes("image"), rs.getString("image_type"));
                }
            }
        }
    }
}
