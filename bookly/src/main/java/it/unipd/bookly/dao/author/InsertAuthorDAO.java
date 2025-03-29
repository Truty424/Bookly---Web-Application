package it.unipd.bookly.dao.author;

import java.sql.Connection;
import java.sql.PreparedStatement;

import it.unipd.bookly.dao.AbstractDAO;
import static it.unipd.bookly.dao.author.AuthorQueries.INSERT_AUTHOR;

/**
 * DAO to insert a new author into the database.
 */
public class InsertAuthorDAO extends AbstractDAO<Boolean> {

    private final String firstName;
    private final String lastName;
    private final String biography;
    private final String nationality;

    public InsertAuthorDAO(Connection con, String firstName, String lastName, String biography, String nationality) {
        super(con);
        this.firstName = firstName;
        this.lastName = lastName;
        this.biography = biography;
        this.nationality = nationality;
    }

    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmt = con.prepareStatement(INSERT_AUTHOR)) {
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setString(3, biography);
            stmt.setString(4, nationality);

            int rowsInserted = stmt.executeUpdate();
            this.outputParam = rowsInserted > 0;
        }
    }
}
