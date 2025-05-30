package it.unipd.bookly.dao.author;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import it.unipd.bookly.Resource.Author;
import it.unipd.bookly.dao.AbstractDAO;
import static it.unipd.bookly.dao.author.AuthorQueries.INSERT_AUTHOR;

/**
 * DAO to insert a new author into the database. This class provides
 * functionality to add a new author record to the database using the provided
 * {@link Author} object.
 */
public class InsertAuthorDAO extends AbstractDAO<Boolean> {

    /**
     * The {@link Author} object containing the details of the author to be
     * inserted.
     */
    private final Author author;

    /**
     * Constructs a DAO to insert a new author into the database.
     *
     * @param con The database connection to use.
     * @param author The {@link Author} object containing the details of the
     * author to be inserted.
     */
    public InsertAuthorDAO(Connection con, Author author) {
        super(con);
        this.author = author;
    }

    /**
     * Executes the query to insert a new author into the database. Populates
     * the {@link #outputParam} with {@code true} if the insertion was
     * successful, {@code false} otherwise.
     *
     * @throws Exception If an error occurs during the database operation.
     */
    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmt = con.prepareStatement(INSERT_AUTHOR, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, author.getFirstName());
            stmt.setString(2, author.getLastName());
            stmt.setString(3, author.getBiography());
            stmt.setString(4, author.getNationality());

            int rowsInserted = stmt.executeUpdate();
            this.outputParam = rowsInserted > 0;

            if (rowsInserted > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        int generatedId = rs.getInt(1);
                        author.setAuthor_id(generatedId); // ✅ Set generated ID
                        LOGGER.info("Author '{} {}' inserted with ID {}", author.getFirstName(), author.getLastName(), generatedId);
                    } else {
                        LOGGER.warn("Author inserted but no generated ID returned.");
                    }
                }
            } else {
                LOGGER.warn("No rows inserted for author '{} {}'.", author.getFirstName(), author.getLastName());
            }

        } catch (Exception ex) {
            LOGGER.error("Error inserting author '{} {}': {}", author.getFirstName(), author.getLastName(), ex.getMessage(), ex);
            throw ex;
        }
    }
}
