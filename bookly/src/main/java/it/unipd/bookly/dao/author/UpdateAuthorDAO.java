package it.unipd.bookly.dao.author;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import it.unipd.bookly.Resource.Author;
import it.unipd.bookly.dao.AbstractDAO;
import static it.unipd.bookly.dao.author.AuthorQueries.UPDATE_AUTHOR;

/**
 * DAO to update an existing author's details in the database.
 */
public class UpdateAuthorDAO extends AbstractDAO<Boolean> {

    private static final Logger LOGGER = LogManager.getLogger(UpdateAuthorDAO.class);

    private final Author author;

    /**
     * Constructs a DAO to update an author.
     *
     * @param con the DB connection
     * @param author the Author object containing updated data
     */
    public UpdateAuthorDAO(final Connection con, Author author) {
        super(con);
        this.author = author;
    }

    /**
     * Executes the update query using values from the Author object.
     */
    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmt = con.prepareStatement(UPDATE_AUTHOR)) {
            stmt.setString(1, author.getFirstName());
            stmt.setString(2, author.getLastName());
            stmt.setString(3, author.get_biography());
            stmt.setString(4, author.get_nationality());
            stmt.setInt(5, author.getAuthorId());

            int affectedRows = stmt.executeUpdate();
            this.outputParam = affectedRows > 0;

            if (this.outputParam) {
                LOGGER.info("Author {} successfully updated.", author.getAuthorId());
            } else {
                LOGGER.warn("No author updated for ID {}", author.getAuthorId());
            }
        }
    }
}
