package it.unipd.bookly.dao.author;

import java.sql.Connection;
import java.sql.PreparedStatement;

import it.unipd.bookly.dao.AbstractDAO;
import static it.unipd.bookly.dao.author.AuthorQueries.ADD_AUTHOR_TO_BOOK;

/**
 * DAO to add an author to a book.
 */
public class AddAuthorToBookDAO extends AbstractDAO<Boolean> {

    private final int bookId;
    private final int authorId;

    public AddAuthorToBookDAO(Connection con, int bookId, int authorId) {
        super(con);
        this.bookId = bookId;
        this.authorId = authorId;
    }

    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmt = con.prepareStatement(ADD_AUTHOR_TO_BOOK)) {
            stmt.setInt(1, bookId);
            stmt.setInt(2, authorId);

            int rowsInserted = stmt.executeUpdate();
            this.outputParam = rowsInserted > 0;
        }
    }
}
