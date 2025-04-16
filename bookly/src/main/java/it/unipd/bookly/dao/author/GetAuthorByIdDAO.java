package it.unipd.bookly.dao.author;

import it.unipd.bookly.dao.AbstractDAO;
import it.unipd.bookly.Resource.Author;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import static it.unipd.bookly.dao.author.AuthorQueries.GET_AUTHOR_BY_ID;

/**
 * DAO to retrieve an author by their ID.
 * This class provides functionality to fetch an author record
 * from the database using the author's unique ID.
 */
public class GetAuthorByIdDAO extends AbstractDAO<Author> {

    /**
     * The ID of the author to retrieve.
     */
    private final int authorId;

    /**
     * Constructs a DAO to retrieve an author by their ID.
     *
     * @param con      The database connection to use.
     * @param authorId The ID of the author to retrieve.
     */
    public GetAuthorByIdDAO(Connection con, int authorId) {
        super(con);
        this.authorId = authorId;
    }

    /**
     * Executes the query to retrieve an author by their ID.
     * Populates the {@link #outputParam} with the retrieved {@link Author} object.
     *
     * @throws Exception If an error occurs during the database operation.
     */
    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmt = con.prepareStatement(GET_AUTHOR_BY_ID)) {
            stmt.setInt(1, authorId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    this.outputParam = new Author(
                        rs.getInt("author_id"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("biography"),
                        rs.getString("nationality")
                    );
                }
            }
        }
    }
}