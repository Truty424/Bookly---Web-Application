package it.unipd.bookly.dao.author;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import it.unipd.bookly.Resource.Author;
import it.unipd.bookly.dao.AbstractDAO;
import static it.unipd.bookly.dao.author.AuthorQueries.GET_AUTHORS_BY_BOOK;

/**
 * DAO to retrieve authors of a specific book.
 * This class provides functionality to fetch all authors associated
 * with a specific book from the database and return them as a list of {@link Author} objects.
 */
public class GetAuthorsByBookDAO extends AbstractDAO<List<Author>> {

    /**
     * The ID of the book whose authors are to be retrieved.
     */
    private final int book_id;

    /**
     * Constructs a DAO to retrieve authors of a specific book.
     *
     * @param con     The database connection to use.
     * @param book_id The ID of the book whose authors are to be retrieved.
     */
    public GetAuthorsByBookDAO(Connection con, int book_id) {
        super(con);
        this.book_id = book_id;
    }

    /**
     * Executes the query to retrieve authors of a specific book.
     * Populates the {@link #outputParam} with a list of {@link Author} objects.
     *
     * @throws Exception If an error occurs during the database operation.
     */
    @Override
    protected void doAccess() throws Exception {
        List<Author> authors = new ArrayList<>();
        try (PreparedStatement stmt = con.prepareStatement(GET_AUTHORS_BY_BOOK)) {
            stmt.setInt(1, book_id);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    authors.add(new Author(
                            rs.getInt("author_id"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("biography"),
                            rs.getString("nationality")
                    ));
                }
            }
        }
        this.outputParam = authors;
    }
}