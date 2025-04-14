package it.unipd.bookly.dao.author;

import java.sql.Connection;
import java.sql.PreparedStatement;

import it.unipd.bookly.Resource.Author;
import it.unipd.bookly.dao.AbstractDAO;
import static it.unipd.bookly.dao.author.AuthorQueries.INSERT_AUTHOR;

/**
 * DAO to insert a new author into the database.
 */
public class InsertAuthorDAO extends AbstractDAO<Boolean> {

    private final Author author;

    public InsertAuthorDAO(Connection con, Author author) {
        super(con);
        this.author = author;
    }

    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmt = con.prepareStatement(INSERT_AUTHOR)) {
            stmt.setString(1, author.getFirst_name());
            stmt.setString(2, author.getLast_name());
            stmt.setString(3, author.get_biography());
            stmt.setString(4, author.get_nationality());

            int rowsInserted = stmt.executeUpdate();
            this.outputParam = rowsInserted > 0;
        }
    }
}
