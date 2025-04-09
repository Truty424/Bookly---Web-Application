package it.unipd.bookly.dao.author;

import it.unipd.bookly.dao.AbstractDAO;
import it.unipd.bookly.Resource.Author;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static it.unipd.bookly.dao.author.AuthorQueries.GET_ALL_AUTHORS;

public class GetAllAuthorsDAO extends AbstractDAO<List<Author>> {

    /**
     * Creates a new DAO to retrieve all authors from the database.
     *
     * @param con the database connection to use.
     */
    public GetAllAuthorsDAO(final Connection con) {
        super(con);
    }

    @Override
    protected void doAccess() throws Exception {
        List<Author> authors = new ArrayList<>();

        try (PreparedStatement stmnt = con.prepareStatement(GET_ALL_AUTHORS)) {
            try (ResultSet rs = stmnt.executeQuery()) {
                while (rs.next()) {
                    int authorId = rs.getInt("author_id");
                    String firstName = rs.getString("first_name");
                    String lastName = rs.getString("last_name");
                    String biography = rs.getString("biography");
                    String nationality = rs.getString("nationality");

                    authors.add(new Author(authorId, firstName, lastName, biography, nationality));
                }
            }

            this.outputParam = authors;

            if (!authors.isEmpty()) {
                LOGGER.info("First Author loaded: {} {}", authors.get(0).getFirst_name(), authors.get(0).
                getLast_name())
                  
            ;
            } else {
                LOGGER.info("No authors found in the database.");
            }

        } catch (Exception ex) {
            LOGGER.error("Error retrieving authors: {}", ex.getMessage());
        }
    }
}
