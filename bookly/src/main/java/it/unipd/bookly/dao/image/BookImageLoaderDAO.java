
package it.unipd.bookly.dao.image;

import it.unipd.yummycenter.Resource.Image;
import it.unipd.yummycenter.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookImageLoaderDAO extends AbstractDAO<Image> {
    private static final String STATEMENT_LOAD_RECIPE_IMAGE = "SELECT * FROM booklySchema.recipe_image WHERE recipe_id = ?";
    private final int recipeId;

    public BookImageLoaderDAO(final Connection con, int recipeId) {
        super(con);
        this.recipeId = recipeId;
    }

    @Override
    protected void doAccess() throws SQLException {
        try (PreparedStatement pstmt = con.prepareStatement(STATEMENT_LOAD_RECIPE_IMAGE)) {
            pstmt.setInt(1, recipeId);
            try(ResultSet rs = pstmt.executeQuery()) {
                if (rs.next())
                    this.outputParam = new Image(rs.getBytes("image"), rs.getString("image_type"));
            }
        }
    }
}
