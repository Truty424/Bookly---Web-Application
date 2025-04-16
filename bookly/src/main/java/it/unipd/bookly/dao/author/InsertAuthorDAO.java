package it.unipd.bookly.dao.author;

import java.sql.Connection;
import java.sql.PreparedStatement;

import it.unipd.bookly.Resource.Author;
import it.unipd.bookly.dao.AbstractDAO;
import static it.unipd.bookly.dao.author.AuthorQueries.INSERT_AUTHOR;

/**
 * DAO to insert a new author into the database.
 * This class provides functionality to add a new author record
 * to the database using the provided {@link Author} object.
 */
public class InsertAuthorDAO extends AbstractDAO<Boolean> {

    /**
     * The {@link Author} object containing the details of the author to be inserted.
     */
    private final Author author;

    /**
     * Constructs a DAO to insert a new author into the database.
     *
     * @param con    The database connection to use.
     * @param author The {@link Author} object containing the details of the author to be inserted.
     */
    public InsertAuthorDAO(Connection con, Author author) {
        super(con);
        this.author = author;
    }

    /**
     * Executes the query to insert a new author into the database.
     * Populates the {@link #outputParam} with {@code true} if the insertion was successful, {@code false} otherwise.
     *
     * @throws Exception If an error occurs during the database operation.
     */
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