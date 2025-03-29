package it.unipd.bookly.dao.author;

import it.unipd.bookly.dao.AbstractDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import static it.unipd.bookly.dao.author.AuthorQueries.DELETE_AUTHOR;

/**
 * DAO to delete an author from the database.
 */
public class DeleteAuthorDAO extends AbstractDAO<Boolean> {

    private final int authorId;

    public DeleteAuthorDAO(Connection con, int authorId) {
        super(con);
        this.authorId = authorId;
    }

    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmt = con.prepareStatement(DELETE_AUTHOR)) {
            stmt.setInt(1, authorId);

            int rowsDeleted = stmt.executeUpdate();
            this.outputParam = rowsDeleted > 0;
        }
    }
}
