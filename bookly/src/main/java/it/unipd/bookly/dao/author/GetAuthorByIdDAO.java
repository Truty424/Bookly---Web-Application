package it.unipd.bookly.dao.author;

import it.unipd.bookly.dao.AbstractDAO;
import it.unipd.bookly.Resource.Author;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import static it.unipd.bookly.dao.author.AuthorQueries.GET_AUTHOR_BY_ID;

/**
 * DAO to retrieve an author by their ID.
 */
public class GetAuthorByIdDAO extends AbstractDAO<Author> {

    private final int authorId;

    public GetAuthorByIdDAO(Connection con, int authorId) {
        super(con);
        this.authorId = authorId;
    }

    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmt = con.prepareStatement(GET_AUTHOR_BY_ID)) {
            stmt.setInt(1, authorId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    this.outputParam = new Author(
                        rs.getInt("author_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("biography"),
                        rs.getString("nationality")
                    );
                }
            }
        }
    }
}
