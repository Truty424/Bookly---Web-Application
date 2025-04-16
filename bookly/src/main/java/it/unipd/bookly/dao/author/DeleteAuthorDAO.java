package it.unipd.bookly.dao.author;

import it.unipd.bookly.dao.AbstractDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import static it.unipd.bookly.dao.author.AuthorQueries.DELETE_AUTHOR;

/**
 * DAO to delete an author from the database.
 * This class provides the functionality to remove an author record
 * from the database using the author's ID.
 */
public class DeleteAuthorDAO extends AbstractDAO<Boolean> {

    /**
     * The ID of the author to be deleted.
     */
    private final int authorId;

    /**
     * Constructs a DAO to delete an author from the database.
     *
     * @param con      The database connection.
     * @param authorId The ID of the author to be deleted.
     */
    public DeleteAuthorDAO(Connection con, int authorId) {
        super(con);
        this.authorId = authorId;
    }

    /**
     * Executes the query to delete an author from the database.
     *
     * @throws Exception If an error occurs during the database operation.
     */
    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmt = con.prepareStatement(DELETE_AUTHOR)) {
            stmt.setInt(1, authorId);

            int rowsDeleted = stmt.executeUpdate();
            this.outputParam = rowsDeleted > 0;
        }
    }
}