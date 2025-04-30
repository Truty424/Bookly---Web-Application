package it.unipd.bookly.dao.author;

import it.unipd.bookly.dao.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
        boolean originalAutoCommit = con.getAutoCommit();
        con.setAutoCommit(false); // Begin transaction

        try {
            try (PreparedStatement disassociateStmt = con.prepareStatement(
                    "DELETE FROM booklySchema.written_by WHERE author_id = ?")) {
                disassociateStmt.setInt(1, authorId);
                disassociateStmt.executeUpdate();
            }

            try (PreparedStatement stmt = con.prepareStatement(DELETE_AUTHOR)) {
                stmt.setInt(1, authorId);
                int rowsDeleted = stmt.executeUpdate();
                this.outputParam = rowsDeleted > 0;

                if (outputParam) {
                    LOGGER.info("Author with ID {} deleted successfully.", authorId);
                } else {
                    LOGGER.warn("No author found with ID {}. Nothing deleted.", authorId);
                }
            }

            con.commit();
        } catch (SQLException ex) {
            con.rollback(); 
            LOGGER.error("Error deleting author ID {}: {}", authorId, ex.getMessage());
            throw ex;
        } finally {
            con.setAutoCommit(originalAutoCommit);
        }
    }
}
