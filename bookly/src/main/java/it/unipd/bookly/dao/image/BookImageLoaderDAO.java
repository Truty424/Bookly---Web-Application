
package it.unipd.bookly.dao.image;

import it.unipd.bookly.Resource.Image;
import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookImageLoaderDAO extends AbstractDAO<Image> {
    private static final String STATEMENT_LOAD_RECIPE_IMAGE = "SELECT * FROM booklySchema.book_image WHERE book_id = ?";
    private final int bookId;

    public BookImageLoaderDAO(final Connection con, int bookId) {
        super(con);
        this.bookId = bookId;
    }

    @Override
    protected void doAccess() throws SQLException {
        try (PreparedStatement pstmt = con.prepareStatement(STATEMENT_LOAD_RECIPE_IMAGE)) {
            pstmt.setInt(1, bookId);
            try(ResultSet rs = pstmt.executeQuery()) {
                if (rs.next())
                    this.outputParam = new Image(rs.getBytes("image"), rs.getString("image_type"));
            }
        }
    }
}
