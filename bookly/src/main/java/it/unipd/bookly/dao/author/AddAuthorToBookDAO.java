package it.unipd.bookly.dao.author;

import java.sql.Connection;
import java.sql.PreparedStatement;

import it.unipd.bookly.dao.AbstractDAO;
import static it.unipd.bookly.dao.author.AuthorQueries.ADD_AUTHOR_TO_BOOK;

/**
 * DAO to add an author to a book.
 */
public class AddAuthorToBookDAO extends AbstractDAO<Boolean> {

    private final int book_id;
    private final int authorId;

    public AddAuthorToBookDAO(Connection con, int book_id, int authorId) {
        super(con);
        this.book_id = book_id;
        this.authorId = authorId;
    }

    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmt = con.prepareStatement(ADD_AUTHOR_TO_BOOK)) {
            stmt.setInt(1, book_id);
            stmt.setInt(2, authorId);

            int rowsInserted = stmt.executeUpdate();
            this.outputParam = rowsInserted > 0;
        }
    }
}
