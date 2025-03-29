package it.unipd.bookly.dao.author;

import it.unipd.bookly.dao.AbstractDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import static it.unipd.bookly.dao.author.AuthorQueries.UPDATE_AUTHOR;

/**
 * DAO to update an existing author in the database.
 */
public class UpdateAuthorDAO extends AbstractDAO<Boolean> {

    private final int authorId;
    private final String firstName;
    private final String lastName;
    private final String biography;
    private final String nationality;

    public UpdateAuthorDAO(Connection con, int authorId, String firstName, String lastName, String biography, String nationality) {
        super(con);
        this.authorId = authorId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.biography = biography;
        this.nationality = nationality;
    }

    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmt = con.prepareStatement(UPDATE_AUTHOR)) {
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setString(3, biography);
            stmt.setString(4, nationality);
            stmt.setInt(5, authorId);

            int rowsUpdated = stmt.executeUpdate();
            this.outputParam = rowsUpdated > 0;
        }
    }
}
